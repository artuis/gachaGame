package com.group3.services;

import java.util.List;
import java.util.concurrent.Future;

import com.group3.beans.Collectible;
import com.group3.beans.Encounter;

import reactor.core.publisher.Mono;

public interface EncounterService {

	public void setEncounter();
	
	public Mono<?> runEncounter(List<Collectible> sent, Encounter journey);
	
	
}
