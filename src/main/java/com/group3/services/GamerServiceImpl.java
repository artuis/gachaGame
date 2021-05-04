package com.group3.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.beans.Gamer;
import com.group3.data.GamerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GamerServiceImpl implements GamerService {
	private static Logger log = LogManager.getLogger(GamerServiceImpl.class);
	@Autowired
	private GamerRepository gamerRepo;

	@Override
	public Mono<Gamer> getGamer(double gamerId) {
		return gamerRepo.findById(gamerId);
	}

	@Override
	public Mono<Gamer> addGamer(Gamer gg) {
		return gamerRepo.insert(gg);
	}

	@Override
	public Mono<Gamer> updateGamer(Gamer gg) {
		return gamerRepo.save(gg);
	}

	@Override
	public Flux<Gamer> getGamers() {
		return gamerRepo.findAll();
	}

	@Override
	public Flux<Gamer> getGamersByPvpScore() {
		// there is probably a better way to do this, admittedly, besides mimicking the getPlayersByScore method
		return gamerRepo.findAll().sort((gg1,gg2)->Integer.parseInt(Double.toString(gg2.getPvpScore() - gg1.getPvpScore()))).map(gg -> {
			return gg;
		});
	}

}
