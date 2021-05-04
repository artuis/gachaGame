package com.group3.services;

import com.group3.beans.Collectible;
import com.group3.data.CollectibleRepository;

import reactor.core.publisher.Mono;

public class CollectibleServiceImpl implements CollectibleService {
	
	private CollectibleRepository collectibleRepo;
	
	public CollectibleServiceImpl() {
		super();
	}

	@Override
	public Mono<Collectible> rollCollectible() {
		
	}

}
