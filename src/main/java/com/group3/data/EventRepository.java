package com.group3.data;



import java.util.UUID;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.group3.beans.Event;

import reactor.core.publisher.Flux;


public interface EventRepository extends ReactiveCassandraRepository<Event, /*PRIMARY KEY DATATYPE REPLACES String PLACEHOLDER*/ UUID> {
<<<<<<< HEAD
	
	@AllowFiltering
	Flux<Event> findAllByOngoing(boolean isOngoing);
=======
>>>>>>> 115d6d591b1c681e84c33041f3b7b17bb9e0c62d

	@AllowFiltering
	Flux<Event> findAllByOngoing(boolean ongoing);
}
