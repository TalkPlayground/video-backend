package com.playground.service;

import org.springframework.stereotype.Service;

import com.playground.dto.SessionPayload;

@Service
public interface SessionService {

	boolean verifyEmail(String email, String name);
	boolean verifyOtp(String email, String otp);
	boolean joinSession(String name, String email, String sessionId);
	public SessionPayload fetchSessionDetails(String sessionId);
	public void saveRecordingOfSession(String sessionId);

}
