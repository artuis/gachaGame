package com.group3.services;

import java.util.UUID;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

import com.group3.beans.Gamer;
import com.group3.beans.Gamer.Role;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GamerService extends ReactiveUserDetailsService {

	Mono<Gamer> getGamer(UUID gamerId);

	Mono<Gamer> addGamer(Gamer gg);

	Mono<Gamer> updateGamer(Gamer gg);

	Flux<Gamer> getGamers();

	Flux<Gamer> getGamersByPvpScore();

	Mono<Gamer> banGamer(UUID gamerId, long daysBanned);

	Mono<Gamer> findGamerByUsername(String usernameFromToken);

	Flux<Gamer> getGamersByRole(Role role);

}
