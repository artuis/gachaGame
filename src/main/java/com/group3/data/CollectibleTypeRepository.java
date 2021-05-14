package com.group3.data;

import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import com.group3.beans.CollectibleType;
import com.group3.beans.CollectibleType.Stage;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Primary
@Repository
public interface CollectibleTypeRepository extends ReactiveCassandraRepository<CollectibleType, String> {
	
	Flux<CollectibleType> findCollectiblesByStage(Stage stage);

	@AllowFiltering
	Mono<CollectibleType> findById(int id);
}
