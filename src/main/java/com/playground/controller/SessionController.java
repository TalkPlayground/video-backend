package com.playground.controller;

import com.playground.dto.HandleRecordingDTO;
import com.playground.dto.SessionTranscriptFile;
import com.playground.dto.StatusChangeDTO;
import com.playground.dto.StoreSessionDTO;
import com.playground.service.SessionService;
import com.playground.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
public class SessionController {

    public static final String SUCCESS = "Success";
    public static final String FAILED = "Failed";
    @Autowired
    SessionService sessionService;

    @Autowired
    private WebClient.Builder webClientBuilder;


    //@PostMapping("/v1/user/email/verify")
    public ResponseEntity<Object> verifyEmailViaOTP(@RequestParam String email, @RequestParam String name) {
        boolean response = sessionService.verifyEmail(email, name);
        if (response) {
            return Response.generateResponse(HttpStatus.OK, response, SUCCESS, true);
        }
        return Response.generateResponse(HttpStatus.BAD_REQUEST, null, FAILED, false);
    }

    //@PostMapping("/v1/user/otp/verify")
    public ResponseEntity<Object> verifyOTP(@RequestParam String email, @RequestParam String otp) {
        boolean response = sessionService.verifyOtp(email, otp);
        if (response) {
            return Response.generateResponse(HttpStatus.OK, response, SUCCESS, true);
        }
        return Response.generateResponse(HttpStatus.EXPECTATION_FAILED, null, "Invalid OTP.", false);
    }

    @PostMapping("/v1/user/session/join")
    public ResponseEntity<Object> joinSession(@RequestParam String name, @RequestParam String email) {
        String response = sessionService.joinSession(name, email);
        if (Objects.nonNull(response)) {
            return Response.generateResponse(HttpStatus.OK, response, SUCCESS, true);
        }
        return Response.generateResponse(HttpStatus.EXPECTATION_FAILED, null, FAILED, false);
    }

    @PostMapping("/v1/user/session/store")
    public ResponseEntity<Object> storeSession(@RequestBody StoreSessionDTO data) {
        boolean response = sessionService.storeSession(data);
        if (response) {
            return Response.generateResponse(HttpStatus.OK, response, SUCCESS, true);
        }
        return Response.generateResponse(HttpStatus.EXPECTATION_FAILED, null, FAILED, false);
    }

    @PostMapping("/v1/user/session/recording")
    public ResponseEntity<Object> handleZoomSdkRecording(@RequestBody HandleRecordingDTO data) {
        boolean response = sessionService.handleRecordingStatus(data);
        if (response) {
            return Response.generateResponse(HttpStatus.OK, response, SUCCESS, true);
        }
        return Response.generateResponse(HttpStatus.EXPECTATION_FAILED, null, FAILED, false);
    }

    @PostMapping("/v1/user/session/transcript/files")
    public ResponseEntity<Object> insertTranscriptFiles(@RequestBody SessionTranscriptFile data) {
        boolean response = sessionService.insertTranscriptFiles(data);
        if (response) {
            return Response.generateResponse(HttpStatus.OK, response, SUCCESS, true);
        }
        return Response.generateResponse(HttpStatus.EXPECTATION_FAILED, null, FAILED, false);
    }

    @PostMapping("/v1/user/session/frontend/loggers")
    public ResponseEntity<Object> insertTranscriptFiles(@RequestParam String logs) {
        String response = sessionService.getlogs(logs);
        if (response != null) {
            return Response.generateResponse(HttpStatus.OK, response, SUCCESS, true);
        }
        return Response.generateResponse(HttpStatus.EXPECTATION_FAILED, null, FAILED, false);
    }

    @PostMapping("/v1/user/transcripts/delete/statusChange")
    public ResponseEntity<Object> updateTranscriptDeleteStatus(@RequestBody StatusChangeDTO status) {
        try {
            Object response = webClientBuilder.build().post().uri("http://52.42.41.198:8082/v1/user/transcripts/status/airtable").body(Mono.just(status), StatusChangeDTO.class).retrieve().bodyToMono(Object.class).block();
            if (response != null) {
                return Response.generateResponse(HttpStatus.OK, response, "Success", true);
            }
            return null;
        } catch (Exception e) {
            return Response.generateResponse(HttpStatus.EXPECTATION_FAILED, null, "Failed", false);
        }
    }
}
