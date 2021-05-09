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
	
	@Scheduled(cron="0 0 0 * * *")						// scheduled for 00:00:00 daily
	public void dailyRollsReset() {
		log.debug("Resetting daily free rolls");
		gamerRepo.findAll().collectList()
		.flatMap(gamers -> {							// get list of all gamers,
			for(Gamer gg : gamers) {					// for each gamer in list
				gg.setDailyRolls(10);					// set daily rolls to 10
				gamerRepo.save(gg).subscribe();						// save updated gamer
			}
			return null;								// no return needed for void
		});
	}
	
	@Scheduled(cron="0 * * * * *")						// checks for ban lifts every minute
	public void dailyBanReset() {
		log.debug("Checking for gamer ban lifts");
		Date current = Date.from(Instant.now());		// pull today's date for reference,
		gamerRepo.findAllByRole(Gamer.Role.BANNED)		// get all gamers that are banned
		.collectList().flatMap(gamers -> {				// collect them to a list, then map the contents;
			for(Gamer gg : gamers) {					// for each gamer in the list
				Set<Date> banDates = gg.getBanDates();	// pull their list of 'ban lift dates,'
				boolean stillBanned = false;			// set a flag for the user still being banned,
				for(Date date : banDates) {				// check every date in the gamer's list of ban lift dates,
					if(date.after(current)) {				// if a ban date is found that falls after today
						stillBanned = true;				// the user is still banned							
						break;							// stop checking
					}
				}
				if(!stillBanned) {						// if the gamer is not still banned
					gg.setRole(Gamer.Role.GAMER);		// set the gamer role to Gamer
					log.debug("User ban is lifted: "
					+gg.getUsername());
					gamerRepo.save(gg).subscribe();		// save the updated Gamer info
				}
			}
			return null;								// no return needed for void
		});
	}
	
	@Scheduled(cron="0 0 0 * * *")						// every day at server midnight
	public void dailyLoginBonusReset() {				
		log.debug("Resetting daily login bonuses");
		gamerRepo.findAll().collectList()				// collect all gamers to a list
		.flatMap(gamers -> {							// map them
			for(Gamer gg : gamers) {					// for every gamer in the list
				gg.setLoginBonusCollected(false);		// reset their login bonus flag
				gamerRepo.save(gg).subscribe();			// save the change
			}
			return null;								// no return needed for void
		});
	}
	
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
					log.debug("Initializing event: "+type.toString());
					switch(type) {
						case DOUBLESTRINGS:
							Event.setSTRINGMOD(2);
							break;
						case ROLLMOD:
							Event.setROLLMOD(1.05f);;
							break;
					}
					log.debug("Event now live! "+type.toString());
					eventRepo.save(event).subscribe();
				}
			}
			return null;
		});
	}
	
	@Scheduled(cron="*/10 * * * * *")
	public void checkEventEndTrigger() {
		log.debug("Checking event repository for active events :) ");
		Date current = Date.from(Instant.now());
		eventRepo.findAll().collectList()
		.flatMap(events -> {
			for(Event event : events) {
				if(event.isOngoing() && current.after(event.getEventEnd())) {
					event.setOngoing(false);
					Event.Type type = event.getEventType();
					switch(type) {
						case DOUBLESTRINGS:
							Event.setSTRINGMOD(1);
							break;
						case ROLLMOD:
							Event.setROLLMOD(1.0f);;
							break;
					}
					log.debug("Event has ended. "+type.toString());
					eventRepo.save(event).subscribe();
				}
			}
			return null;
		});
	}

}
