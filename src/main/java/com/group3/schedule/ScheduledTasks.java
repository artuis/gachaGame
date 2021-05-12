package com.group3.schedule;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.group3.beans.Collectible;
import com.group3.beans.Event;
import com.group3.beans.Gamer;
import com.group3.beans.RewardToken;
import com.group3.services.CollectibleService;
import com.group3.services.EventService;
import com.group3.services.GamerService;

@Component
@Order(1)
public class ScheduledTasks implements CommandLineRunner {
	private Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	@Autowired
	private GamerService gamerService;
	@Autowired 
	private EventService eventService;
	@Autowired
	private CollectibleService collectibleService;
	// ScheduledTasks will begin a thread and run after the Driver finishes initialization
	
	@Override
	public void run(String... args) throws Exception {
		// intentionally blank, perhaps log later
	}
	
	// documentation for @Scheduling: https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling
	// documentation for cron format: https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling-cron-expression

	/*
	 Daily Roll Resetter
	 scheduled for 00:00:00 daily: get list of all gamers, for each 
	 gamer in list, set daily rolls to 10, save updated gamer
	 */
	
	@Scheduled(cron="0 0 0 * * *")
	public void dailyRollsReset() {
		log.debug("Resetting daily free rolls");
		List<Gamer> rollsReset = gamerService.getGamers().collectList().block();
		List<Gamer> gamersReset = new ArrayList<Gamer>();
		for(Gamer gamer : rollsReset) {
				gamer.setDailyRolls(10);
				gamerService.updateGamer(gamer).subscribe(gamersReset::add);
		}
		
		/*
		gamerService.getGamers()
		.doOnNext(gg -> gg.setDailyRolls(10))
		.doOnNext(gg -> gamerService.updateGamer(gg))
		.subscribe();
		*/
	}

	/*
	 Check Ban Reseter
	 checks for ban lifts every minute, pull today's date for reference, 
	 get all gamers that are banned, collect them to a list, for each gamer 
	 in the list pull their list of 'ban lift dates,' start a flag for the 
	 user still being banned, check every date in the gamer's list of ban 
	 lift dates, if a ban date is found that falls after today the user is 
	 still banned: stop checking; if the gamer is not still banned, set the 
	 gamer role to Gamer, save the updated Gamer info
	 */
	
	@Scheduled(cron="0 * * * * *")
	public void checkBanReset() {
		Date current = Date.from(Instant.now());
		List<Gamer> bannedGamers = gamerService.getGamersByRole(Gamer.Role.BANNED)
				.collectList().block();
		List<Gamer> unbannedGamers = new ArrayList<Gamer>();
			for(Gamer gamer : bannedGamers) {
				Set<Date> banDates = gamer.getBanDates();
				boolean stillBanned = false;
				for(Date date : banDates) {
					if(date.after(current)) {
						stillBanned = true;
						break;
					}
				}
				if(!stillBanned) {
					gamer.setRole(Gamer.Role.GAMER);
					log.debug("User ban is lifted: {}", gamer);
					gamerService.updateGamer(gamer).subscribe(unbannedGamers::add);
				}
			}
	}

	/*
	 Daily Login Bonus Resetter
	 every day at server midnight collect all gamers to a list; 
	 for every gamer in the list reset their login bonus flag, save the change; 
	 */
	
	@Scheduled(cron="0 0 0 * * *")
	public void dailyLoginBonusReset() {
		log.debug("Resetting daily login bonuses");
		List<Gamer> loginReset = gamerService.getGamers().collectList().block();
		List<Gamer> resetGamers = new ArrayList<Gamer>();
		for(Gamer gamer : loginReset) {
			gamer.setLoginBonusCollected(false);
			gamerService.updateGamer(gamer).subscribe(resetGamers::add);
		}
	}

	/*
	 Check Event Start Trigger
	 every 10 seconds check for events that are starting and need initialized
	 grab the current timestamp, get all events, put in a list
	 for each event if it's not listed as ongoing, and current time is after 
	 its start, and current time is before its end: mark it as an ongoing 
	 event & get the type of event; if the type is doublestrings set the global 
	 variable stringmod to 2x; if the type is rollmod set the global variable 
	 rollmod to +5%; save the changes to the event
	 */
	

	@Scheduled(cron="*/10 * * * * *")
	public void checkEventStartTrigger() {
		Date current = Date.from(Instant.now());
		List<Event> updatedEvents = new ArrayList<Event>();
		List<Event> eventList = eventService.getEvents()
				.collectList().block();
		for(Event event : eventList) {
			if(event.isOngoing() == false 
					&& current.after(event.getEventStart())
					&& current.before(event.getEventEnd())) {
				event.setOngoing(true);
				Event.Type type = event.getEventType();
				log.debug("Initializing event: {}", type);
				if(type.equals(Event.Type.DOUBLESTRINGS)) {
					Event.setStringMod(2);
				}
				if(type.equals(Event.Type.ROLLMOD)) {
					Event.setRollMod(1.05d);
				}
				log.debug("Event now live!");
				eventService.updateEvent(event)
				.subscribe(updatedEvents::add);
			}

		}
	}
	/*
	 Check Event End Trigger
	 every 10 seconds check for events that are ending and need teardown,
	 grab the current timestamp, get all events, put in a list, for each 
	 event: if it is listed as ongoing, and the current time is after its
	 end, mark it as NOT an ongoing event, get the type of event, if the 
	 type is doublestrings set the global variable stringmod to 1x; if the 
	 type is rollmod set the global variable rollmod to 1x save the changes
	 */
	
	@Scheduled(cron="*/10 * * * * *")
	public void checkEventEndTrigger() {
		Date current = Date.from(Instant.now());
		List<Event> updatedEvents = new ArrayList<Event>();
		List<Event> eventList = eventService.getEvents()
				.collectList().block();
		for(Event event : eventList) {
			if(event.isOngoing() == true 
					&& current.after(event.getEventEnd())) {
				event.setOngoing(false);
				Event.Type type = event.getEventType();
				log.debug("Event teardown: {}", type);
				if(type.equals(Event.Type.DOUBLESTRINGS)) {
					Event.setStringMod(1);
				}
				if(type.equals(Event.Type.ROLLMOD)) {
					Event.setRollMod(1.0d);
				}
				log.debug("Event has ended. Thanks for playing!");
				eventService.updateEvent(event)
				.subscribe(updatedEvents::add);
			}
		}
	}
	
	/*
	 Check Ongoing Events
	 in case of server going down mid event, this method
	 checks the database for any ongoing events and 
	 updates any associated global modifiers
	 */
	
	@Scheduled(initialDelay=1000, fixedDelay=3600000)
	public void checkOngoingEvents() {
		List<Event> ongoingEvents = eventService.viewOngoingEvents()
				.collectList().block();
		for(Event event : ongoingEvents) {
			log.debug("Event currently live: {}", event);
			Event.Type type = event.getEventType();
			if(type.equals(Event.Type.DOUBLESTRINGS)) {
				Event.setStringMod(2);
				log.debug("Current stringMod: {}", Event.getStringMod());
			}
			if(type.equals(Event.Type.ROLLMOD)) {
				Event.setRollMod(1.05d);
				log.debug("Current rollMod: {}", Event.getRollMod());
			}
		}
	}
	
	@Scheduled(cron="0 0 * * * *")
	public void checkEncounterCompletion() {
		Date current = Date.from(Instant.now());
		List<RewardToken> encounterTokens = encounterService.viewOngoingEncounters()
				.collectList().block();
		List<Collectible> releasedCollectibles = new ArrayList<>();
		for(RewardToken token : encounterTokens) {
			log.debug("Checking current encounters for completion...");
			if(current.after(token.getEndTime())) {
				token.setEncounterComplete(true);
				for(UUID collectibleId : token.getCollectiblesOnEncounter()) {
					collectibleService.getCollectible(collectibleId.toString()).map(collectible -> 
					collectible.setOnEncounter(false))
				}
			}
			
		}
	}
}
