package com.group3.schedule;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.group3.beans.Gamer;
import com.group3.data.GamerRepository;

@Component
@Order(1)
public class ScheduledTasks implements CommandLineRunner {
	@Autowired
	private GamerRepository gamerRepo;
	// ScheduledTasks will begin a thread and run after the Driver finishes initialization
	@Override
	public void run(String... args) throws Exception {
		// intentionally blank, perhaps log later
	}
	
	// documentation for @Scheduling: https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling
	// documentation for cron format: https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling-cron-expression
	@Scheduled(cron="0 0 0 * * *")								// scheduled for 00:00:00 daily
	public void dailyRollsReset() {
		gamerRepo.findAll().collectList().flatMap(gamers -> {	// get list of all gamers
			for(Gamer gg : gamers) {							// for each gamer in list
				gg.setDailyRolls(10);							// set daily rolls to 10
				gamerRepo.save(gg);								// save updated gamer
			}
			return null;										// no return needed for void
		});
	}
	
	@Scheduled(cron="0 0 0 * * *")
	public void dailyBanReset() {
		Date today = Date.from(Instant.now());
		gamerRepo.findAllByRole(Gamer.Role.BANNED).collectList().flatMap(gamers -> {
			for(Gamer gg : gamers) {
				Set<Date> banDates = gg.getBanDates();
				if(banDates == null || banDates.isEmpty()) {
					continue;
				} else {
					forEach(date : banDates) {
						if(date.) {
							continue;
						}
					}
				}
			}
			return null;
		});
	}

}
