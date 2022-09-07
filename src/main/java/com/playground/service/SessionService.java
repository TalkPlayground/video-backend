package com.playground.service;

import org.springframework.stereotype.Service;

import com.playground.domain.Session;
import com.playground.dto.HandleRecordingDTO;
import com.playground.dto.SessionPayload;
import com.playground.dto.SessionTranscriptFile;
import com.playground.dto.StoreSessionDTO;

@Service
public interface SessionService {

	boolean verifyEmail(String email, String name);
	boolean verifyOtp(String email, String otp);
	String joinSession(String name, String email);
	public void saveRecordingOfSession(String sessionId);
	public boolean handleRecordingStatus(HandleRecordingDTO data);
	boolean insertTranscriptFiles(SessionTranscriptFile data);
	SessionPayload checkLiveSessionDetails(Session session);
	SessionPayload checkPastSessionDetails(Session session);
	boolean storeSession(StoreSessionDTO data);

//	String getlogs(String logs);

}
