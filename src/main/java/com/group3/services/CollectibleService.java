package com.group3.services;

import com.group3.beans.Collectible;

import reactor.core.publisher.Mono;

public interface CollectibleService {

	Mono<Collectible> rollCollectible();
	
}
