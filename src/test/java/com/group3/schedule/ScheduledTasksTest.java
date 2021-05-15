package com.group3.schedule;

import static org.mockito.Mockito.times;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.group3.beans.Gamer;
import com.group3.services.CollectibleService;
import com.group3.services.EmailService;
import com.group3.services.EncounterService;
import com.group3.services.EventService;
import com.group3.services.GamerService;

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
	
	List<Gamer> gamers = new ArrayList<>();
	List<Gamer.Role> auths = new ArrayList<>();
	Gamer gamer1 = new Gamer();
	Gamer gamer2 = new Gamer();
	
	@BeforeEach
	void init() {

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
		gamer1.setDailyRolls(9);
		gamer1.setEmail("gamer@email.com");
		gamer1.setLoginBonusCollected(false);
		gamer1.setPvpScore(0);
		gamer1.setRegistrationDate(Date.from(Instant.now()));
		gamer1.setRole(Gamer.Role.GAMER);
		gamer1.setRolls(10);
		gamer1.setStardust(10);
		gamer1.setStrings(1000);
		
		gamer2.setGamerId(UUID.randomUUID());
		gamer2.setUsername("RascalBiggsby");
		gamer2.setPassword("hunter3");
		gamer2.setAccountNonExpired(false);
		gamer2.setAccountNonLocked(false);
		gamer2.setAuthorities(auths);
		gamer2.setCollectionSize(0);
		gamer2.setCollectionStrength(0);
		gamer2.setCredentialsNonExpired(false);
		gamer2.setDailyRolls(9);
		gamer2.setEmail("gamer2@email.com");
		gamer2.setLoginBonusCollected(true);
		gamer2.setPvpScore(0);
		gamer2.setRegistrationDate(Date.from(Instant.now()));
		gamer2.setRole(Gamer.Role.GAMER);
		gamer2.setRolls(10);
		gamer2.setStardust(10);
		gamer2.setStrings(1000);
		
		gamers.add(gamer1);
		gamers.add(gamer2);
	}
	
	@AfterEach
	void teardown() {
		gamer1 = null;
		gamer2 = null;
		gamers.clear();
	}
	
	int numberRuns = 5;
	
	@Test
	void whenDailyRollsResetIsSuccessful() {
		ScheduledTasks scheduledTasks = Mockito.mock(ScheduledTasks.class);
		Mockito.doNothing().when(scheduledTasks).dailyRollsReset();
		scheduledTasks.dailyRollsReset();
		scheduledTasks.dailyRollsReset();
		scheduledTasks.dailyRollsReset();
		scheduledTasks.dailyRollsReset();
		scheduledTasks.dailyRollsReset();
		Mockito.verify(scheduledTasks, times(numberRuns)).dailyRollsReset();
	}
	
	@Test
	void whenCheckBanResetIsSuccessful() {
		ScheduledTasks scheduledTasks = Mockito.mock(ScheduledTasks.class);
		Mockito.doNothing().when(scheduledTasks).checkBanReset();
		scheduledTasks.checkBanReset();
		scheduledTasks.checkBanReset();
		scheduledTasks.checkBanReset();
		scheduledTasks.checkBanReset();
		scheduledTasks.checkBanReset();
		Mockito.verify(scheduledTasks, times(numberRuns)).checkBanReset();
	}
	
	@Test
	void whenDailyLoginBonusResetIsSuccessful() {
		ScheduledTasks scheduledTasks = Mockito.mock(ScheduledTasks.class);
		Mockito.doNothing().when(scheduledTasks).dailyLoginBonusReset();
		scheduledTasks.dailyLoginBonusReset();
		scheduledTasks.dailyLoginBonusReset();
		scheduledTasks.dailyLoginBonusReset();
		scheduledTasks.dailyLoginBonusReset();
		scheduledTasks.dailyLoginBonusReset();
		Mockito.verify(scheduledTasks, times(numberRuns)).dailyLoginBonusReset();
	}
	
	@Test
	void whenCheckEventStartTriggerIsSuccessful() {
		ScheduledTasks scheduledTasks = Mockito.mock(ScheduledTasks.class);
		Mockito.doNothing().when(scheduledTasks).checkEventStartTrigger();
		scheduledTasks.checkEventStartTrigger();
		scheduledTasks.checkEventStartTrigger();
		scheduledTasks.checkEventStartTrigger();
		scheduledTasks.checkEventStartTrigger();
		scheduledTasks.checkEventStartTrigger();
		Mockito.verify(scheduledTasks, times(numberRuns)).checkEventStartTrigger();
	}
	
	@Test
	void whenCheckEventEndTriggerIsSuccessful() {
		ScheduledTasks scheduledTasks = Mockito.mock(ScheduledTasks.class);
		Mockito.doNothing().when(scheduledTasks).checkEventEndTrigger();
		scheduledTasks.checkEventEndTrigger();
		scheduledTasks.checkEventEndTrigger();
		scheduledTasks.checkEventEndTrigger();
		scheduledTasks.checkEventEndTrigger();
		scheduledTasks.checkEventEndTrigger();
		Mockito.verify(scheduledTasks, times(numberRuns)).checkEventEndTrigger();
	}
	
	@Test
	void whenCheckOngoingEventsIsSuccessful() {
		ScheduledTasks scheduledTasks = Mockito.mock(ScheduledTasks.class);
		Mockito.doNothing().when(scheduledTasks).checkOngoingEvents();
		scheduledTasks.checkOngoingEvents();
		scheduledTasks.checkOngoingEvents();
		scheduledTasks.checkOngoingEvents();
		scheduledTasks.checkOngoingEvents();
		scheduledTasks.checkOngoingEvents();
		Mockito.verify(scheduledTasks, times(numberRuns)).checkOngoingEvents();
	}
	
	@Test
	void whenCheckEncounterCompletionIsSuccessful() {
		ScheduledTasks scheduledTasks = Mockito.mock(ScheduledTasks.class);
		Mockito.doNothing().when(scheduledTasks).checkEncounterCompletion();
		scheduledTasks.checkEncounterCompletion();
		scheduledTasks.checkEncounterCompletion();
		scheduledTasks.checkEncounterCompletion();
		scheduledTasks.checkEncounterCompletion();
		scheduledTasks.checkEncounterCompletion();
		Mockito.verify(scheduledTasks, times(numberRuns)).checkEncounterCompletion();
	}
}
