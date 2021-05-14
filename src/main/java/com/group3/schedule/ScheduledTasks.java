package com.group3.schedule;

import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.group3.beans.Event;
import com.group3.beans.Gamer;
import com.group3.services.CollectibleService;
import com.group3.services.EmailService;
import com.group3.services.EncounterService;
import com.group3.services.EventService;
import com.group3.services.GamerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Order(1)
public class ScheduledTasks implements CommandLineRunner {
	private Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private GamerService gamerService;
	private EventService eventService;
	private CollectibleService collectibleService;
	private EncounterService encounterService;
	private EmailService emailService;
	
	@Autowired
	public void setGamerService(GamerService gs) {
		this.gamerService = gs;
	}
	
	@Autowired
	public void setEventService(EventService vs) {
		this.eventService = vs;
	}

	@Autowired
	public void setCollectibleService(CollectibleService gs) {
		this.collectibleService = gs;
	}
	
	@Autowired
	public void setEncounterService(EncounterService es) {
		this.encounterService = es;
	}
	
	@Autowired
	public void setEmailService(EmailService es) {
		this.emailService = es;
	}
	
	public ScheduledTasks() {
		super();
	}
	
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
		gamerService.getGamers().flatMap(gamer -> {
			gamer.setDailyRolls(10);
			return gamerService.updateGamer(gamer);
		}).collectList().block();
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
		gamerService.getGamersByRole(Gamer.Role.BANNED).flatMap(gamer -> {
			boolean stillBanned = false;
			for(Date banDate : gamer.getBanDates()) {
				if(banDate.after(current)) {
					stillBanned = true;
					break;
				}
				if(!stillBanned) {
					gamer.setRole(Gamer.Role.GAMER);
					log.debug("User ban is lifted: {}", gamer);
					emailService.sendEmail(
							gamer.getEmail(), 
							"Ban Lifted", 
							"Your ban in GachaGame has lifted. "
							+ "Please be more mindful of the community in the future.");
				}
			}
			return gamerService.updateGamer(gamer);
		}).collectList().block();
	}

	/*
	 Daily Login Bonus Resetter
	 every day at server midnight collect all gamers to a list; 
	 for every gamer in the list reset their login bonus flag, save the change; 
	 */
	
	@Scheduled(cron="0 0 0 * * *")
	public void dailyLoginBonusReset() {
		log.debug("Resetting daily login bonuses");
		gamerService.getGamers().flatMap(gamer -> {
			gamer.setLoginBonusCollected(false);
			return gamerService.updateGamer(gamer);
		}).collectList().block();
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
		eventService.getEvents().flatMap(event -> {
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
			}
			return eventService.updateEvent(event);
		}).collectList().block();
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
		eventService.viewOngoingEvents().flatMap(event -> {
			if(current.after(event.getEventEnd())) {
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
			}
			return eventService.updateEvent(event);
		}).collectList().block();
	}
	
	/*
	 Check Ongoing Events
	 in case of server going down mid event, this method
	 checks the database for any ongoing events and 
	 updates any associated global modifiers
	 */
	
	@Scheduled(initialDelay=1000, fixedDelay=3600000)
	public void checkOngoingEvents() {
		eventService.viewOngoingEvents().flatMap(event -> {
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
			return Mono.just(event);
		}).collectList().block();
	}
	
	/*
	 Check Encounter Completion
	 checks the reward token repository for  tokens that are marked 
	 as not completed, if the current time is after the end time: the
	 encounter token is marked complete, the list of collectibles on 
	 the encounter is retrieved and marked as not being on an encounter,
	 the collectible changes are saved, the reward is distributed to 
	 the gamer (which is saved in the distribution method), and the 
	 updated token is finally saved.
	 */
	
	@Scheduled(cron="*/10 * * * * *")
	public void checkEncounterCompletion() {
		Date current = Date.from(Instant.now());
		encounterService.viewCompletedTokens(false)
				.filter(token -> current.after(token.getEndTime()))
				.flatMap(token -> {
					token.setEncounterComplete(true);
					return Flux.fromIterable(token.getCollectiblesOnEncounter())
							.flatMap(collectibleId -> {
								return collectibleService.getCollectible(collectibleId.toString())
										.flatMap(collectible -> {
											if(collectible != null) {
												collectible.setOnEncounter(false);
												return collectibleService.updateCollectible(collectible);
											} else {return Mono.empty();}
						});
					});
				}).collectList().block();
	}			
}
