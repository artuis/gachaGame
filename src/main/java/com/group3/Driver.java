package com.group3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication
public class Driver {
	public static void main(String[] args) {
		SpringApplication.run(Driver.class, args);
	}
}
