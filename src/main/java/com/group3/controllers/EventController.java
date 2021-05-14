package com.group3.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group3.beans.Event;
import com.group3.services.EventService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Primary
@RestController
@RequestMapping(value = "/events")
public class EventController {

	@Autowired
	private EventService eventService;
	@Autowired
	private Event emptyEvent;
	
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}
	public void setEmptyEvent(Event event) {
		this.emptyEvent = event;
	}
	
	@PreAuthorize("hasAuthority('MODERATOR')")
	@PostMapping
	public Mono<ResponseEntity<Event>> createEvent(@RequestBody Event event){
		return eventService.createEvent(event).defaultIfEmpty(emptyEvent)
				.map(e -> {
					if (e.getEventId() == null) {
						return ResponseEntity.badRequest().build();
					}
					return ResponseEntity.status(201).body(e);
				});
	}
	
	@PreAuthorize("hasAuthority('MODERATOR')")
	@DeleteMapping("{UUID}")
	public Mono<ResponseEntity<Object>> deleteEvent(@PathVariable("UUID") UUID eventId) {
		return eventService.deleteEvent(eventId).thenReturn(ResponseEntity.noContent().build());
	}
	
	@GetMapping
	public Flux<Event> viewOngoingEvents(){
		return eventService.viewOngoingEvents();
		
	}
	
	@GetMapping("/allEvents")
	public Flux<Event> viewAllEvents() {
		return eventService.getEvents();
	}
}
