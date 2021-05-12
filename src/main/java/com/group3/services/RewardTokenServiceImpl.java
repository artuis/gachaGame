package com.group3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.beans.RewardToken;
import com.group3.data.EncounterRepository;
import com.group3.data.RewardTokenRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RewardTokenServiceImpl implements RewardTokenService {
	@Autowired
	private EncounterRepository encounterRepo;
	@Autowired
	private RewardTokenRepository rewardRepo;
	
	@Override
	public Flux<RewardToken> viewCompletedTokens(boolean encounterComplete) {
		return rewardRepo.findAllByEncounterComplete(false);
	}
	
	@Override
	public Mono<RewardToken> updateRewardToken(RewardToken token) {
		return rewardRepo.save(token);
	}

}
