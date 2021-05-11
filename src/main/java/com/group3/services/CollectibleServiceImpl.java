package com.group3.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.group3.beans.Collectible;
import com.group3.data.CollectibleRepository;
import com.group3.data.CollectibleTypeRepository;
import com.group3.data.GamerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CollectibleServiceImpl implements CollectibleService {

	private Logger log = LoggerFactory.getLogger(CollectibleServiceImpl.class);
	
	@Autowired
	private Collectible emptyCollectible;

	@Autowired
	private CollectibleRepository repo;
	
	@Autowired
	private CollectibleTypeRepository typeRepo;
	
	@Autowired
	private GamerRepository gamerRepo;

	public CollectibleServiceImpl() {
		super();
	}

	@Override
	public Flux<Collectible> getCollectibles(String filter) {
		return repo.findByGamerId(UUID.fromString(filter));
	}

	@Override
	public Flux<Collectible> getAllCollectibles() {
		return repo.findAll();
	}

	@Override
	public Mono<Collectible> updateCollectible(Collectible c) {
		return repo.save(c);
	}

	@Override
	public Mono<Collectible> createCollectible(Collectible c) {
		log.debug("creating collectible");
		return repo.findByGamerId(c.getGamerId())
				.defaultIfEmpty(emptyCollectible)
				.collectList()
				.flatMap(owned -> {
					if (owned.stream().anyMatch(collectible -> collectible.getTypeId() == c.getTypeId())) {
						log.debug("looks like owned stuff was found");
						log.debug(owned.toString());
						return Mono.empty();
					}
					return repo.insert(c);
		});
	}
	
	@Override
	public Mono<ResponseEntity<?>> upgradeCollectible(UUID collectibleId) {
		return repo.findById(collectibleId)
				.flatMap(collectible -> {
					return gamerRepo.findById(collectible.getGamerId())
							.flatMap(gamer -> {
								int upgradeCost = collectible.getCurrentStat() * 1000;
								if(gamer.getStrings() < upgradeCost) {
									return Mono.just(ResponseEntity.badRequest()
											.body("Insufficient strings to upgrade."));
								} else {
									gamer.setStrings(gamer.getStrings() - upgradeCost);
									return gamerRepo.save(gamer).flatMap(gg -> {
										collectible.setCurrentStat(collectible.getCurrentStat() + 1);
										return updateCollectible(collectible)
												.thenReturn(ResponseEntity.ok(collectible));
									});
								}
							});
				});
	}

	@Override
	public Mono<Collectible> getCollectible(String id) {
		return repo.findById(UUID.fromString(id));
	}

	@Override
	public Mono<ResponseEntity<?>> removeCollectible(UUID collectibleId, UUID gamerId) {
		return repo.findById(collectibleId).flatMap(collectible -> {
			log.debug("CollectibleId: {} ", collectibleId);
			log.debug("Collectible's gamerId: {}", collectible.getGamerId());
			log.debug("GamerId: {}", gamerId);
			if(!collectible.getGamerId().equals(gamerId)) {
				return Mono.just(ResponseEntity.badRequest()
						.body("Collectible does not belong to the specified gamer."));
			} else {
				return repo.delete(collectible).thenReturn(ResponseEntity.ok(collectible));
			}
		});
	}

	@Override
	public Mono<ResponseEntity<?>> collectibleFusion(List<UUID> collectibleIds) {
		// if minimum quantity of collectible IDs needed to fuse is not met, return badRequest
		int fusionQuantity = 5;
		if(collectibleIds.size() != fusionQuantity) {
			return Mono.just(ResponseEntity.badRequest()
					.body("You need to sacrifice "
			+fusionQuantity+" collectibles to fuse them into the next stage :) "));
		}
		// create a list of collectibles from the given collectible IDs
		List<Collectible> collectibles = new ArrayList<Collectible>();
		for(UUID id : collectibleIds) {
			repo.findById(id).subscribe(collectibles::add);
		}
		// if the collectibles in the list aren't all of the same type, return badRequest
		int type = collectibles.get(0).getTypeId();
		for(Collectible collectible : collectibles) {
			if(collectible.getTypeId() != type) {
				return Mono.just(ResponseEntity.badRequest()
						.body("You can only fuse 5 collectibles of the same type!"));
			}
		}
		// if there is no next stage, return a badRequest
		if(typeRepo.findById(type).block().getNextStage().equals(null)) {
			return Mono.just(ResponseEntity.badRequest().body("These collectibles are already at the final stage!"));
		}
		// if everything checks out, build the next stage collectible
		UUID gamerId = collectibles.get(0).getGamerId();
		int nextStageId = typeRepo.findById(type).block().getNextStage();
		
		Collectible nextStage = new Collectible();
		nextStage.setGamerId(gamerId);
		nextStage.setId(Uuids.timeBased());
		nextStage.setTypeId(nextStageId);
		nextStage.setCurrentStage(typeRepo.findById(nextStageId).block().getStage());
		nextStage.setCurrentStat(typeRepo.findById(nextStageId).block().getBaseStat());

		return repo.deleteAll(collectibles)
				.thenReturn(repo.insert(nextStage))
				.thenReturn(ResponseEntity.ok(nextStage));
	}
	
}
