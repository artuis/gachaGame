package com.group3.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.group3.beans.Collectible;
import com.group3.beans.Encounter;
import com.group3.beans.Gamer;
import com.group3.beans.RewardToken;
import com.group3.data.CollectibleRepository;
import com.group3.data.EncounterRepository;
import com.group3.data.GamerRepository;
import com.group3.services.EncounterServiceImpl;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class EncounterServiceTest {

	@Mock
	private static CollectibleRepository collectibleMock;
	@Mock
	private static EncounterRepository encounterMock;
	@Mock
	private static GamerRepository gamerMock;

	private static EncounterServiceImpl esi;

	@Captor
	private ArgumentCaptor<Gamer> gamerCaptor;

	private static Gamer gamer1;
	private static Encounter journey1;
	private static List<Collectible> inputC;
	private static Encounter inputE;
	private static Collectible mol1, mol2, mol3;
	private static UUID id1, id2;

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
		esi = new EncounterServiceImpl(collectibleMock, encounterMock, gamerMock);
		gamer1.setGamerId(id1);
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
//	@Test
//	void testGoodSetEncounter() {
//		// Establish Mock behaviors
//		doReturn(Mono.just(mol1)).when(collectibleMock).findById(id1);
//		doReturn(Mono.just(mol2)).when(collectibleMock).findById(id2);
//		doReturn(Mono.just(journey1)).when(encounterMock).findById(id1);
//		doReturn(Mono.just(gamer1)).when(gamerMock).findById(id1);
//
//		List<UUID> sentIDs = new ArrayList<UUID>();
//		sentIDs.add(id1);
//		sentIDs.add(id2);
//
//		esi.setEncounter(id1, sentIDs, id1);
//
//		verify(gamerMock).save(argThat(gamer -> gamer.getActiveEncounters().size() > 0));
//	}

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
