package com.group3.services;

import org.reactivestreams.Publisher;

import com.group3.beans.Event;

public interface EventService {

	Publisher<Event> createEvent(Event event);

}
