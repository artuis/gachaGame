package controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.group3.beans.Collectible;
import com.group3.beans.Gamer;
import com.group3.controllers.GamerController;
import com.group3.services.CollectibleService;
import com.group3.services.CollectibleTypeService;
import com.group3.services.GamerService;
import com.group3.util.JWTUtil;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
public class GamerControllerTest {
	@TestConfiguration
	static class Configuration {
		@Bean
		public GamerController getGamerController(Gamer gg, Collectible c, JWTUtil jwtUtil, GamerService gs, CollectibleTypeService cts, CollectibleService cs) {
			GamerController gc = new GamerController();
			gc.setEmptyCollectible(c);
			gc.setEmptyGamer(gg);
			gc.setJWTUtil(jwtUtil);
			gc.setGamerService(gs);
			gc.setCollectibleTypeService(cts);
			gc.setCollectibleService(cs);
			return gc;
		}
		
		
		@Bean
		public Collectible getEmptyCollectible() {
			return Mockito.mock(Collectible.class);
		}
		
		@Bean Gamer getEmptyGamer() {
			return Mockito.mock(Gamer.class);
		}
		
		@Bean JWTUtil getJWTUtil() {
			return Mockito.mock(JWTUtil.class);
		}

		@Bean
		public GamerService getGamerService() {
			return Mockito.mock(GamerService.class);
		}
		
		@Bean
		public CollectibleTypeService getCollectibleTypeService() {
			return Mockito.mock(CollectibleTypeService.class);
		}
		
		@Bean CollectibleService getCollectibleService() {
			return Mockito.mock(CollectibleService.class);
		}
	}
	
	@Autowired
	private Gamer g;
	@Autowired
	private Collectible c;
	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private GamerController gc;
	@Autowired
	private GamerService gs;
	@Autowired
	private CollectibleService cs;
	
	@Test
	public void testRegisterGamerReturnesEntityWithStatus201() {
		Gamer gamer = new Gamer();
		Mockito.when(gs.addGamer(gamer)).thenReturn(Mono.just(gamer));
		Mono<ResponseEntity<Gamer>> result = gc.registerGamer(gamer);
		StepVerifier.create(result).expectNext(ResponseEntity.status(201).body(gamer));
	}
	
	@Test
	public void 
}
