package com.group3.controllers;

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

import com.group3.beans.Gamer;
import com.group3.services.GamerService;

@RestController
@RequestMapping(value = "/gamers")
public class GamerController {
	@Autowired
	private GamerService gamerService;

	@GetMapping
	public Publisher<Gamer> getGamers() {
		return gamerService.getGamers();
	}

	@PutMapping
	public Publisher<Gamer> registerGamer(@RequestBody Gamer gg) {
		return gamerService.addGamer(gg);
	}

	@PostMapping
	public Publisher<Gamer> login(@RequestBody Gamer gg) {
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

}
