package com.group3.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.group3.beans.Gamer;
import com.group3.services.CollectibleService;
import com.group3.services.CollectibleTypeService;
import com.group3.services.GamerService;
import com.group3.util.JWTUtil;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/gamers")
public class GamerController {
	private Gamer emptyGamer;
	private Collectible emptyCollectible;
	private JWTUtil jwtUtil;
	private GamerService gamerService;
	private CollectibleTypeService collectibleTypeService;
	private CollectibleService collectibleService;

	private Logger log = LoggerFactory.getLogger(GamerController.class);

	public GamerController() {
		super();
	}

	@Autowired
	public void setEmptyGamer(Gamer g) {
		this.emptyGamer = g;
	}

	@Autowired
	public void setEmptyCollectible(Collectible c) {
		this.emptyCollectible = c;
	}

	@Autowired
	public void setJWTUtil(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Autowired
	public void setGamerService(GamerService gs) {
		this.gamerService = gs;
	}

	@Autowired
	public void setCollectibleTypeService(CollectibleTypeService cts) {
		this.collectibleTypeService = cts;
	}

	@Autowired
	public void setCollectibleService(CollectibleService gs) {
		this.collectibleService = gs;
	}

	@PreAuthorize("hasAuthority('MODERATOR')")
	@GetMapping
	public Mono<ResponseEntity<List<Gamer>>> getGamers() {
		return gamerService.getGamers().collectList().map(gamers -> ResponseEntity.ok(gamers));
	}

	// more human readable way of obtaining a stored gamer
	@GetMapping("{name}")
	public Mono<ResponseEntity<Gamer>> getGamerByUsername(@PathVariable("name") String name) {
		return gamerService.findGamerByUsername(name).defaultIfEmpty(emptyGamer).map(gamer -> {
			if (gamer.getUsername() != null) {
				return ResponseEntity.ok(gamer);
			}
			return ResponseEntity.notFound().build();
		});
	}

	@PutMapping("/register")
	public Mono<ResponseEntity<Gamer>> registerGamer(@RequestBody Gamer gg) {
		return gamerService.addGamer(gg).defaultIfEmpty(emptyGamer).map(gamer -> {
			if (gamer.getUsername() == null || gamer.getGamerId() == null) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(gg);
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(gamer);
		});
	}

	@PostMapping("/login")
	public Mono<ResponseEntity<Gamer>> login(@RequestBody Gamer gg, ServerWebExchange exchange) {
		return gamerService.findByUsername(gg.getUsername()).defaultIfEmpty(emptyGamer).cast(Gamer.class).map(gamer -> {
			if (gamer.getUsername() == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gg);
			}
			exchange.getResponse().addCookie(ResponseCookie.from("token", jwtUtil.generateToken((Gamer) gamer))
					.path("/").httpOnly(true).build());
			return ResponseEntity.ok(gamer); // 👌

		});
	}

	@DeleteMapping("/logout")
	public Mono<ServerResponse> logout(ServerWebExchange exchange) {
		ResponseCookie cookie = ResponseCookie.from("token", "").maxAge(0).build();
		exchange.getResponse().addCookie(cookie);
		return ServerResponse.noContent().build();
	}

	// note that if you use this route, you must input the rest of the fields
	// including the ones you dont want to update
	@PreAuthorize("hasAuthority('MODERATOR')")
	@PutMapping
	public Mono<ResponseEntity<Gamer>> updateGamer(@RequestBody Gamer gg) {
		return gamerService.updateGamer(gg).defaultIfEmpty(emptyGamer).map(gamer -> {
			if (gamer.getUsername() == null) {
				return ResponseEntity.notFound().build();
			} else {
				return ResponseEntity.ok(gamer);
			}
		});
	}

	@PreAuthorize("hasAuthority('MODERATOR')")
	@PostMapping("/ban/{gamerId}")
	public Mono<ResponseEntity<Gamer>> banGamer(@PathVariable("gamerId") UUID gamerId,
			@RequestParam("daysBanned") long daysBanned) {
		return gamerService.banGamer(gamerId, daysBanned).defaultIfEmpty(emptyGamer).map(gamer -> {
			if (gamer.getGamerId() == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(gamer);
		});
	}

	@PreAuthorize("hasAuthority('GAMER')")
	@PutMapping("/collectibles/roll")
	public Mono<ResponseEntity<Object>> rollNewCollectible(ServerWebExchange exchange) {
		String token = exchange.getRequest().getCookies().getFirst("token").getValue();
		return gamerService.getGamer(UUID.fromString((String) jwtUtil.getAllClaimsFromToken(token).get("id")))
				.flatMap(gamer -> {
					log.debug("" + gamer.getStardust());
					log.debug("" + gamer.getStrings());
					log.debug("" + gamer.getDailyRolls());
					if (gamer.getDailyRolls() > 0) {
						gamer.setDailyRolls(gamer.getDailyRolls() - 1);
					} else if (gamer.getStrings() >= 1000) {
						gamer.setStrings(gamer.getStrings() - 1000);
					} else if (gamer.getStardust() >= 10) {
						gamer.setStardust(gamer.getStardust() - 10);
					} else {
						return Mono.just(ResponseEntity.badRequest().body("not enough money 5head"));
					}
					log.debug("Stardust: {}", gamer.getStardust());
					log.debug("Strings: {}", gamer.getStrings());
					log.debug("Daily Rolls: {}", gamer.getDailyRolls());
					return collectibleTypeService.rollCollectibleType().flatMap(rolled -> {
						log.debug("rolled: {}", rolled);
						Collectible collectible = Collectible.fromCollectibleTypeAndId(rolled, gamer.getGamerId());
						return collectibleService.createCollectible(collectible).defaultIfEmpty(emptyCollectible)
								.flatMap(collected -> {
									if (collected == emptyCollectible) {
										log.debug("consolation prize");
										gamer.setStrings(gamer.getStrings() + 100);
									} else {
										gamer.setCollectionSize(gamer.getCollectionSize() + 1);
										gamer.setCollectionStrength(
												gamer.getCollectionStrength() + collected.getCurrentStat());
									}
									return gamerService.updateGamer(gamer).thenReturn(ResponseEntity.ok(rolled));
								});
					});

				});
	}
}
