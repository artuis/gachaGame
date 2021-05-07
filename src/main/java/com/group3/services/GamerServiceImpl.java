package com.group3.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.group3.beans.Gamer;
import com.group3.data.GamerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GamerServiceImpl implements GamerService {
	@Autowired
	private GamerRepository gamerRepo;

	@Override
	public Mono<Gamer> getGamer(int gamerId) {
		return gamerRepo.findById(gamerId);
	}

	@Override
	public Mono<Gamer> addGamer(Gamer gg) {
		gg.setRegistrationDate(Date.from(Instant.now()));
		
		List<Gamer.Role> perms = new ArrayList<Gamer.Role>();
		if(gg.getRole() != null && gg.getRole().equals(Gamer.Role.MODERATOR)) {
			perms.add(Gamer.Role.MODERATOR);
		} else {gg.setRole(Gamer.Role.GAMER);}
		perms.add(Gamer.Role.GAMER);
		gg.setAuthorities(perms);
		
		if(gg.getRolls() == 0) {gg.setRolls(10);}
		if(gg.getDailyRolls() == 0) {gg.setDailyRolls(10);}
		if(gg.getStardust() == 0) {gg.setStardust(10);}
		if(gg.getStrings() == 0) {gg.setStrings(1000);}
		
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

	@Override
//	@Moderator	// only moderators can perform this action
	public Mono<Gamer> banGamer(int gamerId, long daysBanned) {
		Mono<Gamer> gamer = gamerRepo.findById(gamerId).map(gg -> {				// find the gamer
			gg.setRole(Gamer.Role.BANNED);										// set gamer role to banned
			Set<Date> banDates;													// declare set of ban dates
			if(gg.getBanDates() == null) {										// check if the set is exists
				banDates = Collections.synchronizedSet(new HashSet<Date>());	// make the set if it doesn't exist, *synchronized*
			} else {
				banDates = gg.getBanDates();									// if the set exists, load it
			}
			Date banLiftDate = Date.from(Instant.now()							// calculate date of ban lift from today +
					.plus(daysBanned, ChronoUnit.DAYS));						// how many days the gamer is banned
			banDates.add(banLiftDate);											// add passed date to set
			gg.setBanDates(banDates);											// assign ban date set to gamer
			gamerRepo.save(gg);													// save updated gamer in repo
			return gg;															// return transormed gamer to mono
		});
		return gamer;															// return Mono<Gamer> to controller
	}

	public Mono<UserDetails> findByUsername(String username) throws UsernameNotFoundException {
		return gamerRepo.findByUsername(username)
				.doOnSuccess(gamer -> {
					gamer.setLastLogin(Date.from(Instant.now()));
					gamerRepo.save(gamer);
				})
				.map(gamer -> gamer);
	}
}
