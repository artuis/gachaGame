package com.group3.services;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.group3.beans.Collectible;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CollectibleService {

	Flux<Collectible> getCollectibles(String filter);

	Flux<Collectible> getAllCollectibles();

	Mono<Collectible> updateCollectible(Collectible c);

	Mono<Collectible> createCollectible(Collectible c);

	Mono<ResponseEntity<?>> upgradeCollectible(UUID collectibleId);

	Mono<Collectible> getCollectible(String id);

	Mono<ResponseEntity<?>> removeCollectible(UUID collectibleId, UUID gamerId);

	Mono<Collectible> collectibleFusion(List<UUID> collectibleIds);

}
