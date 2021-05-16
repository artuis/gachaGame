package com.group3.controllers;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group3.beans.CollectibleType;
import com.group3.services.CollectibleTypeService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/collectibletypes")
public class CollectibleTypeController {
	@Autowired
	private CollectibleTypeService collectibleService;
	
	public CollectibleTypeController() {
		super();
	}
	
	@PreAuthorize("hasAuthority('MODERATOR')")
	@PostMapping
	public Publisher<CollectibleType> addCollectibleType(@RequestBody CollectibleType c) {
		return collectibleService.createCollectibleType(c);
	}
	
	@PreAuthorize("hasAuthority('MODERATOR')")
	@PutMapping
	public Mono<CollectibleType> updateCollectibleType(@RequestBody CollectibleType c) {
		return collectibleService.updateCollectibleType(c);
	}
	
	@PreAuthorize("hasAuthority('MODERATOR')")
	@DeleteMapping
	public Mono<Void> deleteCollectibleType(@RequestBody CollectibleType c) {
		return collectibleService.deleteCollectibleType(c);
	}

}
