package com.group3.services;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.beans.Collectible;
import com.group3.beans.CollectibleType;
import com.group3.data.CollectibleTypeRepository;

import io.netty.util.internal.ThreadLocalRandom;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CollectibleTypeServiceImpl implements CollectibleTypeService {
	
	@Autowired
	private CollectibleTypeRepository collectibleTypeRepo;
	
	public CollectibleTypeServiceImpl() {
		super();
	}

	@Override
	public Mono<CollectibleType> rollCollectibleType() {
		double rand = ThreadLocalRandom.current().nextDouble();
		if (rand < CollectibleType.Stage.STAGE_1.getRate()) {
			return collectibleTypeRepo
					.findCollectiblesByStage(CollectibleType.Stage.STAGE_1)
					.collectList()
					.map(collectibles -> collectibles.get(ThreadLocalRandom.current().nextInt(collectibles.size())));
		} else if (rand < CollectibleType.Stage.STAGE_2.getRate()) {
			return collectibleTypeRepo
					.findCollectiblesByStage(CollectibleType.Stage.STAGE_2)
					.collectList()
					.map(collectibles -> collectibles.get(ThreadLocalRandom.current().nextInt(collectibles.size())));
		} else { /*rand < Collectible.Stage.STAGE_3.getRate())*/
			return collectibleTypeRepo
					.findCollectiblesByStage(CollectibleType.Stage.STAGE_3)
					.collectList()
					.map(collectibles -> collectibles.get(ThreadLocalRandom.current().nextInt(collectibles.size())));
		}
	}
	
	@Override
	public Mono<CollectibleType> createCollectibleType(CollectibleType c) {
		return collectibleTypeRepo.insert(c);
	}

	@Override
	public Publisher<CollectibleType> updateCollectibleType(CollectibleType c) {
		return collectibleTypeRepo.save(c);
	}

	@Override
	public Flux<CollectibleType> getAllCollectibleTypes() {
		return collectibleTypeRepo.findAll();
	}
}
