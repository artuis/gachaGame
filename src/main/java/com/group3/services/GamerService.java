package com.group3.services;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

import com.group3.beans.Gamer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GamerService extends ReactiveUserDetailsService {

	Mono<Gamer> getGamer(int gamerId);

	Mono<Gamer> addGamer(Gamer gg);

	Mono<Gamer> updateGamer(Gamer gg);

	Flux<Gamer> getGamers();

	Flux<Gamer> getGamersByPvpScore();

	Mono<Gamer> banGamer(int gamerId, Long daysBanned);

}
