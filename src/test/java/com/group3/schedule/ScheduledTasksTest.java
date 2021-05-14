package com.group3.schedule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.group3.beans.Collectible;
import com.group3.beans.Event;
import com.group3.beans.Gamer;
import com.group3.beans.RewardToken;
import com.group3.services.CollectibleService;
import com.group3.services.EmailService;
import com.group3.services.EncounterService;
import com.group3.services.EventService;
import com.group3.services.GamerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
class ScheduledTasksTest {
	@TestConfiguration
	static class Configuration {
		@Bean
		public ScheduledTasks getScheduledTasks(GamerService gs, 
				CollectibleService cs, 
				EventService vs, 
				EncounterService es,
				EmailService ms) {
			ScheduledTasks st = new ScheduledTasks();
			st.setGamerService(gs);
			st.setCollectibleService(cs);
			st.setEventService(vs);
			st.setEncounterService(es);
			return st;
		}
		
		@Bean 
		GamerService getGamerServiceST() {
			return Mockito.mock(GamerService.class);
		}
		
		@Bean 
		CollectibleService getCollectibleServiceST() {
			return Mockito.mock(CollectibleService.class);
		}
		
		@Bean 
		EventService getEventServiceST() {
			return Mockito.mock(EventService.class);
		}
		
		@Bean 
		EncounterService getEncounterServiceST() {
			return Mockito.mock(EncounterService.class);
		}
		
		@Bean
		EmailService getEmailServiceST() {
			return Mockito.mock(EmailService.class)
		}

	}
	
	@Autowired
	private ScheduledTasks st;
	@Autowired
	private GamerService gs;
	@Autowired
	private CollectibleService cs;
	@Autowired
	private EventService vs;
	@Autowired
	private EncounterService es;
	
	List<Gamer> gamers = new ArrayList<>();
	List<Gamer.Role> auths = new ArrayList<>();
	Set<Date> banDates = new HashSet<>();
	List<Event> events = new ArrayList<>();
	Gamer gamer1 = new Gamer();
	Gamer gamer2 = new Gamer();
	Event event1 = new Event();
	Event event2 = new Event();
	
	@BeforeEach
	public void init() {

		auths.add(Gamer.Role.GAMER);

		gamer1.setGamerId(UUID.randomUUID());
		gamer1.setUsername("RandyFlatts");
		gamer1.setPassword("hunter2");
		gamer1.setAccountNonExpired(false);
		gamer1.setAccountNonLocked(false);
		gamer1.setAuthorities(auths);
		gamer1.setCollectionSize(0);
		gamer1.setCollectionStrength(0);
		gamer1.setCredentialsNonExpired(false);
		gamer1.setDailyRolls(0);
		gamer1.setEmail("gamer@email.com");
		gamer1.setLoginBonusCollected(true);
		gamer1.setPvpScore(0);
		gamer1.setRegistrationDate(Date.from(Instant.now()));
		gamer1.setRole(Gamer.Role.BANNED);
		gamer1.setRolls(10);
		gamer1.setStardust(10);
		gamer1.setStrings(1000);
		banDates.add(Date.from(Instant.now()));
		gamer1.setBanDates(banDates);
		
		gamer2.setGamerId(gamer1.getGamerId());
		gamer2.setUsername(gamer1.getUsername());
		gamer2.setPassword(gamer1.getPassword());
		gamer2.setAccountNonExpired(false);
		gamer2.setAccountNonLocked(false);
		gamer2.setAuthorities(auths);
		gamer2.setCollectionSize(0);
		gamer2.setCollectionStrength(0);
		gamer2.setCredentialsNonExpired(false);
		gamer2.setDailyRolls(10);
		gamer2.setEmail(gamer1.getEmail());
		gamer2.setLoginBonusCollected(false);
		gamer2.setPvpScore(0);
		gamer2.setRegistrationDate(gamer1.getRegistrationDate());
		gamer2.setRole(Gamer.Role.GAMER);
		gamer2.setBanDates(gamer1.getBanDates());
		gamer2.setRolls(10);
		gamer2.setStardust(10);
		gamer2.setStrings(1000);
		
		gamers.add(gamer2);
		
		event1.setEventId(UUID.randomUUID());
		event1.setOngoing(true);
		event1.setEventType(Event.Type.DOUBLESTRINGS);

		
		event2.setEventId(event1.getEventId());
		event2.setOngoing(false);
		event2.setEventType(Event.Type.DOUBLESTRINGS);

	}
	
	@AfterEach
	void teardown() {
		gamer1 = null;
		gamer2 = null;
		gamers.clear();
		event1 = null;
		event2 = null;
		events.clear();
	}

	@Test
	void dailyRollsResetReturnsResetGamers() {
		Mockito.when(gs.getGamers()).thenReturn(Flux.just(gamer1));
		Mockito.when(gs.updateGamer(gamer1)).thenReturn(Mono.just(gamer2));
		List<Gamer> result = st.dailyRollsReset();
		assertEquals(gamers, result);
	}
	
	@Test
	void checkBanResetReturnsResetGamers() {
		Mockito.when(gs.getGamersByRole(Gamer.Role.BANNED)).thenReturn(Flux.just(gamer1));
		Mockito.when(gs.updateGamer(gamer1)).thenReturn(Mono.just(gamer2));
		List<Gamer> result = st.checkBanReset();
		assertEquals(gamers, result);
	}
	
	@Test
	void dailyLoginBonusResetReturnsResetGamers() {
		Mockito.when(gs.getGamers()).thenReturn(Flux.just(gamer1));
		Mockito.when(gs.updateGamer(gamer1)).thenReturn(Mono.just(gamer2));
		List<Gamer> result = st.dailyLoginBonusReset();
		assertEquals(gamers, result);
	}
	
	@Test
	void checkEventStartTriggerReturnsEvents() {
		event1.setEventStart(Date.from(Instant.now().minus(5l, ChronoUnit.MINUTES)));
		event1.setEventEnd(Date.from(Instant.now().plus(1l, ChronoUnit.DAYS)));
		event2.setEventStart(event1.getEventStart());
		event2.setEventEnd(event1.getEventEnd());
		events.add(event1);
		Mockito.when(vs.getEvents()).thenReturn(Flux.just(event2));
		Mockito.when(vs.updateEvent(event2)).thenReturn(Mono.just(event1));
		List<Event> result = st.checkEventStartTrigger();
		assertEquals(events, result);
	}
	
	@Test
	void checkEventEndTriggerReturnsEvents() {
		event1.setEventStart(Date.from(Instant.now().minus(5l, ChronoUnit.MINUTES)));
		event1.setEventEnd(Date.from(Instant.now().minus(1l, ChronoUnit.MINUTES)));
		event2.setEventStart(event1.getEventStart());
		event2.setEventEnd(event1.getEventEnd());
		events.add(event2);
		Mockito.when(vs.getEvents()).thenReturn(Flux.just(event1));
		Mockito.when(vs.updateEvent(event1)).thenReturn(Mono.just(event2));
		List<Event> result = st.checkEventStartTrigger();
		assertEquals(events, result);
	}
	
	@Test
	void checkOngoingEventsReturnsEvents() {
		event1.setEventStart(Date.from(Instant.now().minus(5l, ChronoUnit.MINUTES)));
		event1.setEventEnd(Date.from(Instant.now().plus(5l, ChronoUnit.MINUTES)));
		event2.setEventStart(event1.getEventStart());
		event2.setEventEnd(event1.getEventEnd());
		events.add(event1);
		Mockito.when(vs.viewOngoingEvents()).thenReturn(Flux.just(event1));
		List<Event> result = st.checkOngoingEvents();
		assertEquals(events, result);
	}
	
	@Test
	void checkEncounterCompletionReturnsCollectibles() {
		List<UUID> collectibles = new ArrayList<>();
		Collectible collectible = new Collectible();
		collectible.setId(UUID.randomUUID());
		collectible.setGamerId(UUID.randomUUID());
		collectible.setOnEncounter(true);
		collectibles.add(collectible.getId());
		
		List<Collectible> collectiblesAfter = new ArrayList<>();
		Collectible collectible2 = new Collectible();
		collectible2.setId(collectible.getId());
		collectible2.setGamerId(collectible.getGamerId());
		collectible2.setOnEncounter(false);
		collectiblesAfter.add(collectible2);
		
		RewardToken before = new RewardToken();
		before.setTokenID(UUID.randomUUID());
		before.setGamerID(collectible.getGamerId());
		before.setActiveEncounter(UUID.randomUUID());
		before.setCollectiblesOnEncounter(collectibles);
		before.setEncounterComplete(false);
		before.setEndTime(Date.from(Instant.now().minus(5l, ChronoUnit.MINUTES)));
		before.setReward(45);
		before.setStartTime(Date.from(Instant.now().minus(7l, ChronoUnit.MINUTES)));
		
		RewardToken after = new RewardToken();
		after.setTokenID(before.getTokenID());
		after.setGamerID(before.getGamerID());
		after.setActiveEncounter(before.getActiveEncounter());
		after.setCollectiblesOnEncounter(collectibles);
		after.setEncounterComplete(true);
		after.setEndTime(before.getEndTime());
		after.setReward(before.getReward());
		after.setStartTime(before.getStartTime());
		
		Mockito.when(es.viewCompletedTokens(false)).thenReturn(Flux.just(before));
		Mockito.when(cs.getCollectible(collectible.getId().toString())).thenReturn(Mono.just(collectible));
		Mockito.when(cs.updateCollectible(collectible)).thenReturn(Mono.just(collectible2));
		List<Collectible> result = st.checkEncounterCompletion();
		assertEquals(collectiblesAfter, result);
	}
	
}
