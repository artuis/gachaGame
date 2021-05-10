package com.group3.services;

import java.util.List;
import java.util.concurrent.Future;

import com.group3.beans.Collectible;
import com.group3.beans.Encounter;
import com.group3.beans.RewardToken;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EncounterService {

	public Flux<RewardToken> getRunningEncounters(int gamerID);
	
	public Mono<RewardToken> setEncounter(int i, List<Integer> colIDs, Integer encounterID);

	public Mono<?> runEncounter(List<Collectible> sent, Encounter journey);

	
	
}
