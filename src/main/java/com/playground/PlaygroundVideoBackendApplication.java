package com.playground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.playground.domain.OtpSessions;

@SpringBootApplication
public class PlaygroundVideoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaygroundVideoBackendApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public OtpSessions otpSessions() {
		return new OtpSessions();
	}
}
