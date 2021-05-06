package com.group3.data;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.group3.beans.Gamer;
import com.group3.beans.Gamer.Role;

import reactor.core.publisher.Flux;

public interface GamerRepository extends ReactiveCassandraRepository<Gamer, Integer> {

	@AllowFiltering
	Flux<Gamer> findAllByRole(Role r);

}
