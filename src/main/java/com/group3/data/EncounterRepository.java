package com.group3.data;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.group3.beans.Encounter;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EncounterRepository extends ReactiveCassandraRepository<Encounter, /*PRIMARY KEY DATATYPE REPLACES String PLACEHOLDER*/ String> {

	@AllowFiltering
	Flux<Encounter> findAllByLevel(int level);
	@AllowFiltering
	Mono<Encounter> findById(UUID encID);

}
