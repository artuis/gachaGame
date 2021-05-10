package com.group3.services;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.group3.beans.Collectible;
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
	
}
