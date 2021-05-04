package com.group3.services;

import java.util.List;
import java.util.concurrent.Future;

import com.group3.beans.Collectible;
import com.group3.beans.Encounter;

public interface EncounterService {

	public Future<?> runEncounter(List<Collectible> sent, Encounter journey);
	
}
