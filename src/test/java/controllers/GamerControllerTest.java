package controllers;

import java.time.Duration;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.group3.beans.Collectible;
import com.group3.beans.Gamer;
import com.group3.controllers.GamerController;
import com.group3.services.CollectibleService;
import com.group3.services.CollectibleTypeService;
import com.group3.services.GamerService;
import com.group3.util.JWTUtil;

import reactor.core.publisher.Flux;
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
		gamer.setUsername("test");
		gamer.setGamerId(Uuids.timeBased());
		Mockito.when(gs.addGamer(gamer)).thenReturn(Mono.just(gamer));
		Mono<ResponseEntity<Gamer>> result = gc.registerGamer(gamer);
		StepVerifier.create(result).expectNext(ResponseEntity.status(HttpStatus.CREATED).body(gamer)).verifyComplete();
	}
	
	@Test
	public void testGetGamersReturnsGamers() {
		Gamer gamer = new Gamer();
		Mockito.when(gs.getGamers()).thenReturn(Flux.just(gamer));
		Flux<Gamer> result = gc.getGamers();
		StepVerifier.create(result).expectNext(gamer).verifyComplete();
	}
	
	@Test
	public void testValidLoginLogsReturns200() {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post("/gamers/login"));
		Gamer gamer = new Gamer();
		gamer.setUsername("test");
		Mockito.when(gs.findByUsername(gamer.getUsername())).thenReturn(Mono.just(gamer));
		Mono<ResponseEntity<Gamer>> result = gc.login(gamer, exchange);
		StepVerifier.create(result).expectNext(ResponseEntity.ok(gamer)).verifyComplete();
	}
	
	@Test
	public void testLogoutAdds0AgeToken() {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.delete("/gamers/logout"));
		Mono<ServerResponse> result = gc.logout(exchange);
		result.subscribe();
		Assert.isTrue(exchange.getResponse().getCookies().containsKey("token"), "\"token\" not found");
		Assert.isTrue(exchange.getResponse().getCookies().getFirst("token").getMaxAge().equals(Duration.ZERO), "age not 0");
	}
	
	@Test
	public void getGamerByUsernameGetsGamer() {
		
	}
}
