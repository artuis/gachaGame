package com.group3.data;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import com.group3.beans.Collectible;
import com.group3.beans.Collectible.Stage;

import reactor.core.publisher.Flux;

@Repository
public interface CollectibleRepository extends ReactiveCassandraRepository<Collectible, String> {
	
	Flux<Collectible> findCollectiblesByStage(Stage stage);
}
