package com.group3.controllers;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group3.beans.Event;
import com.group3.services.EventService;
import com.group3.util.JWTUtil;

@RestController
@RequestMapping(value = "/events")
public class EventController {
	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private EventService eventService;
	
	@PreAuthorize("hasAuthority('MODERATOR')")
	@PutMapping
	public Publisher<Event> createEvent(@RequestBody Event event){
		return eventService.createEvent(event);
	}
	
	public EventController() {
		// TODO Auto-generated constructor stub
	}

}
