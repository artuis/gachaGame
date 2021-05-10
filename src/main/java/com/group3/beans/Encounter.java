package com.group3.beans;

public class Encounter {

	int encounterID;
	// Target number the collectibles have to beat to succeed vs encounter
	int difficulty;
	// Amount of 'reward' returned when successful vs encounter
	int reward;
	// Length of time (in milliseconds) the encounter should last
	int length;
	
	public Encounter() {
		super();
	}

	public int getEncounterID() {
		return encounterID;
	}

	public void setEncounterID(int encounterID) {
		this.encounterID = encounterID;
	}

	public int getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	public int getReward() {
		// TODO figure out a better 'reward'
		return reward;
	}
	public void setReward(int reward) {
		this.reward = reward;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
}
