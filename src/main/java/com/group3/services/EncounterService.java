package com.group3.services;

import java.util.List;
import java.util.concurrent.Future;

import com.group3.beans.Collectible;
import com.group3.beans.Encounter;
import com.group3.beans.RewardToken;

import reactor.core.publisher.Mono;

public interface EncounterService {

	public RewardToken setEncounter(List<Integer> colIDs, Integer encID);
	
	public Mono<?> runEncounter(List<Collectible> sent, Encounter journey);
	
	
}
