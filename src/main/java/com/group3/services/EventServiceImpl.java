package com.group3.services;

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
		// TODO Auto-generated constructor stub
	}
	
	public Flux<Event> viewOngoingEvents(){
		return eventRepo.findAllByOngoing(true);
	}

	/*do we want to be able to make more events or are we 
	 * good with just double strings and 
	 * turn it on and off/schedule it*/
	@Override
	public Mono<Event> createEvent(Event event) {
		//verify that event has start and end time before creating
		if (event.getEventStart() == null | event.getEventEnd() == null) {
			log.trace("invalid event start/end times");
			return Mono.empty();
		}
		if(event != null) {
			event.setEventId(Uuids.timeBased());
		}
		return eventRepo.insert(event);
	}

	@Override
	public Mono<Event> updateEvent(Event event){
		return eventRepo.save(event);
		
	}

	@Override
	public Flux<Event> getEvents(){
		return eventRepo.findAll();
	}
	
}
