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
	private EventRepository eventRepository;
	
	@Autowired
	private Event emptyEvent;
	
	private Logger log = LoggerFactory.getLogger(EventServiceImpl.class);
	
	public EventServiceImpl() {
		super();
	}
	
	public void setEventRepository(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}
	public void setEmptyEvent(Event event) {
		this.emptyEvent = event;
	}
	
	public Flux<Event> viewOngoingEvents(){
		return eventRepository.findAllByOngoing(true);
	}
	
	/* Creates an event which has a start and end time. 
	 * If those are null, return empty, otherwise 
	 * give the event a UUID eventId and insert in eventRepo.*/
	
	@Override
	public Mono<Event> createEvent(Event event) {
		if (event.getEventStart() == null || event.getEventEnd() == null) {
			log.trace("Invalid event start/end times");
			return Mono.empty();
		} else {
			event.setEventId(Uuids.timeBased());
			return eventRepository.insert(event);
		}
	}

	public Mono<Void> deleteEvent(UUID eventId) {
		return eventRepository.deleteById(eventId);
	}
	
	@Override
	public Mono<Event> updateEvent(Event event){
		return eventRepository.save(event);
		
	}
	
	public Mono<Event> findEventById(UUID eventId) {
		return eventRepository.findById(eventId);
	}

	@Override
	public Flux<Event> getEvents(){
		return eventRepository.findAll();
	}
	
}
