package com.group3.services;

import java.util.List;

import org.reactivestreams.Publisher;

import com.group3.beans.Collectible;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CollectibleService {

	Mono<Collectible> rollCollectible();

	Mono<Collectible> createCollectible(Collectible c);

	Publisher<Collectible> updateCollectible(Collectible c);

	Flux<Collectible> getAllCollectibles();
	
}
