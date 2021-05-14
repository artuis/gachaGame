package com.group3.data;

import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.group3.beans.Gamer;
import com.group3.beans.Gamer.Role;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Primary
public interface GamerRepository extends ReactiveCassandraRepository<Gamer, UUID> {
	
	@AllowFiltering
	Flux<Gamer> findAllByRole(Role r);
	@AllowFiltering
	Mono<Gamer> findByUsername(String username);
	@AllowFiltering
	Mono<Boolean> existsByUsername(String username);

}
