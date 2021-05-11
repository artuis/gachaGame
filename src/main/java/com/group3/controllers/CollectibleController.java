package com.group3.controllers;

import java.util.List;
import java.util.UUID;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group3.beans.Collectible;
import com.group3.beans.CombinedCollectible;
import com.group3.services.CollectibleService;
import com.group3.services.CollectibleTypeService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/collectibles")
public class CollectibleController {

	@Autowired
	private CollectibleService collectibleService;

	@Autowired
	private CollectibleTypeService collectibleTypeService;

	public CollectibleController() {
		super();
	}

	@PostMapping
	public Publisher<Collectible> addCollectible(@RequestBody Collectible c) {
		return collectibleService.createCollectible(c);
	}

	@PutMapping
	public Publisher<Collectible> updateCollectible(@RequestBody Collectible c) {
		return collectibleService.updateCollectible(c);
	}

	@PutMapping("/upgrade")
	public Mono<ResponseEntity<?>> upgradeCollectible(@RequestParam("collectibleId") UUID collectibleId) {
		return collectibleService.upgradeCollectible(collectibleId);
	}

	@GetMapping(params = { "filter" })
	public Publisher<Collectible> getCollectibles(@RequestParam("filter") String filter) {
		switch (filter.toLowerCase()) {
		case "all":
			return collectibleService.getAllCollectibles();
		default:
			return collectibleService.getCollectibles(filter);
		}
	}

	@GetMapping(params = { "id" })
	public Mono<CombinedCollectible> getCollectible(@RequestParam("id") String id) {
		Mono<Collectible> collectible = collectibleService.getCollectible(id);
		return collectible.flatMap(c -> collectibleTypeService.getCollectibleType(c.getTypeId()).map(t -> new CombinedCollectible(c, t)));
	}
	
	@PreAuthorize("hasAuthority('MODERATOR')")
	@DeleteMapping
	public Mono<ResponseEntity<?>> removeCollectible(@RequestParam("collectibleId") UUID collectibleId, @RequestParam("gamerId") UUID gamerId) {
		return collectibleService.removeCollectible(collectibleId, gamerId);
	}
	
	@PostMapping("/fusion")
	public Mono<ResponseEntity<?>> collectibleFusion(@RequestBody List<UUID> collectibleIds) {
		return collectibleService.collectibleFusion(collectibleIds);
	}
//			(@RequestParam("collectibleId1") UUID collectibleId1, 
//			@RequestParam("collectibleId2") UUID collectibleId2,
//			@RequestParam("collectibleId3") UUID collectibleId3,
//			@RequestParam("collectibleId4") UUID collectibleId4,
//			@RequestParam("collectibleId5") UUID collectibleId5)
}
