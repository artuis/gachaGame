package com.group3.services;

import java.util.List;

import org.reactivestreams.Publisher;

import com.group3.beans.CollectibleType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CollectibleService {

	Mono<CollectibleType> rollCollectible();

	Mono<CollectibleType> createCollectible(CollectibleType c);

	Publisher<CollectibleType> updateCollectible(CollectibleType c);

	Flux<CollectibleType> getAllCollectibles();
	
}
