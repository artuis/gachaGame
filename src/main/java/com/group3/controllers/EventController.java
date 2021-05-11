package com.group3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group3.beans.Event;
import com.group3.services.EventService;
import com.group3.util.JWTUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/events")
public class EventController {

	@Autowired
	private EventService eventService;
	@Autowired
	private Event emptyEvent;
	
	@PreAuthorize("hasAuthority('MODERATOR')")
	@PutMapping
	public Mono<ResponseEntity<Event>> createEvent(@RequestBody Event event){
		return eventService.createEvent(event).defaultIfEmpty(emptyEvent)
				.map(e -> {
					if (emptyEvent.getEventId() == null) {
						return ResponseEntity.badRequest().build();
					}
					return ResponseEntity.status(201).body(e);
				});
	}
	
	@GetMapping("/view")
	public Flux<Event> viewOngoingEvents(){
		return eventService.viewOngoingEvents();
		
	}
}
