package com.group3.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import com.group3.beans.Collectible;
import com.group3.beans.Encounter;
import com.group3.beans.Gamer;
import com.group3.beans.RewardToken;
import com.group3.data.CollectibleRepository;
import com.group3.data.EncounterRepository;
import com.group3.data.GamerRepository;
import com.group3.data.RewardTokenRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class EncounterServiceTest {

	@Captor
	private ArgumentCaptor<Gamer> gamerCaptor;

	private static Gamer gamer1;
	private static Encounter journey1;
	private static List<Collectible> inputC;
	private static Encounter inputE;
	private static Collectible mol1, mol2, mol3;
	private static UUID id1, id2;
	
	@TestConfiguration
	static class Configuration {
		@Bean
		EncounterServiceImpl getEncounterServiceImpl(CollectibleService cs, EncounterRepository er, RewardTokenRepository rtr, CollectibleRepository cr, GamerRepository gr, EmailService es) {
			EncounterServiceImpl esi = new EncounterServiceImpl();
			esi.setCollectibleService(cs);
			esi.setEncounterRepo(er);
			esi.setRewardRepo(rtr);
			esi.setCollectibleRepo(cr);
			esi.setGamerRepo(gr);
			esi.setEmailService(es);
			return esi;
		}
		
		@Bean
		CollectibleService getCollectibleService() {
			return Mockito.mock(CollectibleService.class);
		}
		
		@Bean
		EncounterRepository getEncounterRepository() {
			return Mockito.mock(EncounterRepository.class);
		}
		
		@Bean
		RewardTokenRepository getRewardRepository() {
			return Mockito.mock(RewardTokenRepository.class);
		}
		
		@Bean
		CollectibleRepository getCollectibleRepository() {
			return Mockito.mock(CollectibleRepository.class);
		}
		
		@Bean
		GamerRepository getGamerRepository() {
			return Mockito.mock(GamerRepository.class);
		}
		
		@Bean
		EmailService getEmailService() {
			return Mockito.mock(EmailService.class);
		}
	}
	
	@Autowired
	private EncounterServiceImpl esi;
	@Autowired
	private CollectibleService cs;
	@Autowired
	private EncounterRepository er;
	@Autowired
	private RewardTokenRepository rtr;
	@Autowired
	private CollectibleRepository cr;
	@Autowired
	private GamerRepository gr;
	@Autowired
	private EmailService es;

	@BeforeAll
	static void setup() {
		id1 = new UUID(0, 1);
		id2 = new UUID(0, 2);
		gamer1 = new Gamer();
		journey1 = new Encounter();
		inputC = new ArrayList<Collectible>();
		mol1 = new Collectible();
		mol2 = new Collectible();
		mol3 = new Collectible();
		inputC.add(mol1);
		inputC.add(mol2);
		inputC.add(mol3);
		inputE = new Encounter();
	}

	@BeforeEach
	void init() {
		gamer1.setGamerId(id1);
		gamer1.setRolls(0);
		journey1.setEncounterID(id1);
		mol1.setId(id1);
		mol1.setCurrentStat(1);
		mol1.setGamerId(id1);
		mol2.setId(id2);
		mol2.setCurrentStat(2);
		mol2.setGamerId(id1);
		mol3.setId(new UUID(0, 3));
		mol3.setCurrentStat(3);
		mol3.setGamerId(new UUID(0, 4));
		inputE.setDifficulty(5);
		inputE.setReward(100);
		inputE.setLength(1000);
	}

	/*
	 * Test gives two collectibles from the same user the collectibles are sent on
	 * their journey and the reward token is given to the gamer Passes if gamer has
	 * a token
	 */
	@Test
	void testGoodSetEncounter() {
		// Establish Mock behaviors
		RewardToken rt = new RewardToken();
		rt.setTokenID(id1);
		Mockito.when(cs.getCollectible(mol1.getId().toString())).thenReturn(Mono.just(mol1));
		Mockito.when(cs.getCollectible(mol2.getId().toString())).thenReturn(Mono.just(mol2));
		Mockito.when(er.findByEncounterID(journey1.getEncounterID())).thenReturn(Mono.just(journey1));
		Mockito.when(cr.save(mol1)).thenReturn(Mono.just(mol1));
		Mockito.when(cr.save(mol2)).thenReturn(Mono.just(mol2));
		Mockito.when(rtr.insert(Mockito.any(RewardToken.class))).thenReturn(Mono.just(rt));
		List<UUID> sentIDs = new ArrayList<UUID>();
		sentIDs.add(id1);
		sentIDs.add(id2);
		
		Mono<RewardToken> result = esi.setEncounter(id1, sentIDs, id1);
		StepVerifier.create(result).expectNext(rt).verifyComplete();
	}

	/*
	 * Test gives two collectibles from different users
	 * Passes if method returns null
	 */
//	@Test
//	void testBadSetEncounter() {
//		// Establish Mock behaviors
//		doReturn(Mono.just(mol1)).when(collectibleMock).findById(id1);
//		doReturn(Mono.just(mol3)).when(collectibleMock).findById(id2);
//
//		List<UUID> sentIDs = new ArrayList<UUID>();
//		sentIDs.add(id1);
//		sentIDs.add(id2);
//
//		Mono<RewardToken> testResult = esi.setEncounter(id1, sentIDs, id1);
//
//		assertEquals(null, testResult);
//	}
	
	@Test
	void distributeRewardDistributeRewardsBasedOnCompletion() {
		int low = 10;
		int lowMed = 30;
		int med = 50;
		int medHigh = 70;
		int high = 90;
		int megaHigh = 100;
		Mockito.when(gr.findById(id1)).thenReturn(Mono.just(gamer1));
		Mockito.when(gr.save(gamer1)).thenReturn(Mono.just(gamer1));
		Mono<Gamer> result1 = esi.distributeReward(low, gamer1.getGamerId());
		Mono<Gamer> result2 = esi.distributeReward(lowMed, gamer1.getGamerId());
		Mono<Gamer> result3 = esi.distributeReward(med, gamer1.getGamerId());
		Mono<Gamer> result4 = esi.distributeReward(medHigh, gamer1.getGamerId());
		Mono<Gamer> result5 = esi.distributeReward(high, gamer1.getGamerId());
		Mono<Gamer> result6 = esi.distributeReward(megaHigh, gamer1.getGamerId());
		
		StepVerifier.create(result1).expectNext(gamer1).verifyComplete();
		StepVerifier.create(result2).expectNext(gamer1).verifyComplete();
		StepVerifier.create(result3).expectNext(gamer1).verifyComplete();
		StepVerifier.create(result4).expectNext(gamer1).verifyComplete();
		StepVerifier.create(result5).expectNext(gamer1).verifyComplete();
		StepVerifier.create(result6).expectNext(gamer1).verifyComplete();
	}

	@Test
	void testWinRunEncounter() {
		// Inputting strong team
		int result = esi.runEncounter(inputC, inputE);
		// Should return 33 reward
		assertEquals(33, result);
	}

	@Test
	void testLoseRunEncounter() {
		mol2.setCurrentStat(1);
		mol3.setCurrentStat(1);
		// Inputting weak team
		int result = esi.runEncounter(inputC, inputE);
		// Should return 10 reward
		assertEquals(10, result);
	}

}
