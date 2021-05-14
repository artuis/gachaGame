package com.group3.services;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import com.group3.beans.Gamer;
import com.group3.data.GamerRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class GamerServiceTest {
	@TestConfiguration
	static class Configuration {
		@Bean
		GamerServiceImpl getGamerServiceImpl(GamerRepository gamerRepo) {
			GamerServiceImpl gs = new GamerServiceImpl();
			gs.setGamerRepo(gamerRepo);
			return gs;
		}
		
		@Bean
		GamerRepository getGamerRepo() {
			return Mockito.mock(GamerRepository.class);
		}
	}
	
	@Autowired
	private GamerRepository gr;
	@Autowired
	private GamerServiceImpl gsi;
	
	@Test
	void testAddGamerAddsGamer() {
		Gamer gamer = new Gamer();
		gamer.setUsername("test");
		Mockito.when(gr.existsByUsername(gamer.getUsername())).thenReturn(Mono.just(Boolean.FALSE));
		Mockito.when(gr.insert(gamer)).thenReturn(Mono.just(gamer));
		Mono<Gamer> result = gsi.addGamer(gamer);
		StepVerifier.create(result).expectNext(gamer).verifyComplete();
	}
	
	@Test
	void banGamerBansGamer() {
		Gamer gamer = new Gamer();
		gamer.setGamerId(UUID.randomUUID());
		Mockito.when(gr.findById(gamer.getGamerId())).thenReturn(Mono.just(gamer));
		Mockito.when(gr.save(gamer)).thenReturn(Mono.just(gamer));
		Mono<Gamer> result = gsi.banGamer(gamer.getGamerId(), 7);
		StepVerifier.create(result).assertNext(banned -> { 
			Assert.isTrue(banned.getRole().equals(Gamer.Role.BANNED), "Gamer not banned");
		}).verifyComplete();
	}
	
	@Test
	void findByUsernameLogsInUserAndAddsBonuses() {
		Gamer gamer = new Gamer();
		gamer.setUsername("test");
		gamer.setLoginBonusCollected(false);
		gamer.setStrings(0);
		Mockito.when(gr.findByUsername(Mockito.any())).thenReturn(Mono.just(gamer));
		Mockito.when(gr.save(gamer)).thenReturn(Mono.just(gamer));
		Mono<UserDetails> result = gsi.findByUsername(gamer.getUsername());
		StepVerifier.create(result).assertNext(logged -> {
			Gamer loggedGamer = (Gamer) logged;
			Assert.isTrue(loggedGamer.getStrings() == GamerServiceImpl.bonusStrings, "bonus strings not set");
			Assert.isTrue(loggedGamer.isLoginBonusCollected(), "login bonus flag not set to collected");
		}).verifyComplete();
	}
}
