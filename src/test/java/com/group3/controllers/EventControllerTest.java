package com.group3.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.group3.beans.Event;
import com.group3.data.EventRepository;
import com.group3.services.EventService;

import reactor.core.publisher.Mono;

@WebFluxTest(controllers = EventController.class)
@Import(EventService.class)
public class EventControllerTest {
	
	@MockBean
	EventRepository repository;
	
	@Autowired
	private WebTestClient webClient;
	@Test
	void testCreateEvent() {
		Event event = new Event();
		/* UUID id = f722e9ae-b37f-11eb-8529-0242ac130003;
		event.setEventId(id); */
		event.setEventType(Event.Type.DOUBLESTRINGS);
		event.setOngoing(false);
		event.setEventStart(null);
		event.setEventEnd(null);
		
		Mockito.when(repository.save(event)).thenReturn(Mono.just(event));
		
		webClient.post()
        	.contentType(MediaType.APPLICATION_JSON)
        	.body(BodyInserters.fromObject(event))
        	.exchange()
        	.expectStatus().isCreated();
		
		Mockito.verify(repository, times(1)).save(event);
	}
	
	@Test
    void testDeleteEvent() {
        Mono<Void> voidReturn  = Mono.empty();
        Mockito
            .when(repository.deleteById(/*random UUID goes here?*/))
            .thenReturn(voidReturn);
 
        webClient.delete()
            .exchange()
            .expectStatus().isOk();
    }
}
