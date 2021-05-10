package com.group3.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.group3.beans.Collectible;
import com.group3.beans.Encounter;
import com.group3.beans.Gamer;
import com.group3.data.CollectibleRepository;
import com.group3.data.EncounterRepository;
import com.group3.data.GamerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class EncounterServiceTest {

	@Mock
	private static CollectibleRepository collectibleMock;
	private static EncounterRepository encounterMock;
	private static GamerRepository gamerMock;

	private EncounterServiceImpl esi = new EncounterServiceImpl();

	private static Gamer gamer1;
	private static Encounter journey1;
	private static List<Collectible> inputC;
	private static Encounter inputE;
	private static Collectible mol1, mol2, mol3;

	@BeforeAll
	static void setup() {
		gamer1  = new Gamer();
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
		gamer1.setGamerId(1);
		journey1.setEncounterID(1);
		mol1.setId(1);
		mol1.setCurrentStat(1);
		mol1.setGamerId(1);
		mol2.setId(2);
		mol2.setCurrentStat(2);
		mol2.setGamerId(1);
		mol3.setId(3);
		mol3.setCurrentStat(3);
		mol3.setGamerId(0);
		inputE.setDifficulty(5);
		inputE.setReward(100);
		inputE.setLength(1000);
	}


	@Test
	void testGoodSetEncounter() {
		// Establish Mock behaviors
		when(collectibleMock.findById(1).thenReturn(Mono.just(mol1)));
		when(collectibleMock.findById(2).thenReturn(Mono.just(mol2)));
		when(encounterMock.findById(1).thenReturn(Mono.just(journey1)));
		when()
	}
	
	@Test
	void testBadSetEncounter() {
		
	}
	
	@Test
	void testWinRunEncounter() {
		// Inputting strong team
		int result = (int) esi.runEncounter(inputC, inputE).block();
		// Should return 33 reward
		assertEquals(result, 33);
	}

	@Test
	void testLoseRunEncounter() {
		mol2.setCurrentStat(1);
		mol3.setCurrentStat(1);
		// Inputting weak team
		int result = (int) esi.runEncounter(inputC, inputE).block();
		// Should return 10 reward
		esi.runEncounter(inputC, inputE)
		.subscribe(r -> assertEquals(r, 33));
		assertEquals(result, 10);
	}

}
