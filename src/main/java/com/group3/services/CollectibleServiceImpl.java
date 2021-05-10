package com.group3.services;

import java.util.UUID;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.beans.Collectible;
import com.group3.beans.Gamer;
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
	public Publisher<Collectible> updateCollectible(Collectible c) {
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
	public Mono<Collectible> upgradeCollectible(UUID collectibleId) {
		log.debug("Upgrading collectible");
		return repo.findById(collectibleId)
				.map(collectible -> {
					if(gamerRepo.findById(collectible.getGamerId()).map(gamer -> {
						int upgradeCost = collectible.getCurrentStat() * 1000;
						if(gamer.getStrings() < upgradeCost) {
							log.debug("Insufficient strings; upgrade cost: "+upgradeCost);
							return Mono.empty();
						}
						else {
							gamer.setStrings(gamer.getStrings() - upgradeCost);
							return gamerRepo.save(gamer).subscribe();
						}
					}).hasElement() != null) {
						collectible.setCurrentStat(collectible.getCurrentStat()+1);
					}
				repo.save(collectible).subscribe();
				return collectible;
		});
	}

	@Override
	public Publisher<Collectible> getCollectible(String id) {
		return repo.findById(UUID.fromString(id));
	}
	
}
