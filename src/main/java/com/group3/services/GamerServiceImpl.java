package com.group3.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.group3.beans.Gamer;
import com.group3.data.GamerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GamerServiceImpl implements GamerService {
	public static int bonusStrings = 1000;	// login bonus strings, global variable
	@Autowired
	private GamerRepository gamerRepo;
	
	private Logger log = LoggerFactory.getLogger(GamerServiceImpl.class);
	
	public void setGamerRepo(GamerRepository gamerRepo) {
		this.gamerRepo = gamerRepo;
	}

	@Override
	public Mono<Gamer> getGamer(UUID gamerId) {
		return gamerRepo.findById(gamerId);
	}

	@Override
	public Mono<Gamer> addGamer(Gamer gg) {
		if (gg.getUsername() == null) {
			log.trace("invalid username");
			return Mono.empty();
		}
		return gamerRepo.existsByUsername(gg.getUsername()).flatMap(exists -> {
			if (!exists.booleanValue()) {
				gg.setGamerId(Uuids.timeBased());
				gg.setRegistrationDate(Date.from(Instant.now()));
				List<Gamer.Role> perms = new ArrayList<>();
				if(gg.getRole() != null && gg.getRole().equals(Gamer.Role.MODERATOR)) {
					perms.add(Gamer.Role.MODERATOR);
				} else {gg.setRole(Gamer.Role.GAMER);}
				perms.add(Gamer.Role.GAMER);
				gg.setAuthorities(perms);
				gg.setLoginBonusCollected(false);
				if(gg.getRolls() == 0) {gg.setRolls(10);}
				if(gg.getDailyRolls() == 0) {gg.setDailyRolls(10);}
				if(gg.getStardust() == 0) {gg.setStardust(10);}
				if(gg.getStrings() == 0) {gg.setStrings(1000);}
				return gamerRepo.insert(gg);
			} else {
				return Mono.empty();
			}
		});
	}

	@Override
	public Mono<Gamer> updateGamer(Gamer gg) {
		return gamerRepo.findById(gg.getGamerId()).flatMap(gamer -> gamerRepo.save(gg));
	}
	
	@Override
	public Flux<Gamer> getGamers() {
		return gamerRepo.findAll();
	}
	
	@Override
	public Flux<Gamer> getGamersByRole(Gamer.Role role) {
		return gamerRepo.findAllByRole(role);
	}
	
	@Override
	public Flux<Gamer> getGamersByPvpScore() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
//	@Moderator	// only moderators can perform this action
	public Mono<Gamer> banGamer(UUID gamerId, long daysBanned) {
		return gamerRepo.findById(gamerId).flatMap(gg -> {			// find the gamer
			gg.setRole(Gamer.Role.BANNED);										// set gamer role to banned
			Set<Date> banDates;													// declare set of ban dates
			if(gg.getBanDates() == null) {										// check if the set is exists
				banDates = Collections.synchronizedSet(new HashSet<Date>());	// make the set if it doesn't exist, *synchronized*
			} else {
				banDates = gg.getBanDates();									// if the set exists, load it
			}
			Date banLiftDate = Date.from(Instant.now()							// calculate date of ban lift from now +
					.plus(daysBanned, ChronoUnit.DAYS));						// how many days the gamer is banned
			banDates.add(banLiftDate);											// add passed date to set
			gg.setBanDates(banDates);											// assign ban date set to gamer												
			return gamerRepo.save(gg);											// save updated gamer in repo
		});																		// return Mono<Gamer> to controller
	}
	
	//Only use this method for logging in, use findGamerByUsername if you need to query by username
	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return gamerRepo.findByUsername(username)
				.doOnSuccess(gamer -> {
					if (gamer != null) {
						log.debug("found a gamer: {}", gamer);
						gamer.setLastLogin(Date.from(Instant.now()));
						if(!gamer.isLoginBonusCollected()) {
							gamer.setStrings(gamer.getStrings()+bonusStrings);
							gamer.setLoginBonusCollected(true);
						}
						gamerRepo.save(gamer).subscribe();
					} 
				})
				.map(gamer -> gamer);
	}
	
	//This method will not update last login time but will find by username
	@Override
	public Mono<Gamer> findGamerByUsername(String username) {
		return gamerRepo.findByUsername(username);
	}
	
}
