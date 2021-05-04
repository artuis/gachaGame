package com.group3.services;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.beans.Collectible;
import com.group3.data.CollectibleRepository;

import reactor.core.publisher.Mono;

@Service
public class CollectibleServiceImpl implements CollectibleService {
	
	@Autowired
	private CollectibleRepository collectibleRepo;
	
	public CollectibleServiceImpl() {
		super();
	}

	@Override
	public Mono<Collectible> rollCollectible() {
		return null;
	}
	
	@Override
	public Mono<Collectible> createCollectible(Collectible c) {
		return collectibleRepo.insert(c);
	}

	@Override
	public Publisher<Collectible> updateCollectible(Collectible c) {
		return collectibleRepo.save(c);
	}
}
