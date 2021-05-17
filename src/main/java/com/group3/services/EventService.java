package com.group3.services;


import java.util.UUID;

import com.group3.beans.Event;
import com.group3.data.EventRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventService {

	Mono<Event> createEvent(Event event);

	Mono<Event> updateEvent(Event event);

	Flux<Event> getEvents();

	Flux<Event> viewOngoingEvents();

	Mono<Void> deleteEvent(UUID eventId);
	
	Mono<Event> findEventById(UUID eventId);

	void setEventRepository(EventRepository eventRepository);

	void setEmptyEvent(Event event);


}
