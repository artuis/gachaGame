package com.group3.services;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.group3.beans.Event;
import com.group3.data.EventRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepository eventRepo;
	
	private Logger log = LoggerFactory.getLogger(GamerServiceImpl.class);
	
	public EventServiceImpl() {
		super();
	}
	
	public Flux<Event> viewOngoingEvents(){
		return eventRepo.findAllByOngoing(true);
	}
	
	/* Creates an event which has a start and end time. 
	 * If those are null, return empty, otherwise 
	 * give the event a UUID eventId and insert in eventRepo.*/
	
	@Override
	public Mono<Event> createEvent(Event event) {
		if (event.getEventStart() == null || event.getEventEnd() == null) {
			return Mono.empty();
		}
		if(event != null) {
			event.setEventId(Uuids.timeBased());
		}
		return eventRepo.insert(event);
	}

	public Mono<Void> deleteEvent(UUID eventId) {
		return eventRepo.deleteById(eventId);
	}
	
	@Override
	public Mono<Event> updateEvent(Event event){
		return eventRepo.save(event);
		
	}
	
	public Mono<Event> findEventById(UUID eventId) {
		return eventRepo.findById(eventId);
	}

	@Override
	public Flux<Event> getEvents(){
		return eventRepo.findAll();
	}
	
}
