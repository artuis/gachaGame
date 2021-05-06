package com.group3.controllers;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group3.beans.CollectibleType;
import com.group3.services.CollectibleService;

@RestController
@RequestMapping(value = "/collectibletypes")
public class CollectibleController {
	
	@Autowired
	private CollectibleService collectibleService;
	
	public CollectibleController() {
		super();
	}
	
	@PostMapping
	public Publisher<CollectibleType> addCollectible(@RequestBody CollectibleType c) {
		return collectibleService.createCollectible(c);
	}
	
	@PutMapping
	public Publisher<CollectibleType> updateCollectible(@RequestBody CollectibleType c) {
		return collectibleService.updateCollectible(c);
	}

}
