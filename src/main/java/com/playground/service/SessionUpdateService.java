package com.playground.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.playground.domain.Session;
import com.playground.dto.SessionPayload;
import com.playground.repository.SessionRepository;

@Service
public class SessionUpdateService {
	
	@Autowired SessionRepository sessionRepository;
	@Autowired SessionService sessionService;
	
	private static final Logger log = LoggerFactory.getLogger(SessionUpdateService.class);
	
	//@Scheduled(cron = "1 * * ? * *") //every second
	@Scheduled(cron = "0 */5 * ? * *") // every five minutes
	@Async
	public void updateSessionDetails() {
		try {
			List<Session> listOfSession = sessionRepository.findAllBySessionStatus("LIVE");
			listOfSession.parallelStream().forEach(data-> {
				SessionPayload sessionDetails = sessionService.checkLiveSessionDetails(data);
				if (Objects.nonNull(sessionDetails)) {
					if (sessionDetails.isHas_recording()) {
						sessionService.saveRecordingOfSession(data.getSessionUUID());
					};
				}
			});
			log.info("New job has executed at " + LocalDateTime.now());
		} catch (Exception e) {
			log.error("Issues generated in job execution time - " + e.getLocalizedMessage());
		}
	}
	                                                                                              
}
