package com.group3.test;

import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.group3.beans.Collectible;
import com.group3.beans.CollectibleType;
import com.group3.beans.CollectibleType.Stage;
import com.group3.controllers.GamerController;
import com.group3.beans.Gamer;
import com.group3.services.CollectibleService;
import com.group3.services.CollectibleTypeService;
import com.group3.services.EmailService;
import com.group3.services.GamerService;
import com.group3.util.JWTUtil;

import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class GamerControllerTest {
	@TestConfiguration
	static class Configuration {
		@Bean
		public GamerController getGamerController(Gamer gg, Collectible c, JWTUtil jwtUtil, GamerService gs, CollectibleTypeService cts, CollectibleService cs, EmailService es) {
			GamerController gc = new GamerController();
			gc.setEmptyCollectible(c);
			gc.setEmptyGamer(gg);
			gc.setJWTUtil(jwtUtil);
			gc.setGamerService(gs);
			gc.setCollectibleTypeService(cts);
			gc.setCollectibleService(cs);
			gc.setEmailService(es);
			return gc;
		}
		
		@Bean
		Collectible getEmptyCollectible() {
			return Mockito.mock(Collectible.class);
		}
		
		@Bean 
		Gamer getEmptyGamer() {
			return Mockito.mock(Gamer.class);
		}
		
		@Bean 
		JWTUtil getJWTUtil() {
			return Mockito.mock(JWTUtil.class, Mockito.RETURNS_DEEP_STUBS);
		}

		@Bean
		GamerService getGamerService() {
			return Mockito.mock(GamerService.class);
		}
		
		@Bean
		CollectibleTypeService getCollectibleTypeService() {
			return Mockito.mock(CollectibleTypeService.class);
		}
		
		@Bean 
		CollectibleService getCollectibleService() {
			return Mockito.mock(CollectibleService.class);
		}
		
		@Bean 
		EmailService getEmailService() {
			return Mockito.mock(EmailService.class);
		}
	}

	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private GamerController gc;
	@Autowired
	private GamerService gs;
	@Autowired
	private CollectibleService cs;
	@Autowired 
	private CollectibleTypeService cts;
	
	@Test
	void testRegisterGamerReturnesEntityWithStatus201() {
		Gamer gamer = new Gamer();
		gamer.setUsername("test");
		gamer.setRole(Gamer.Role.GAMER);
		gamer.setEmail("test@test.test");
		gamer.setGamerId(Uuids.timeBased());
		Mockito.when(gs.addGamer(gamer)).thenReturn(Mono.just(gamer));
		Mono<ResponseEntity<Gamer>> result = gc.registerGamer(gamer);
		StepVerifier.create(result).expectNext(ResponseEntity.status(HttpStatus.CREATED).body(gamer)).verifyComplete();
	}
	
	
	@Test
	void testGetGamersReturnsGamers() {
		Gamer gamer = new Gamer();
		Mockito.when(gs.getGamers()).thenReturn(Flux.just(gamer));
		Flux<Gamer> result = gc.getGamers();
		StepVerifier.create(result).expectNext(gamer).verifyComplete();
	}
	
	@Test
	void testValidLoginLogsReturns200() {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post("/gamers/login"));
		Gamer gamer = new Gamer();
		gamer.setUsername("test");
		gamer.setRole(Gamer.Role.GAMER);
		Mockito.when(gs.findByUsername(gamer.getUsername())).thenReturn(Mono.just(gamer));
		Mono<ResponseEntity<Gamer>> result = gc.login(gamer, exchange);
		StepVerifier.create(result).expectNext(ResponseEntity.ok(gamer)).verifyComplete();
	}
	
	@Test
	void testLogoutAdds0AgeToken() {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.delete("/gamers/logout"));
		Mono<ServerResponse> result = gc.logout(exchange);
		result.subscribe();
		Assert.isTrue(exchange.getResponse().getCookies().containsKey("token"), "\"token\" not found");
		Assert.isTrue(exchange.getResponse().getCookies().getFirst("token").getMaxAge().equals(Duration.ZERO), "age not 0");
	}
	
	@Test
	void getGamerByUsernameGetsGamer() {
		String name = "name";
		Gamer gamer = new Gamer();
		gamer.setUsername(name);
		Mockito.when(gs.findGamerByUsername(name)).thenReturn(Mono.just(gamer));
		Mono<ResponseEntity<Gamer>> result = gc.getGamerByUsername(name);
		StepVerifier.create(result).expectNext(ResponseEntity.ok(gamer)).verifyComplete();
	}
	
	@Test
	void updateGamerUpdatesGamer() {
		Gamer gamer = new Gamer();
		Gamer updatedGamer = new Gamer();
		updatedGamer.setUsername("name");
		Mockito.when(gs.updateGamer(gamer)).thenReturn(Mono.just(updatedGamer));
		Mono<ResponseEntity<Gamer>> result = gc.updateGamer(gamer);
		StepVerifier.create(result).expectNext(ResponseEntity.ok(updatedGamer)).verifyComplete();
	}
	
	@Test
	void banGamerBansGamer() {
		UUID rand = UUID.randomUUID();
		Gamer gamer = new Gamer();
		gamer.setEmail("test@test.test");
		gamer.setGamerId(rand);
		long daysBanned = 7l;
		Mockito.when(gs.banGamer(gamer.getGamerId(), daysBanned)).thenReturn(Mono.just(gamer));
		Mono<ResponseEntity<Gamer>> result = gc.banGamer(rand, daysBanned);
		StepVerifier.create(result).expectNext(ResponseEntity.ok(gamer)).verifyComplete();
	}
	
	@Test
	void rollNewCollectibleRollsCollectibleFromDailyRolls() {
		JWTUtil generator = new JWTUtil();
		generator.setExpirationTime("28800");
		generator.setSecret("ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave64ByteLength");
		generator.setKey(Keys.hmacShaKeyFor("ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave64ByteLength".getBytes()));
		
		Gamer gamer = new Gamer();
		gamer.setUsername("test");
		gamer.setGamerId(UUID.randomUUID());
		gamer.setAuthorities(Arrays.asList(Gamer.Role.GAMER));
		gamer.setDailyRolls(1);
		gamer.setStrings(1000);
		gamer.setStardust(10);
		gamer.setCollectionSize(0);
		gamer.setCollectionStrength(0);
		
		Gamer after = new Gamer();
		after.setUsername("test");
		after.setGamerId(gamer.getGamerId());
		after.setAuthorities(Arrays.asList(Gamer.Role.GAMER));
		after.setDailyRolls(0);
		
		CollectibleType collectibleType = new CollectibleType();
		collectibleType.setBaseStat(5);
		collectibleType.setId(1);
		collectibleType.setName("test");
		collectibleType.setStage(Stage.STAGE_1);
		
		Collectible collectible = Collectible.fromCollectibleTypeAndId(collectibleType, gamer.getGamerId());
		HttpCookie hc = new HttpCookie("token", generator.generateToken(gamer));
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.put("/ganers/collectibles/roll").cookie(hc));

		Mockito.when(gs.getGamer(gamer.getGamerId())).thenReturn(Mono.just(gamer));
		Mockito.when(jwtUtil.getAllClaimsFromToken(Mockito.anyString()).get(Mockito.anyString())).thenReturn(gamer.getGamerId().toString());
		Mockito.when(cts.rollCollectibleType()).thenReturn(Mono.just(collectibleType));
		Mockito.when(cs.createCollectible(Mockito.any())).thenReturn(Mono.just(collectible));
		Mockito.when(gs.updateGamer(Mockito.any())).thenReturn(Mono.just(after));
		Mono<ResponseEntity<Object>> result = gc.rollNewCollectible(exchange);
		StepVerifier.create(result).expectNext(ResponseEntity.ok(collectibleType)).verifyComplete();
	}
	
	@Test
	void rollNewCollectibleRollsCollectibleFromStrings() {
		JWTUtil generator = new JWTUtil();
		generator.setExpirationTime("28800");
		generator.setSecret("ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave64ByteLength");
		generator.setKey(Keys.hmacShaKeyFor("ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave64ByteLength".getBytes()));
		
		Gamer gamer = new Gamer();
		gamer.setUsername("test");
		gamer.setGamerId(UUID.randomUUID());
		gamer.setAuthorities(Arrays.asList(Gamer.Role.GAMER));
		gamer.setDailyRolls(0);
		gamer.setRolls(0);
		gamer.setStrings(1000);
		gamer.setStardust(10);
		gamer.setCollectionSize(0);
		gamer.setCollectionStrength(0);
		
		Gamer after = new Gamer();
		after.setUsername("test");
		after.setGamerId(gamer.getGamerId());
		after.setAuthorities(Arrays.asList(Gamer.Role.GAMER));
		after.setDailyRolls(0);
		
		CollectibleType collectibleType = new CollectibleType();
		collectibleType.setBaseStat(5);
		collectibleType.setId(1);
		collectibleType.setName("test");
		collectibleType.setStage(Stage.STAGE_1);
		
		Collectible collectible = Collectible.fromCollectibleTypeAndId(collectibleType, gamer.getGamerId());
		HttpCookie hc = new HttpCookie("token", generator.generateToken(gamer));
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.put("/ganers/collectibles/roll").cookie(hc));

		Mockito.when(gs.getGamer(gamer.getGamerId())).thenReturn(Mono.just(gamer));
		Mockito.when(jwtUtil.getAllClaimsFromToken(Mockito.anyString()).get(Mockito.anyString())).thenReturn(gamer.getGamerId().toString());
		Mockito.when(cts.rollCollectibleType()).thenReturn(Mono.just(collectibleType));
		Mockito.when(cs.createCollectible(Mockito.any())).thenReturn(Mono.just(collectible));
		Mockito.when(gs.updateGamer(Mockito.any())).thenReturn(Mono.just(after));
		Mono<ResponseEntity<Object>> result = gc.rollNewCollectible(exchange);
		StepVerifier.create(result).expectNext(ResponseEntity.ok(collectibleType)).verifyComplete();
	}
	
	@Test
	void rollNewCollectibleRollsCollectibleFromRolls() {
		JWTUtil generator = new JWTUtil();
		generator.setExpirationTime("28800");
		generator.setSecret("ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave64ByteLength");
		generator.setKey(Keys.hmacShaKeyFor("ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave64ByteLength".getBytes()));
		
		Gamer gamer = new Gamer();
		gamer.setUsername("test");
		gamer.setGamerId(UUID.randomUUID());
		gamer.setAuthorities(Arrays.asList(Gamer.Role.GAMER));
		gamer.setDailyRolls(0);
		gamer.setRolls(1);
		gamer.setStrings(1000);
		gamer.setStardust(10);
		gamer.setCollectionSize(0);
		gamer.setCollectionStrength(0);
		
		Gamer after = new Gamer();
		after.setUsername("test");
		after.setGamerId(gamer.getGamerId());
		after.setAuthorities(Arrays.asList(Gamer.Role.GAMER));
		after.setDailyRolls(0);
		
		CollectibleType collectibleType = new CollectibleType();
		collectibleType.setBaseStat(5);
		collectibleType.setId(1);
		collectibleType.setName("test");
		collectibleType.setStage(Stage.STAGE_1);
		
		Collectible collectible = Collectible.fromCollectibleTypeAndId(collectibleType, gamer.getGamerId());
		HttpCookie hc = new HttpCookie("token", generator.generateToken(gamer));
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.put("/ganers/collectibles/roll").cookie(hc));

		Mockito.when(gs.getGamer(gamer.getGamerId())).thenReturn(Mono.just(gamer));
		Mockito.when(jwtUtil.getAllClaimsFromToken(Mockito.anyString()).get(Mockito.anyString())).thenReturn(gamer.getGamerId().toString());
		Mockito.when(cts.rollCollectibleType()).thenReturn(Mono.just(collectibleType));
		Mockito.when(cs.createCollectible(Mockito.any())).thenReturn(Mono.just(collectible));
		Mockito.when(gs.updateGamer(Mockito.any())).thenReturn(Mono.just(after));
		Mono<ResponseEntity<Object>> result = gc.rollNewCollectible(exchange);
		StepVerifier.create(result).expectNext(ResponseEntity.ok(collectibleType)).verifyComplete();
	}
	
	@Test
	void rollNewCollectibleRollsCollectibleFromStardust() {
		JWTUtil generator = new JWTUtil();
		generator.setExpirationTime("28800");
		generator.setSecret("ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave64ByteLength");
		generator.setKey(Keys.hmacShaKeyFor("ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave64ByteLength".getBytes()));
		
		Gamer gamer = new Gamer();
		gamer.setUsername("test");
		gamer.setGamerId(UUID.randomUUID());
		gamer.setAuthorities(Arrays.asList(Gamer.Role.GAMER));
		gamer.setRolls(0);
		gamer.setDailyRolls(0);
		gamer.setStrings(0);
		gamer.setStardust(10);
		gamer.setCollectionSize(0);
		gamer.setCollectionStrength(0);
		
		Gamer after = new Gamer();
		after.setUsername("test");
		after.setGamerId(gamer.getGamerId());
		after.setAuthorities(Arrays.asList(Gamer.Role.GAMER));
		after.setDailyRolls(0);
		
		CollectibleType collectibleType = new CollectibleType();
		collectibleType.setBaseStat(5);
		collectibleType.setId(1);
		collectibleType.setName("test");
		collectibleType.setStage(Stage.STAGE_1);
		
		Collectible collectible = Collectible.fromCollectibleTypeAndId(collectibleType, gamer.getGamerId());
		HttpCookie hc = new HttpCookie("token", generator.generateToken(gamer));
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.put("/ganers/collectibles/roll").cookie(hc));

		Mockito.when(gs.getGamer(gamer.getGamerId())).thenReturn(Mono.just(gamer));
		Mockito.when(jwtUtil.getAllClaimsFromToken(Mockito.anyString()).get(Mockito.anyString())).thenReturn(gamer.getGamerId().toString());
		Mockito.when(cts.rollCollectibleType()).thenReturn(Mono.just(collectibleType));
		Mockito.when(cs.createCollectible(Mockito.any())).thenReturn(Mono.just(collectible));
		Mockito.when(gs.updateGamer(Mockito.any())).thenReturn(Mono.just(after));
		Mono<ResponseEntity<Object>> result = gc.rollNewCollectible(exchange);
		StepVerifier.create(result).expectNext(ResponseEntity.ok(collectibleType)).verifyComplete();
	}
}
