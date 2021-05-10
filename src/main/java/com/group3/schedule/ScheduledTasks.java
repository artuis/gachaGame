package com.group3.schedule;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.group3.beans.Event;
import com.group3.beans.Gamer;
import com.group3.data.EventRepository;
import com.group3.data.GamerRepository;

@Component
@Order(1)
public class ScheduledTasks implements CommandLineRunner {
	private Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	@Autowired
	private GamerRepository gamerRepo;
	@Autowired
	private EventRepository eventRepo;
	// ScheduledTasks will begin a thread and run after the Driver finishes initialization
	
	@Override
	public void run(String... args) throws Exception {
		// intentionally blank, perhaps log later
	}
	
	// documentation for @Scheduling: https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling
	// documentation for cron format: https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling-cron-expression

	/*
	 Daily Roll Resetter
	 scheduled for 00:00:00 daily: get list of all gamers,
	 for each gamer in list, set daily rolls to 10
	 save updated gamer, no returned needed for void
	 */
	
	@Scheduled(cron="0 0 0 * * *")
	public void dailyRollsReset() {
		log.debug("Resetting daily free rolls");
		gamerRepo.findAll().collectList()
		.flatMap(gamers -> {
			for(Gamer gg : gamers) {
				gg.setDailyRolls(10);
				gamerRepo.save(gg).subscribe();
			}
			return null;
		});
	}

	/*
	 Daily Ban Resetter
	 checks for ban lifts every minute, pull today's date for reference, 
	 get all gamers that are banned, collect them to a list,
	 then map the contents; for each gamer in the list
	 pull their list of 'ban lift dates,'
	 start a flag for the user still being banned,
	 check every date in the gamer's list of ban lift dates,
	 if a ban date is found that falls after today
	 the user is still banned stop checking
	 if the gamer is not still banned set the gamer role to Gamer
	 save the updated Gamer info, no return needed for void
	 */
	
	@Scheduled(cron="0 * * * * *")
	public void dailyBanReset() {
		Date current = Date.from(Instant.now());
		gamerRepo.findAllByRole(Gamer.Role.BANNED)
		.collectList().flatMap(gamers -> {
			for(Gamer gg : gamers) {
				Set<Date> banDates = gg.getBanDates();
				boolean stillBanned = false;
				for(Date date : banDates) {
					if(date.after(current)) {
						stillBanned = true;
						break;
					}
				}
				if(!stillBanned) {
					gg.setRole(Gamer.Role.GAMER);
					log.debug("User ban is lifted: "
					+gg.getUsername());
					gamerRepo.save(gg).subscribe();
				}
			}
			return null;
		});
	}

	/*
	 Daily Login Bonus Resetter
	 every day at server midnight collect all gamers to a list and map them; 
	 for every gamer in the list reset their login bonus flag save the change; 
	 no return needed for void
	 */
	
	@Scheduled(cron="0 0 0 * * *")
	public void dailyLoginBonusReset() {
		log.debug("Resetting daily login bonuses");
		gamerRepo.findAll().collectList()
		.flatMap(gamers -> {
			for(Gamer gg : gamers) {
				gg.setLoginBonusCollected(false);
				gamerRepo.save(gg).subscribe();
			}
			return null;
		});
	}

	/*
	 Check Event Start Trigger
	 every 10 seconds check for events that are starting and need initialized
	 grab the current timestamp, get all events, put in a list
	 map the list of events; for each event if it's not listed as ongoing
	 and current time is after its start and current time is before its end
	 mark it as an ongoing event & get the type of event
	 if the type is doublestrings set the global variable stringmod to 2x
	 if the type is rollmod set the global variable rollmod to +5%
	 save the changes to the event; no return needed for void
	 */
	
	@Scheduled(cron="*/10 * * * * *")
	public void checkEventStartTrigger() {
		Date current = Date.from(Instant.now());
		eventRepo.findAll().collectList()
		.flatMap(events -> {
			for(Event event : events) {
				if(!event.isOngoing()
						&& current.after(event.getEventStart())
						&& current.before(event.getEventEnd())) {
					event.setOngoing(true);
					Event.Type type = event.getEventType();
					log.debug("Initializing event...");
					if(type.equals(Event.Type.DOUBLESTRINGS)) {
							Event.setStringMod(2);
					}
					if(type.equals(Event.Type.ROLLMOD)) {
							Event.setRollMod(1.05d);
					}
					log.debug("Event now live!");
					eventRepo.save(event).subscribe();				// save the changes to the event
				}
			}
			return null;											// no return needed for void
		});
	}
	/*
	 Check Event End Trigger
	 every 10 seconds check for events that are ending and need teardown
	 grab the current timestamp, get all events, put in a list
	 map the list of events; for each event if it is listed as ongoing
	 and the current time is after its end mark it as NOT an ongoing event
	 get the type of event if the type is doublestrings set the global variable
	 stringmod to 1x; if the type is rollmod set the global variable rollmod to 1x
	 save the changes to the event; no return needed for void
	 */
	
	@Scheduled(cron="*/10 * * * * *")
	public void checkEventEndTrigger() {
		Date current = Date.from(Instant.now());
		eventRepo.findAll().collectList()
		.flatMap(events -> {
			for(Event event : events) {
				if(event.isOngoing()
						&& current.after(event.getEventEnd())) {
					event.setOngoing(false);
					Event.Type type = event.getEventType();
					if(type.equals(Event.Type.DOUBLESTRINGS)) {
							Event.setStringMod(1);
					}
					if(type.equals(Event.Type.ROLLMOD)) {
							Event.setRollMod(1.0d);
					}
					log.debug("Event has ended.");
					eventRepo.save(event).subscribe();				// save the changes to the event
				}
			}
			return null;											// no return needed for void
		});
	}
}
