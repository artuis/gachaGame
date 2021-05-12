package com.group3.services;


import com.group3.beans.Event;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventService {

	Mono<Event> createEvent(Event event);

	Mono<Event> updateEvent(Event event);

	Flux<Event> getEvents();

	Flux<Event> viewOngoingEvents();

}
