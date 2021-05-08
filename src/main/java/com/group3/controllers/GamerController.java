package com.group3.controllers;

import java.time.Duration;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;

import com.group3.beans.Collectible;
import com.group3.beans.CollectibleType;
import com.group3.beans.Gamer;
import com.group3.services.CollectibleService;
import com.group3.services.CollectibleTypeService;
import com.group3.services.GamerService;
import com.group3.util.JWTUtil;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/gamers")
public class GamerController {
	@Autowired
	private Gamer emptyGamer;
	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private GamerService gamerService;
	@Autowired
	private CollectibleTypeService collectibleTypeService;
	@Autowired
	private CollectibleService collectibleService;
	
	private Logger log = LoggerFactory.getLogger(GamerController.class);

	@PreAuthorize("hasAuthority('MODERATOR')")
	@GetMapping
	public Publisher<Gamer> getGamers() {
		return gamerService.getGamers();
	}

	@GetMapping("{name}")
	public Mono<ResponseEntity<?>> getGamerByUsername(@PathVariable("name") String name) {
		return gamerService.findGamerByUsername(name).defaultIfEmpty(emptyGamer).map(gamer -> {
			if (gamer.getUsername() != null) {
				return ResponseEntity.ok(gamer);
			}
			return ResponseEntity.notFound().build();
		});
	}

	@PutMapping("/register")
	public Publisher<Gamer> registerGamer(@RequestBody Gamer gg) {
		return gamerService.addGamer(gg);
	}

	@PostMapping("/login")
	public Mono<ResponseEntity<?>> login(@RequestBody Gamer gg, ServerWebExchange exchange) {
		return gamerService.findByUsername(gg.getUsername()).defaultIfEmpty(emptyGamer).map(gamer -> {
			if (gamer.getUsername() == null) {
				return ResponseEntity.notFound().build();
			} else {
				exchange.getResponse().addCookie(
						ResponseCookie.from("token", jwtUtil.generateToken((Gamer) gamer)).httpOnly(true).build());
				return ResponseEntity.ok(gamer); // 👌
			}
		});
	}

	@DeleteMapping("/logout")
	public Mono<ServerResponse> logout(ServerWebExchange exchange) {
		ResponseCookie cookie = ResponseCookie.from("token", "").maxAge(0).build();
		exchange.getResponse().addCookie(cookie);
		return ServerResponse.noContent().build();
	}

	@PreAuthorize("hasAuthority('MODERATOR')")
	@PutMapping("{gamerId}")
	public Mono<ResponseEntity<Gamer>> updateGamer(@PathVariable("gamerId") int gamerId, @RequestBody Gamer gg) {
		return gamerService.updateGamer(gg).defaultIfEmpty(emptyGamer).map(gamer -> {
			if (gamer.getUsername() == null) {
				return ResponseEntity.notFound().build();
			} else {
				return ResponseEntity.ok(gamer);
			}
		});
	}

	@PreAuthorize("hasAuthority('MODERATOR')")
	@PostMapping("{gamerId}")
	public Publisher<Gamer> banGamer(@PathVariable("gamerId") int gamerId,
			@RequestParam("daysBanned") long daysBanned) {
		return gamerService.banGamer(gamerId, daysBanned);
	}

	@PreAuthorize("hasAuthority('GAMER')")
	@PutMapping("/collectibles/roll")
	public Mono<CollectibleType> rollNewCollectible(ServerWebExchange exchange) {
		String token = exchange.getRequest().getCookies().getFirst("token").getValue();
		return gamerService.getGamer((int) jwtUtil.getAllClaimsFromToken(token).get("id")).flatMap(gamer -> {
			log.debug("" + gamer.getStardust());
			log.debug("" + gamer.getStrings());
			log.debug("" + gamer.getDailyRolls());
			if (gamer.getDailyRolls() > 0) {
				gamer.setDailyRolls(gamer.getDailyRolls() - 1);
			} else if (gamer.getStrings() >= 1000) {
				gamer.setStrings(gamer.getStrings() - 1);
			} else if (gamer.getStardust() >= 100) {
				gamer.setStardust(gamer.getStardust() - 100);
			}
			return collectibleTypeService.rollCollectibleType().map(rolled -> {
				Collectible collectible = new Collectible();
				return rolled;
			})
		});
	}
}
