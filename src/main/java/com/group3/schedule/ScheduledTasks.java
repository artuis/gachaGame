package com.group3.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.group3.beans.Gamer;
import com.group3.data.GamerRepository;

@Component
public class ScheduledTasks {
	@Autowired
	private GamerRepository gamerRepo;
	
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

}
