package com.group3.services;

import java.util.List;
import java.util.UUID;

import com.group3.beans.Collectible;
import com.group3.beans.Encounter;
import com.group3.beans.Gamer;
import com.group3.beans.RewardToken;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EncounterService {
	
	Mono<Encounter> createEncounterTemplate(Encounter encounter);
	
	Mono<Encounter> updateEncounterTemplate(Encounter encounter);
	
	Mono<Void> deleteEncounterTemplate(UUID encounter);

	public Flux<RewardToken> getRunningEncounters(UUID gamerID);
	
	public Mono<RewardToken> setEncounter(UUID gamerID, List<UUID> colIDs, UUID encID);

	public int runEncounter(List<Collectible> sent, Encounter journey);

	Flux<RewardToken> viewCompletedTokens(boolean encounterComplete);

	Mono<RewardToken> updateRewardToken(RewardToken token);

	public Mono<Gamer> distributeReward(int reward, UUID gamerID);
	
}
