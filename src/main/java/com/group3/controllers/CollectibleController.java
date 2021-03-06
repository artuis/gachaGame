package com.group3.controllers;

import java.util.ArrayList;
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
	private Collectible emptyCollectible;
	
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
	public Mono<ResponseEntity<Object>> upgradeCollectible(@RequestParam("collectibleId") UUID collectibleId) {
		return collectibleService.upgradeCollectible(collectibleId);
	}

	@GetMapping(params = { "filter" })
	public Publisher<Collectible> getCollectibles(@RequestParam("filter") String filter) {
		if (filter.equalsIgnoreCase("all")) {
			return collectibleService.getAllCollectibles();
		} else {
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
	public Mono<ResponseEntity<Object>> removeCollectible(@RequestParam("collectibleId") UUID collectibleId, @RequestParam("gamerId") UUID gamerId) {
		return collectibleService.removeCollectible(collectibleId, gamerId);
	}
	
	@PostMapping("/fusion")
	public Mono<ResponseEntity<Collectible>> collectibleFusion(
			@RequestParam("collectibleId1") UUID collectibleId1, 
			@RequestParam("collectibleId2") UUID collectibleId2,
			@RequestParam("collectibleId3") UUID collectibleId3,
			@RequestParam("collectibleId4") UUID collectibleId4,
			@RequestParam("collectibleId5") UUID collectibleId5) {
		List<UUID> collectibleIds = new ArrayList<>();
		collectibleIds.add(collectibleId1);
		collectibleIds.add(collectibleId2);
		collectibleIds.add(collectibleId3);
		collectibleIds.add(collectibleId4);
		collectibleIds.add(collectibleId5);
		return collectibleService.collectibleFusion(collectibleIds).defaultIfEmpty(emptyCollectible).map(collectible -> {
			if(collectible.getId() != null) {
				return ResponseEntity.ok(collectible);
			}
			return ResponseEntity.notFound().build();
		});
	}
}
