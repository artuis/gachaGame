package com.group3.services;

import java.sql.Date;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.group3.beans.Event;
import com.group3.data.EventRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class EventServiceTest {

	@TestConfiguration
	static class Configuration {
		@Bean
		public EventService getEventService(EventRepository eventRepository, Event event) {
			EventService es = new EventServiceImpl();
			es.setEventRepository(eventRepository);
			es.setEmptyEvent(event);
			return es;
		}
		@Bean
		public EventRepository getEventRepository() {
			return Mockito.mock(EventRepository.class);
		}
		@Bean
		public Event getEvent() {
			return Mockito.mock(Event.class);
		}
	}
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private EventService eventService;
	
	@Test
	void createEventCreatesEvent() {
		Date startDate = new Date(100000);
		Date endDate = new Date(1000001);
		Event event = new Event();
		event.setEventStart(startDate);
		event.setEventEnd(endDate);
		
		Mockito.when(eventRepository.insert(event)).thenReturn(Mono.just(event));
		
		Mono<Event> result = eventService.createEvent(event);
		
		StepVerifier.create(result).expectNext(event).verifyComplete();
	}
	
	@Test
	void deleteEventDeletesEvent() {
		UUID id = UUID.randomUUID();
		
		Mockito.when(eventRepository.deleteById(id)).thenReturn(Mono.justOrEmpty(null));
		
		@SuppressWarnings("unchecked")
		Mono<Void> result = (Mono<Void>) eventService.deleteEvent(id);
		StepVerifier.create(result).expectNext().verifyComplete();
	}
	
	@Test
	void updateEventUpdatesEvent() {
		Event event = new Event();
		
		Mockito.when(eventRepository.save(event)).thenReturn(Mono.just(event));
		
		Mono<Event> result = eventService.updateEvent(event);
		
		StepVerifier.create(result).expectNext(event).verifyComplete();
	}
	
	@Test
	void viewOngoingEventsReturnsAllOngoingEvents() {
		Event event1 = new Event();
		Event event2 = new Event();
		event1.setOngoing(true);
		event2.setOngoing(false);
		
		Mockito.when(eventRepository.findAllByOngoing(true)).thenReturn(Flux.just(event1));
		
		Flux<Event> result = eventService.viewOngoingEvents();
		
		StepVerifier.create(result).expectNext(event1).verifyComplete();
	}
}
