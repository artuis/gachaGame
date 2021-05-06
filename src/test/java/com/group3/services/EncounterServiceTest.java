package com.group3.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.group3.beans.Collectible;
import com.group3.beans.Encounter;

class EncounterServiceTest {

	// @Mock

	private EncounterServiceImpl esi = new EncounterServiceImpl();

	private static List<Collectible> inputC;
	private static Encounter inputE;
	private static Collectible mol1, mol2, mol3;

	@BeforeAll
	static void setup() {
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
		mol1.setCurrentStat(1);
		mol2.setCurrentStat(2);
		mol3.setCurrentStat(3);
		inputE.setDifficulty(5);
		inputE.setReward(100);
		inputE.setLength(1000);
	}

	@Test
	@Disabled("Not yet implemented")
	void testSetEncounter() {
		fail("Not yet implemented");
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
