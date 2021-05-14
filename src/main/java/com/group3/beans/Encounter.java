package com.group3.beans;

import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.stereotype.Component;
@Primary
@Component
@Table("encounters")
public class Encounter {

	@PrimaryKeyColumn(name = "encounterID", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID encounterID;
	// Level of the encounter
	@PrimaryKeyColumn(name = "level", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private int level;
	// Target number the collectibles have to beat to succeed vs encounter
	@Column
	private int difficulty;
	// Amount of 'reward' returned when successful vs encounter
	@Column
	private int reward;
	// Length of time (in milliseconds) the encounter should last
	@Column
	private long length;
	
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + difficulty;
		result = prime * result + ((encounterID == null) ? 0 : encounterID.hashCode());
		result = prime * result + (int) (length ^ (length >>> 32));
		result = prime * result + level;
		result = prime * result + reward;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Encounter other = (Encounter) obj;
		if (difficulty != other.difficulty)
			return false;
		if (encounterID == null) {
			if (other.encounterID != null)
				return false;
		} else if (!encounterID.equals(other.encounterID))
			return false;
		if (length != other.length)
			return false;
		if (level != other.level)
			return false;
		if (reward != other.reward)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Encounter [encounterID=" + encounterID + ", level=" + level + ", difficulty=" + difficulty + ", reward="
				+ reward + ", length=" + length + "]";
	}

}
