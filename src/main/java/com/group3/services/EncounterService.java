package com.group3.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

import com.group3.beans.Collectible;
import com.group3.beans.Encounter;
import com.group3.beans.RewardToken;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EncounterService {

	public Flux<RewardToken> getRunningEncounters(UUID gamerID);
	
	public Mono<RewardToken> setEncounter(UUID i, List<UUID> colIDs, UUID encounterID);

	public int runEncounter(List<Collectible> sent, Encounter journey);

	
	
}
