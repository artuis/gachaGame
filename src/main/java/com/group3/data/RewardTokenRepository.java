package com.group3.data;

import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import com.group3.beans.RewardToken;

import reactor.core.publisher.Flux;
@Primary
@Repository
public interface RewardTokenRepository extends ReactiveCassandraRepository<RewardToken, UUID> {

	@AllowFiltering
	Flux<RewardToken> findAllByGamerID(UUID gamerID);

	@AllowFiltering
	Flux<RewardToken> findAllByEncounterComplete(boolean encounterComplete);
}
