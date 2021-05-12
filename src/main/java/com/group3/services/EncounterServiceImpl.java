package com.group3.services;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.group3.beans.Collectible;
import com.group3.beans.Encounter;
import com.group3.beans.RewardToken;
import com.group3.beans.Gamer;
import com.group3.data.CollectibleRepository;
import com.group3.data.EncounterRepository;
import com.group3.data.GamerRepository;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EncounterServiceImpl implements EncounterService {

	@Autowired
	private CollectibleRepository collectibleRepo;
	@Autowired
	private EncounterRepository encounterRepo;
	@Autowired
	private GamerRepository gamerRepo;
	
	public EncounterServiceImpl() {
		super();
	}
	
	public Flux<RewardToken> getRunningEncounters(UUID gamerID){
		Gamer g = (Gamer) gamerRepo.findById(gamerID).subscribe();
		return Flux.fromIterable(g.getActiveEncounters());
	}
	
	public Mono<RewardToken> setEncounter(UUID gamerID, List<UUID> colIDs, UUID encID) {
		
		// Get the collectibles to send on the journey
		List<Collectible> sent = new ArrayList<Collectible>();
		colIDs.forEach(x -> 
		collectibleRepo.findById(x)
		.doOnNext(y -> sent.add(y)).subscribe());
		
		// If any of the collectibles sent
		// are on an encounter
		// or owned by a different gamer
		if(sent.stream().anyMatch(x->x.isOnEncounter()) ||
				sent.stream().anyMatch(x->x.getGamerId()!=gamerID)) {
			return null;
		}
		
		RewardToken rewardToken = new RewardToken();
		rewardToken.setActiveEncounter(encID);
		rewardToken.setCollectiblesOnEncounter(colIDs);
		// Get the journey to send on the collectibles
		encounterRepo.findByEncounterID(encID)
		.doOnNext( e -> rewardToken.setReward(runEncounter(sent, e)))
		.doOnNext( e -> rewardToken.setEncounterTimes(e.getLength()))
		.subscribe();

		// Set the collectibles as unavailable to go on further encounters
		sent.forEach(c -> c.setOnEncounter(true));
		
		// Give the token to gamer
		gamerRepo.findById(gamerID)
		.doOnNext(gg -> gg.addActiveEncounter(rewardToken))
		.doOnNext(gg -> gamerRepo.save(gg))
		.subscribe();

		// TODO give rewardToken to scheduler to send alert to gamer when ready
		
		
		return Mono.just(rewardToken);
		
	}
	
	public int runEncounter(List<Collectible> sent, Encounter journey) {
		// The deterministic version of the encounter runner.
		// A random version can be made later. (this is simpler to test)
		
		// Gather stat total of sent collectible list
		// TODO: Create a better way of calculating 'success'
		int total = sent.stream()
						.mapToInt(x -> x.getCurrentStat())
						.sum();
		int reward;

		// If the combined statistical score of each collectible, reaches the set
		// Encounter difficulty, it wins and returns with a reward.
		if(total >= journey.getDifficulty()) {
			// Reward is defined by the encounter and limited by the number
			// of collectibles you send on the journey
			reward = journey.getReward() / sent.size();
		}
		else {
			// Return a small amount upon failure
			reward = 10;
		}
		
		// Return Mono
		// return Mono.just(reward).delayElement(Duration.ofMillis(journey.getLength()));
		// Return just int reward
		return reward;
	}
	
	public Flux<?> getEncounterReward(UUID gamerID) {
		
		Gamer gg = (Gamer) gamerRepo.findById(gamerID).subscribe();
		
		List<RewardToken> tokens = gg.getActiveEncounters();
		
		// Check if there are even any tokens
		if(tokens.isEmpty()) {
			// TODO return response
			return null;
		} else {
			// TODO check if encounters are done
			// and get rewards from the ones that are
			//tokens.stream().;
			
			
		}
		
		return null;
	}
	
	// Constructor for testing
	public EncounterServiceImpl(CollectibleRepository cMock, EncounterRepository eMock, GamerRepository gMock) {
		this.collectibleRepo = cMock;
		this.encounterRepo = eMock;
		this.gamerRepo = gMock;
	}

	
}