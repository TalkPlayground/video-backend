package com.playground.service;

import com.playground.domain.Session;
import com.playground.dto.SessionPayload;
import com.playground.repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class SessionUpdateService {

    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    SessionService sessionService;

    private static final Logger log = LoggerFactory.getLogger(SessionUpdateService.class);

    @Scheduled(cron = "0 */10 * ? * *") // every ten minutes
    @Async
    public void updateSessionDetails() {
        try {
            List<Session> listOfSession = sessionRepository.findAllBySessionStatus("LIVE");
            for (Session session : listOfSession) {
                log.info("save Recording session 43 : {}", session);
                SessionPayload sessionPayload = sessionService.checkLiveSessionDetails(session);
                log.info("save Recording session 45 : {}", sessionPayload);
                if (Objects.nonNull(sessionPayload) && sessionPayload.isHas_recording()) {
                    sessionService.saveRecordingOfSession(session.getSessionUUID());
                    log.info("save Recording session 47 : {}", session.getSessionUUID());
                }
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm:ss");
            log.info("New job has executed at " + LocalDateTime.now().format(formatter));
        } catch (Exception e) {
            log.error("Issues generated in job execution time - " + e.getLocalizedMessage());
        }
    }
}
