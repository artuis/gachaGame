package com.group3.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group3.services.EncounterService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/encounters")
public class EncounterController {

	@Autowired
	private EncounterService encounterService;


	public EncounterController() {
		super();
	}

	@PostMapping
	public Mono<ResponseEntity<?>> startEncounter(
			@RequestParam("collectibleIDList") List<Integer> colIDs,
			@RequestParam("encounterID") Integer encounterID) {
		// TODO connect to EncounterServiceImpl
		// get current user
		// attach RewardToken to user's list of encounters
		encounterService.setEncounter(colIDs, encounterID);
		
		// return something?
		return null;
	}

}
