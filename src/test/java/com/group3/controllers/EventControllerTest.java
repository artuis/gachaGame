package com.group3.controllers;

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
import com.group3.services.EventService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
//@WebFluxTest(controllers = EventController.class)
//@Import(EventService.class)
class EventControllerTest {
	@TestConfiguration
	static class Configuration {
		
		@Bean
		public EventController getEventController(EventService eventService, Event event) {
			EventController ec = new EventController();
			ec.setEventService(eventService);
			ec.setEmptyEvent(event);
			return ec;
		}
		@Bean
		public EventService getEventService() {
			return Mockito.mock(EventService.class);
		}
		@Bean
		public Event getEvent() {
			return Mockito.mock(Event.class);
		}
	}
	@Autowired
	private EventService eventService;
	
	@Autowired
	private EventController eventController;
	
	@Test
	void testCreateEventCreatesEvent() {
		Event event = new Event();
		UUID id = UUID.randomUUID();
		event.setEventId(id);
		event.setEventType(Event.Type.DOUBLESTRINGS);
		event.setOngoing(false);
		event.setEventStart(null);
		event.setEventEnd(null);
		
		Mockito.when(eventService.createEvent(event)).thenReturn(Mono.just(event));
		
		Mono<ResponseEntity<Event>> result = eventController.createEvent(event);
		
		StepVerifier.create(result).expectNext(ResponseEntity.status(201).body(event)).verifyComplete();
	}
//	
//	@MockBean
//	EventRepository repository;
//	
//	@Autowired
//	private WebTestClient webClient;
//	@SuppressWarnings("deprecation")
//	@Test
//	void testCreateEvent() {
//		Event event = new Event();
//		UUID id = UUID.randomUUID();
//		event.setEventId(id);
//		event.setEventType(Event.Type.DOUBLESTRINGS);
//		event.setOngoing(false);
//		event.setEventStart(null);
//		event.setEventEnd(null);
//		
//		Mockito.when(repository.save(event)).thenReturn(Mono.just(event));
//		
//		webClient.post().uri("/events")
//        	.contentType(MediaType.APPLICATION_JSON)
//        	.body(BodyInserters.fromObject(event))
//        	.exchange()
//        	.expectStatus().isCreated();
//		
//		Mockito.verify(repository, Mockito.times(1)).save(event);
//	}
//	
//	@Test
//    void testDeleteEvent() {
//        Mono<Void> voidReturn  = Mono.empty();
//        Mockito
//            .when(repository.deleteById(UUID.randomUUID()))
//            .thenReturn(voidReturn);
// 
//        webClient.delete().uri("/events/{UUID}")
//            .exchange()
//            .expectStatus().isOk();
//    }
}
