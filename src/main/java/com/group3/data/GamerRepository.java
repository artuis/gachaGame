package com.group3.data;

import java.util.List;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import com.group3.beans.Gamer;
import com.group3.beans.Gamer.Role;

@Repository
public interface GamerRepository extends ReactiveCassandraRepository<Gamer, Integer> {

	@AllowFiltering
	List<Gamer> findAllByRole(Role r);

}
