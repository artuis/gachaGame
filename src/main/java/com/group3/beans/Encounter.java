package com.group3.beans;

import java.util.UUID;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.stereotype.Component;

@Component
@Table("encounters")
public class Encounter {

	@PrimaryKeyColumn(name = "encounterID", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	UUID encounterID;
	// Level of the encounter
	@PrimaryKeyColumn(name = "level", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	int level;
	// Target number the collectibles have to beat to succeed vs encounter
	@Column
	int difficulty;
	// Amount of 'reward' returned when successful vs encounter
	@Column
	int reward;
	// Length of time (in milliseconds) the encounter should last
	@Column
	long length;
	
	public Encounter() {
		super();
	}

	public UUID getEncounterID() {
		return encounterID;
	}

	public void setEncounterID(UUID encounterID) {
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
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
}
