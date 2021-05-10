package com.group3.services;

import org.reactivestreams.Publisher;

import com.group3.beans.CollectibleType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CollectibleTypeService {

	Mono<CollectibleType> rollCollectibleType();

	Mono<CollectibleType> createCollectibleType(CollectibleType c);

	Publisher<CollectibleType> updateCollectibleType(CollectibleType c);

	Flux<CollectibleType> getAllCollectibleTypes();

	Mono<CollectibleType> get(int typeId);
	
}
