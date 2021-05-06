package com.group3.data;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import com.group3.beans.CollectibleType;
import com.group3.beans.CollectibleType.Stage;

import reactor.core.publisher.Flux;

@Repository
public interface CollectibleRepository extends ReactiveCassandraRepository<CollectibleType, String> {
	
	Flux<CollectibleType> findCollectiblesByStage(Stage stage);
}
