package com.group3.services;

import java.util.Collections;
import java.util.List;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.beans.CollectibleType;
import com.group3.data.CollectibleRepository;

import io.netty.util.internal.ThreadLocalRandom;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CollectibleServiceImpl implements CollectibleService {
	
	@Autowired
	private CollectibleRepository collectibleRepo;
	
	public CollectibleServiceImpl() {
		super();
	}

	@Override
	public Mono<CollectibleType> rollCollectible() {
		double rand = ThreadLocalRandom.current().nextDouble();
		if (rand < CollectibleType.Stage.STAGE_1.getRate()) {
			return collectibleRepo
					.findCollectiblesByStage(CollectibleType.Stage.STAGE_1)
					.collectList()
					.map(collectibles -> collectibles.get(ThreadLocalRandom.current().nextInt(collectibles.size())));
		} else if (rand < CollectibleType.Stage.STAGE_2.getRate()) {
			return collectibleRepo
					.findCollectiblesByStage(CollectibleType.Stage.STAGE_2)
					.collectList()
					.map(collectibles -> collectibles.get(ThreadLocalRandom.current().nextInt(collectibles.size())));
		} else { /*rand < Collectible.Stage.STAGE_3.getRate())*/
			return collectibleRepo
					.findCollectiblesByStage(CollectibleType.Stage.STAGE_3)
					.collectList()
					.map(collectibles -> collectibles.get(ThreadLocalRandom.current().nextInt(collectibles.size())));
		}
	}
	
	@Override
	public Mono<CollectibleType> createCollectible(CollectibleType c) {
		return collectibleRepo.insert(c);
	}

	@Override
	public Publisher<CollectibleType> updateCollectible(CollectibleType c) {
		return collectibleRepo.save(c);
	}

	@Override
	public Flux<CollectibleType> getAllCollectibles() {
		return collectibleRepo.findAll();
	}
}
