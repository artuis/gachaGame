package com.group3.beans;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import reactor.core.publisher.Mono;

@UserDefinedType
public class RewardToken {
	
	//@Column
	//private Mono<?> activeEncounter;
	@Column
	UUID activeEncounter;
	
	int reward;
	Date startTime;
	Date endTime;
	List<UUID> collectiblesOnEncounter;
	boolean encounterComplete = false;
	

	
	public RewardToken() {
		super();
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
	
//	public Object getRewardIfReady() {
//		try {
//			// TODO check scheduler to see if event is complete
//			// return activeEncounter.toFuture().get(1, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		} catch (TimeoutException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
}
