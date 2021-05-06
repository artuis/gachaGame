package com.group3.services;

import com.group3.beans.Gamer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GamerService {

	Mono<Gamer> getGamer(int gamerId);

	Mono<Gamer> addGamer(Gamer gg);

	Mono<Gamer> updateGamer(Gamer gg);

	Flux<Gamer> getGamers();

	Flux<Gamer> getGamersByPvpScore();

}
