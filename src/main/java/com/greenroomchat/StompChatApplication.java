package com.greenroomchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;

@SpringBootApplication
public class StompChatApplication {
	@Bean
	public HashSet<String> userList() {return new HashSet<>();}

	public static void main(String[] args) {
		SpringApplication.run(StompChatApplication.class, args);
	}
}