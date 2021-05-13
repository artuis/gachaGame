package com.group3.beans;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.stereotype.Component;

@Component
@Table("rewardtokens")
public class RewardToken {

	@PrimaryKeyColumn(name = "tokenID", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private UUID tokenID;
	@PrimaryKeyColumn(name = "gamerID", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private UUID gamerID;
	@Column
	private UUID activeEncounter;
	@Column
	private int reward;
	@Column
	private Date startTime;
	@Column
	private Date endTime;
	@Column
	private List<UUID> collectiblesOnEncounter;
	@Column
	private boolean encounterComplete = false;

	public RewardToken() {
		super();
	}

	public UUID getTokenID() {
		return tokenID;
	}

	public void setTokenID(UUID tokenID) {
		this.tokenID = tokenID;
	}

	public UUID getGamerID() {
		return gamerID;
	}

	public void setGamerID(UUID gamerID) {
		this.gamerID = gamerID;
	}

	public UUID getActiveEncounter() {
		return activeEncounter;
	}

	public void setActiveEncounter(UUID activeEncounter) {
		this.activeEncounter = activeEncounter;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEncounterTimes(long length) {
		this.startTime = Date.from(Instant.now());
		this.endTime = Date.from(Instant.now().plus(length, ChronoUnit.MINUTES));
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public List<UUID> getCollectiblesOnEncounter() {
		return collectiblesOnEncounter;
	}

	public void setCollectiblesOnEncounter(List<UUID> collectiblesOnEncounter) {
		this.collectiblesOnEncounter = collectiblesOnEncounter;
	}

	public boolean isEncounterComplete() {
		return encounterComplete;
	}

	public void setEncounterComplete(boolean encounterComplete) {
		this.encounterComplete = encounterComplete;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activeEncounter == null) ? 0 : activeEncounter.hashCode());
		result = prime * result + ((collectiblesOnEncounter == null) ? 0 : collectiblesOnEncounter.hashCode());
		result = prime * result + (encounterComplete ? 1231 : 1237);
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((gamerID == null) ? 0 : gamerID.hashCode());
		result = prime * result + reward;
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((tokenID == null) ? 0 : tokenID.hashCode());
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
		RewardToken other = (RewardToken) obj;
		if (activeEncounter == null) {
			if (other.activeEncounter != null)
				return false;
		} else if (!activeEncounter.equals(other.activeEncounter))
			return false;
		if (collectiblesOnEncounter == null) {
			if (other.collectiblesOnEncounter != null)
				return false;
		} else if (!collectiblesOnEncounter.equals(other.collectiblesOnEncounter))
			return false;
		if (encounterComplete != other.encounterComplete)
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (gamerID == null) {
			if (other.gamerID != null)
				return false;
		} else if (!gamerID.equals(other.gamerID))
			return false;
		if (reward != other.reward)
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (tokenID == null) {
			if (other.tokenID != null)
				return false;
		} else if (!tokenID.equals(other.tokenID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RewardToken [tokenID=" + tokenID + ", gamerID=" + gamerID + ", activeEncounter=" + activeEncounter
				+ ", reward=" + reward + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", collectiblesOnEncounter=" + collectiblesOnEncounter + ", encounterComplete=" + encounterComplete
				+ "]";
	}

}
