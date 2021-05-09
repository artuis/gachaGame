package com.group3.services;

import java.util.UUID;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.beans.Collectible;
import com.group3.data.CollectibleRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CollectibleServiceImpl implements CollectibleService {

	private Logger log = LoggerFactory.getLogger(CollectibleServiceImpl.class);
	
	@Autowired
	private Collectible emptyCollectible;

	@Autowired
	private CollectibleRepository repo;

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

}
