package com.group3.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.group3.beans.Event;
import com.group3.data.EventRepository;

import reactor.core.publisher.Mono;

public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepository eventRepo;
	
	private Logger log = LoggerFactory.getLogger(GamerServiceImpl.class);
	
	public EventServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	/*do we want to be able to make more events or are we 
	 * good with just double strings and 
	 * turn it on and off/schedule it*/
	@Override
	public Mono<Event> createEvent(Event event) {
		//verify that event has start and end time before creating
		if (event.getEventStart() == null | event.getEventEnd() == null) {
			log.trace("invalid username");
			return Mono.empty();
		}
		//TODO
		return null;
	}

}
