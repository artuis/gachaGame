package com.group3.beans;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import reactor.core.publisher.Mono;

public class RewardToken {
	
	private Mono<?> activeEncounter;
	
	public RewardToken() {
		super();
	}
	
	public void setRunningEncounter(Mono<?> activeEncounter) {
		this.activeEncounter = activeEncounter;
	}
	
	public Object getRewardIfReady() {
		try {
			return activeEncounter.toFuture().get(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		return null;
	}
}
