package com.playground.service;

import org.springframework.stereotype.Service;

@Service
public interface SessionService {

	boolean verifyEmail(String email, String name);
	boolean verifyOtp(String email, String otp);

}
