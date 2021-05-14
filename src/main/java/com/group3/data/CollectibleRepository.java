package com.group3.data;

import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.group3.beans.Collectible;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Primary
public interface CollectibleRepository extends ReactiveCassandraRepository<Collectible, UUID>{

	@AllowFiltering
	Flux<Collectible> findByGamerId(UUID uuid);
	

	@AllowFiltering
	Mono<Collectible> findById(UUID uuid);

}
