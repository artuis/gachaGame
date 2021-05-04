package com.group3.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group3.beans.Gamer;
import com.group3.data.ReactiveGamerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GamerServiceImpl implements GamerService {
	private static Logger log = LogManager.getLogger(GamerServiceImpl.class);
	@Autowired
	private ReactiveGamerRepository gamerRepo;

	@Override
	public Mono<Gamer> getGamer(int gamerId) {
		return gamerRepo.findById(gamerId);
	}

	@Override
	public Mono<Gamer> addGamer(Gamer gg) {
		log.trace("insert?");
		log.trace(gg.toString());
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
		// TODO Auto-generated method stub
		return null;
	}

}
