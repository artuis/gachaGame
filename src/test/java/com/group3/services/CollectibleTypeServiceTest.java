package com.group3.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.group3.beans.CollectibleType;
import com.group3.data.CollectibleTypeRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class CollectibleTypeServiceTest {
	@TestConfiguration
	static class Configuration {
		@Bean
		public CollectibleTypeServiceImpl getCollectibleTypeServiceImpl(CollectibleTypeRepository ctr) {
			CollectibleTypeServiceImpl cts = new CollectibleTypeServiceImpl();
			cts.setCollectibleTypeRepo(ctr);
			return cts;
		}
		
		@Bean
		CollectibleTypeRepository getCollectibleTypeRepository() {
			return Mockito.mock(CollectibleTypeRepository.class);
		}
	}
	
	@Autowired
	private CollectibleTypeServiceImpl cts;
	@Autowired
	private CollectibleTypeRepository ctr;
	
	@Test
	void rollCollectibleRollsCollectible() {
		CollectibleType ct = new CollectibleType();
		Mockito.when(ctr.findCollectiblesByStage(Mockito.any())).thenReturn(Flux.just(ct));
		Mono<CollectibleType> result = cts.rollCollectibleType();
		StepVerifier.create(result).expectNext(ct).verifyComplete();
	}
	
	@Test
	void createCollectibleTypeCreatesCollectibleType() {
		CollectibleType ct = new CollectibleType();
		Mockito.when(ctr.insert(ct)).thenReturn(Mono.just(ct));
		Mono<CollectibleType> result = cts.createCollectibleType(ct);
		StepVerifier.create(result).expectNext(ct).verifyComplete();
	}
	
	@Test
	void updateCollectibleTypeUpdatesCollectibleType() {
		CollectibleType ct = new CollectibleType();
		Mockito.when(ctr.save(ct)).thenReturn(Mono.just(ct));
		Mono<CollectibleType> result = cts.updateCollectibleType(ct);
		StepVerifier.create(result).expectNext(ct).verifyComplete();
	}
	
	@Test
	void getCollectibleTypeGetsCollectibleType() {
		CollectibleType ct = new CollectibleType();
		Mockito.when(ctr.findAll()).thenReturn(Flux.just(ct));
		Flux<CollectibleType> result = cts.getAllCollectibleTypes();
		StepVerifier.create(result).expectNext(ct).verifyComplete();
	}
	
	@Test
	void getAllCollectibleTypeGetsAllCollectibleTypes() {
		CollectibleType ct = new CollectibleType();
		ct.setId(1);
		Mockito.when(ctr.findById(1)).thenReturn(Mono.just(ct));
		Mono<CollectibleType> result = cts.getCollectibleType(1);
		StepVerifier.create(result).expectNext(ct).verifyComplete();
	}
}
