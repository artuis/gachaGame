package com.group3.services;

import com.group3.beans.RewardToken;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RewardTokenService {

	Flux<RewardToken> viewCompletedTokens(boolean encounterComplete);

	Mono<RewardToken> updateRewardToken(RewardToken token);

}
