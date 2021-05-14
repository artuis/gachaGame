package com.group3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.beans.CollectibleType;
import com.group3.beans.CollectibleType.Stage;
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
		Stage stage;
		if (rand < Stage.STAGE_1.getRate()) {
			stage = Stage.STAGE_1;
		} else {
			stage = rand < Stage.STAGE_2.getRate() ? Stage.STAGE_2 : Stage.STAGE_3;
		}
		return getCollectibleFromStage(stage);
	}

	private Mono<CollectibleType> getCollectibleFromStage(Stage stage) {
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
