package com.group3.services;

import java.util.ArrayList;
import java.util.List;
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
import com.group3.data.CollectibleRepository;
import com.group3.data.EncounterRepository;

import reactor.core.publisher.Mono;

@Service
public class EncounterServiceImpl implements EncounterService {

	@Autowired
	private CollectibleRepository collectibleRepo;
	@Autowired
	private EncounterRepository encounterRepo;
	
	public EncounterServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	public RewardToken setEncounter(List<Integer> colIDs, Integer encID) {
		
		// TODO get collectible list from gamer
		List<Collectible> sent = new ArrayList<Collectible>();
		//colIDs.forEach(x -> sent.add(collectibleRepo.));
		
		// TODO get encounter selection
		Encounter journey = new Encounter();
		//journey = encounterRepo.getEncounter(encID);
		
		// TODO create reward token that contains the Mono
		// of the Encounter the collectibles are sent on
		RewardToken reward = new RewardToken();
		reward.setRunningEncounter(runEncounter(sent, journey));
		
		return reward;
		
	}
	
	public Mono<?> runEncounter(List<Collectible> sent, Encounter journey) {
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
		
		// Pause for relevant length of time
		try {
			Thread.sleep(journey.getLength());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Return Future
		return Mono.just(reward);
		
	}
}