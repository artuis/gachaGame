package com.group3.controllers;

import java.util.List;
import java.util.UUID;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.server.ServerWebExchange;

import com.group3.beans.Encounter;
import com.group3.beans.RewardToken;
import com.group3.services.EncounterService;
import com.group3.util.JWTUtil;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/encounters")
public class EncounterController {
	private Logger log = LoggerFactory.getLogger(EncounterController.class);
	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private EncounterService encounterService;

	public EncounterController() {
		super();
	}

	// TODO Get: view available encounters

	@PreAuthorize("hasAuthority('GAMER')")
	@PostMapping
	public Mono<ResponseEntity<RewardToken>> startEncounter(@RequestParam("collectibleIDList") List<UUID> collectibleIDList,
			@RequestParam("encounterID") UUID encounterID, ServerWebExchange exchange) {
		String token = exchange.getRequest().getCookies().getFirst("token").getValue();
		log.debug("EncounterID passing controller: {}", encounterID);
		log.debug("CollectibleList passing controller: {}", collectibleIDList);
		log.debug("String token passing controller: {}", token);
		log.debug("Gamer UUID passing controller: {}", UUID.fromString(jwtUtil.getAllClaimsFromToken(token).get("id").toString()));
		RewardToken rewardToken = encounterService.setEncounter(UUID.fromString(jwtUtil
				.getAllClaimsFromToken(token).get("id").toString()), 
				collectibleIDList, encounterID).block();
				return Mono.just(ResponseEntity.ok().body(rewardToken));
	}

	@PreAuthorize("hasAuthority('GAMER')")
	@GetMapping("{gamerId}")
	public Publisher<?> viewRunningEncounters(ServerWebExchange exchange) {
		String token = exchange.getRequest().getCookies().getFirst("token").getValue();
		return encounterService.getRunningEncounters((UUID) jwtUtil.getAllClaimsFromToken(token).get("id"));
	}

	@PreAuthorize("hasAuthority('MODERATOR')")
	@PostMapping("/createEncounter")
	public Mono<ResponseEntity<Encounter>> createEncounterTemplate(@RequestBody Encounter encounter) {
		return encounterService.createEncounterTemplate(encounter).thenReturn(ResponseEntity.ok(encounter));
	}
	
	@PreAuthorize("hasAuthority('MODERATOR')")
	@DeleteMapping
	public Mono<ResponseEntity<?>> deleteEncounterTemplate(@RequestParam UUID encounterID) {
		return encounterService.deleteEncounterTemplate(encounterID).thenReturn(ResponseEntity.ok().build());
	}
	
	@PreAuthorize("hasAuthority('MODERATOR')")
	@PutMapping("{encounterID}")
	public Mono<ResponseEntity<Encounter>> updateEncounterTemplate(@RequestBody Encounter encounter) {
		return encounterService.updateEncounterTemplate(encounter).thenReturn(ResponseEntity.ok(encounter));
	}
}
