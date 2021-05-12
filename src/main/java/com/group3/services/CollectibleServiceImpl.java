package com.group3.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.group3.beans.Collectible;
import com.group3.beans.CollectibleType;
import com.group3.data.CollectibleRepository;
import com.group3.data.GamerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CollectibleServiceImpl implements CollectibleService {

	private Logger log = LoggerFactory.getLogger(CollectibleServiceImpl.class);
	
	@Autowired
	private Collectible emptyCollectible;
	
	@Autowired
	private CollectibleTypeServiceImpl typeServ;

	@Autowired
	private CollectibleRepository repo;
	
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
						log.debug("Looks like a repeat collectible was rolled.");
						log.debug(owned.toString());
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
			if(!collectible.getGamerId().equals(gamerId)) {
				return Mono.just(ResponseEntity.badRequest()
						.body("Collectible does not belong to the specified gamer."));
			} else {
				return repo.delete(collectible).thenReturn(ResponseEntity.ok(collectible));
			}
		});
	}

	@Override
	public Mono<Collectible> collectibleFusion(List<UUID> collectibleIds) {
		// create a list of collectibles from the given collectible IDs
		List<Collectible> defaultEmpty = new ArrayList<>();
		defaultEmpty.add(emptyCollectible);
		List<Collectible> collectibles = getAllCollectibles()
				.filter(collectible -> collectibleIds.contains(collectible.getId()))
				.collectList().defaultIfEmpty(defaultEmpty).block();
		// null check
		if(collectibles == null || collectibles.isEmpty() || collectibles.get(0).equals(emptyCollectible)) {
			return Mono.empty();
		}
		// if the collectibles in the list aren't all of the same type, return empty
		int type = collectibles.get(0).getTypeId();
		for(Collectible collectible : collectibles) {
			if(collectible.getTypeId() != type) {
				return Mono.empty();
			}
		}
		// if everything checks out, build the next stage collectible
		UUID gamerId = collectibles.get(0).getGamerId();
		CollectibleType nextStageBase = null;
		try {
			nextStageBase = typeServ.getCollectibleType(type).flatMap(currentType -> 
			typeServ.getCollectibleType(currentType.getNextStage()).flatMap(nextType -> {
				if(nextType != null) {
					return Mono.just(nextType);
				} else { return Mono.just(currentType);}
			})).cast(CollectibleType.class).block();
		} catch (NullPointerException e) {
			log.debug("A little null pointer exception never hurt anyone.");
		}
		if(nextStageBase == null || nextStageBase.getId() == type) {
			return Mono.empty();
		}
		Collectible nextStage = Collectible.fromCollectibleTypeAndId(nextStageBase, gamerId);
		return repo.deleteAll(collectibles).then(repo.save(nextStage));
	}
	
}
