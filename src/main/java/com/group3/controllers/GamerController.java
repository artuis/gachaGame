package com.group3.controllers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.group3.beans.CollectibleType;
import com.group3.beans.Gamer;
import com.group3.services.CollectibleTypeService;
import com.group3.services.GamerService;
import com.group3.util.JWTUtil;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/gamers")
public class GamerController {
	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private GamerService gamerService;
	@Autowired
	private CollectibleTypeService collectibleService;
	
	@PreAuthorize("hasAuthority('MODERATOR')")
	@GetMapping
	public Publisher<Gamer> getGamers() {
		return gamerService.getGamers();
	}
	
	@GetMapping("{name}")
	public Mono<UserDetails> getGamerByUsername(@PathVariable("name") String name) {
		return gamerService.findByUsername(name);
	}
	
	@PutMapping("/register")
	public Publisher<Gamer> registerGamer(@RequestBody Gamer gg) {
		gg.setRegistrationDate(Date.from(Instant.now()));
		List<Gamer.Role> perms = new ArrayList<Gamer.Role>();
		perms.add(Gamer.Role.GAMER);
		gg.setAuthorities(perms);
		gg.setRegistrationDate(Date.from(Instant.now()));
		return gamerService.addGamer(gg);
	}
	
	@PostMapping("/login")
	public Mono<ResponseEntity<?>> login(@RequestBody Gamer gg, ServerWebExchange exchange) {
		 return gamerService.findByUsername(gg.getUsername())
			.defaultIfEmpty(new Gamer())
			.map(gamer -> {
				if (gamer.getUsername() == null) {
					return ResponseEntity.notFound().build();
				} else {
				System.out.println("hello");
					exchange.getResponse()
							.addCookie(ResponseCookie
								.from("token", jwtUtil
								.generateToken((Gamer) gamer))
								.httpOnly(true).build());
					return ResponseEntity.ok(gamer);
				}
			});
	}
	
	@DeleteMapping
	public ResponseEntity<Void> logout() {
		
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasRole('MODERATOR')")
	@PutMapping("{gamerId}")
	public Publisher<Gamer> updateGamer(@PathVariable("gamerId") int gamerId, @RequestBody Gamer gg) {
		return gamerService.updateGamer(gg);
	}

	@PreAuthorize("hasRole('MODERATOR')")
	@PostMapping("{gamerId}")
	public Publisher<Gamer> banGamer(@PathVariable("gamerId") int gamerId, @RequestBody Date banLiftDate) {
		return gamerService.banGamer(gamerId, banLiftDate);
	}
	
	@PreAuthorize("hasRole('GAMER')")
	@PutMapping("/collectibles/roll")
	
	public Mono<CollectibleType> rollNewCollectible() {
		return collectibleService.rollCollectibleType();
	}
}
