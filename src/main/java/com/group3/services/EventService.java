package com.group3.services;

import org.reactivestreams.Publisher;

import com.group3.beans.Event;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventService {

	Publisher<Event> createEvent(Event event);

	Mono<Event> updateEvent(Event event);

	Flux<Event> getEvents();

}
