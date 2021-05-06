package com.group3.controllers;

import java.time.Instant;
import java.util.Date;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group3.beans.Collectible;
import com.group3.beans.Gamer;
import com.group3.services.CollectibleService;
import com.group3.services.GamerService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/gamers")
public class GamerController {
	@Autowired
	private GamerService gamerService;
	@Autowired
	private CollectibleService collectibleService;

	@GetMapping
	public Publisher<Gamer> getGamers() {
		return gamerService.getGamers();
	}

	@PutMapping
	public Publisher<Gamer> registerGamer(@RequestBody Gamer gg) {
		gg.setRegistrationDate(Date.from(Instant.now()));
		return gamerService.addGamer(gg);
	}

	@PostMapping
	public Publisher<Gamer> login(@RequestBody Gamer gg) {
		gg.setLastLogin(Date.from(Instant.now()));
		gamerService.updateGamer(gg);
		return gamerService.getGamer(gg.getGamerId());
	}

	@DeleteMapping
	public ResponseEntity<Void> logout() {
		return ResponseEntity.noContent().build();
	}

	@PutMapping("{gamerId}")
	public ResponseEntity<Gamer> updateGamer(@PathVariable("gamerId") int gamerId, @RequestBody Gamer gg) {
		gamerService.updateGamer(gg);
		return ResponseEntity.ok(gg);
	}

	@PutMapping("/collectibles/roll")
	public Mono<Collectible> rollNewCollectible() {
		return collectibleService.rollCollectible();
	}
}
