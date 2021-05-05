package com.group3.services;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.group3.beans.Collectible;
import com.group3.beans.Encounter;

@Service
public class EncounterServiceImpl implements EncounterService {

	public EncounterServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	@Async
	public static Future<?> runEncounter(List<Collectible> sent, Encounter journey) {
		// The deterministic version of the encounter runner.
		// A random version can be made later. (this is simpler to test)
		
		// Gather stat total of sent collectible list
		// TODO: Create a better way of calculating 'success'
		int total = sent.stream()
						.mapToInt(x -> x.getStatSum())
						.sum();
		int reward;

		// If the combined statistical score of each, reaches the set
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
		
		// Create and return Future
		Future<Integer> futureReward = () -> {
			Thread.sleep(journey.getLength());
			return reward;
		};
		
		return futureReward;
	}
}
