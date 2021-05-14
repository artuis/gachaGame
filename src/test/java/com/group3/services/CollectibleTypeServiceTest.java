package com.group3.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.group3.data.CollectibleTypeRepository;

@ExtendWith(SpringExtension.class)
class CollectibleTypeServiceTest {
	@TestConfiguration
	static class Configuration {
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
	private CollectibleTypeRepository ctr;
	
	@Test
	void rollCollectibleRollsCollectible() {
		
	}
}
