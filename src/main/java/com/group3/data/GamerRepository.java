package com.group3.data;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.group3.beans.Gamer;

import reactor.core.publisher.Mono;

public interface GamerRepository extends ReactiveCassandraRepository<Gamer, Integer> {
	
	@AllowFiltering
	Mono<Gamer> findByUsername(String username);

}
