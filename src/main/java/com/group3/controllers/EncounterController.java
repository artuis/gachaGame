package com.group3.controllers;

import java.util.List;
import java.util.UUID;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.group3.services.EncounterService;
import com.group3.services.GamerService;
import com.group3.util.JWTUtil;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/encounters")
public class EncounterController {

	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private EncounterService encounterService;
	@Autowired
	private GamerService gamerService;

	public EncounterController() {
		super();
	}

	// TODO Get: view available encounters

	@PreAuthorize("hasAuthority('GAMER')")
	@PostMapping
	public Mono<ResponseEntity<?>> startEncounter(@RequestParam("collectibleIDList") List<UUID> colIDs,
			@RequestParam("encounterID") UUID encounterID, ServerWebExchange exchange) {

		String token = exchange.getRequest().getCookies().getFirst("token").getValue();
		encounterService.setEncounter((UUID) jwtUtil.getAllClaimsFromToken(token).get("id"), colIDs, encounterID);

		// return something?
		return null;
	}

	@PreAuthorize("hasAuthority('GAMER')")
	@GetMapping("{gamerId}")
	public Publisher<?> viewRunningEncounters(ServerWebExchange exchange) {
		String token = exchange.getRequest().getCookies().getFirst("token").getValue();
		return encounterService.getRunningEncounters((UUID) jwtUtil.getAllClaimsFromToken(token).get("id"));
	}

	// TODO Get: receive reward if encounter is completed

}
