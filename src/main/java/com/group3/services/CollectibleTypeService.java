package com.group3.services;

import com.group3.beans.CollectibleType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CollectibleTypeService {

	Mono<CollectibleType> rollCollectibleType();

	Mono<CollectibleType> createCollectibleType(CollectibleType c);

	Mono<CollectibleType> updateCollectibleType(CollectibleType c);

	Flux<CollectibleType> getAllCollectibleTypes();

	Mono<CollectibleType> getCollectibleType(int typeId);
	
}
