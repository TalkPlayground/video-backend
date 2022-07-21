package com.playground.service;

import org.springframework.stereotype.Service;

import com.playground.dto.SessionPayload;
import com.playground.dto.SessionTranscriptFile;

@Service
public interface SessionService {

	boolean verifyEmail(String email, String name);
	boolean verifyOtp(String email, String otp);
	boolean joinSession(String name, String email, String sessionId);
	public SessionPayload fetchSessionDetails(String sessionId);
	public void saveRecordingOfSession(String sessionId);
	public boolean handleRecordingStatus(String sessionId, boolean status);
	boolean insertTranscriptFiles(SessionTranscriptFile data);
	boolean updateAwsUrlInRecording(String recordingId, String awsUrl);

}
