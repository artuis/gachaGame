package com.group3.beans;

public class Encounter {

	// Target number the collectibles have to beat to succeed vs encounter
	int difficulty;
	// Amount of 'reward' returned when successful vs encounter
	int reward;
	// Length of time (in milliseconds) the encounter should last
	int length;
	
	public Encounter() {
		// TODO Auto-generated constructor stub
	}

	public int getDifficulty() {
		return difficulty;
	}
	public int getReward() {
		// TODO figure out a better 'reward'
		return reward;
	}
	public int getLength() {
		return length;
	}
}
