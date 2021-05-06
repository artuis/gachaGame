package com.group3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;
import com.group3.schedule.ScheduledTasks;


@SpringBootApplication
@EnableScheduling
public class Driver {
	public static void main(String[] args) {
		SpringApplication.run(Driver.class, args);
		SpringApplication.run(ScheduledTasks.class);
	}
}
