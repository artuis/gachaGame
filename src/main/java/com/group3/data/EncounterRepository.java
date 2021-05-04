package com.group3.data;

import java.util.List;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.group3.beans.Encounter;

public interface EncounterRepository extends ReactiveCassandraRepository<Encounter, /*PRIMARY KEY DATATYPE REPLACES String PLACEHOLDER*/ String> {


}
