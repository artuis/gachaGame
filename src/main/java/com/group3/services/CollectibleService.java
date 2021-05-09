package com.group3.services;

import java.util.UUID;

import org.reactivestreams.Publisher;

import com.group3.beans.Collectible;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CollectibleService {

	Flux<Collectible> getCollectibles(String filter);

	Flux<Collectible> getAllCollectibles();

	Publisher<Collectible> updateCollectible(Collectible c);

	Mono<Collectible> createCollectible(Collectible c);
	
	Mono<Collectible> upgradeCollectible(UUID collectibleId);

}
