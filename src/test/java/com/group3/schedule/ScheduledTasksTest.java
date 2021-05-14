package com.group3.schedule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
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

import com.group3.beans.Event;
import com.group3.beans.Gamer;
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
			st.setEmailService(ms);
			return st;
		}
		
		@Bean
		public GamerService getGamerService() {
			return Mockito.mock(GamerService.class);
		}
		
		@Bean
		public CollectibleService getCollectibleService() {
			return Mockito.mock(CollectibleService.class);
		}
		
		@Bean
		public EventService getEventService() {
			return Mockito.mock(EventService.class);
		}
		
		@Bean
		public EncounterService getEncounterService() {
			return Mockito.mock(EncounterService.class);
		}
		
		@Bean
		public EmailService getEmailService() {
			return Mockito.mock(EmailService.class);
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
	@Autowired
	private EmailService ms;
	
	List<Gamer> gamers = new ArrayList<>();
	List<Gamer.Role> auths = new ArrayList<>();
	Set<Date> banDates = new HashSet<>();
	Gamer gamer1 = new Gamer();
	Gamer gamer2 = new Gamer();
	
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
	}
	
	@AfterEach
	void teardown() {
		gamer1 = null;
		gamer2 = null;
		gamers.clear();
	}

	@Test
	void dailyRollsResetReturnsReset() {
		Mockito.when(gs.getGamers()).thenReturn(Flux.just(gamer1));
		Mockito.when(gs.updateGamer(gamer1)).thenReturn(Mono.just(gamer2));
		List<Gamer> result = st.dailyRollsReset();
		assertEquals(gamers, result);
	}
	
	@Test
	void checkBanResetReturnsReset() {
		Mockito.when(gs.getGamersByRole(Gamer.Role.BANNED)).thenReturn(Flux.just(gamer1));
		Mockito.when(gs.updateGamer(gamer1)).thenReturn(Mono.just(gamer2));
		List<Gamer> result = st.checkBanReset();
		assertEquals(gamers, result);
	}
	
	@Test
	void dailyLoginBonusResetReturnsReset() {
		Mockito.when(gs.getGamers()).thenReturn(Flux.just(gamer1));
		Mockito.when(gs.updateGamer(gamer1)).thenReturn(Mono.just(gamer2));
		List<Gamer> result = st.dailyLoginBonusReset();
		assertEquals(gamers, result);
	}
	
	@Test
	void checkEventStartTriggerReturnsEvent() {
		Event event1 = new Event();
		Mockito.when(vs.getEvents()).thenReturn(Flux.just(event1));
	}
	
}
