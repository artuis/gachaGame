package com.group3.data;

import java.util.UUID;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import com.group3.beans.Encounter;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface EncounterRepository extends ReactiveCassandraRepository<Encounter, UUID> {

	@AllowFiltering
	Flux<Encounter> findAllByLevel(int level);
	
	@AllowFiltering
	Mono<Encounter> findByEncounterID(UUID encounterID);

}
