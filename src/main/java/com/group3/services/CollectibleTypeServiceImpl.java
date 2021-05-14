package com.group3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.beans.CollectibleType;
import com.group3.beans.Event;
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

	public void setCollectibleTypeRepo(CollectibleTypeRepository ctr) {
		this.collectibleTypeRepo = ctr;
	}

	@Override
	public Mono<CollectibleType> rollCollectibleType() {
		double rand = ThreadLocalRandom.current().nextDouble() * Event.getRollMod();
		CollectibleType.Stage stage;
		if (rand < CollectibleType.Stage.STAGE_1.getRate()) {
			stage = CollectibleType.Stage.STAGE_1;
		} else {
			stage = rand < CollectibleType.Stage.STAGE_2.getRate() ? CollectibleType.Stage.STAGE_2 : CollectibleType.Stage.STAGE_3;
		}
		return getCollectibleFromStage(stage);
	}

	private Mono<CollectibleType> getCollectibleFromStage(CollectibleType.Stage stage) {
		return collectibleTypeRepo.findCollectiblesByStage(stage).collectList()
				.map(collectibles -> collectibles.get(ThreadLocalRandom.current().nextInt(collectibles.size())));
	}

	@Override
	public Mono<CollectibleType> createCollectibleType(CollectibleType c) {
		return collectibleTypeRepo.insert(c);
	}

	@Override
	public Mono<CollectibleType> updateCollectibleType(CollectibleType c) {
		return collectibleTypeRepo.save(c);
	}

	@Override
	public Flux<CollectibleType> getAllCollectibleTypes() {
		return collectibleTypeRepo.findAll();
	}

	@Override
	public Mono<CollectibleType> getCollectibleType(int typeId) {
		return collectibleTypeRepo.findById(typeId);
	}
}
