package com.playground.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.playground.service.SessionService;
import com.playground.utils.Response;

@RestController
public class SessionController {
	
	@Autowired SessionService sessionService;
	
	@PostMapping("/v1/user/email/verify")
	public ResponseEntity<Object> verifyEmailViaOTP(@RequestParam String email, @RequestParam String name){
		boolean response = sessionService.verifyEmail(email, name);
		if (response) {
			return Response.generateResponse(HttpStatus.OK, response, "Success", true);
		}
		return Response.generateResponse(HttpStatus.BAD_REQUEST, null, "Failed", false);
	}
	
	@PostMapping("/v1/user/otp/verify")
	public ResponseEntity<Object> verifyOTP(@RequestParam String email, @RequestParam String otp){
		boolean response = sessionService.verifyOtp(email, otp);
		if (response) {
			return Response.generateResponse(HttpStatus.OK, response, "Success", true);
		}
		return Response.generateResponse(HttpStatus.EXPECTATION_FAILED, null, "Invalid OTP.", false);
	}
	
	@PostMapping("/v1/user/session/join")
	public ResponseEntity<Object> joinSession(@RequestParam String name, @RequestParam String email, @RequestParam String sessionId){
		boolean response = sessionService.joinSession(name, email, sessionId);
		if (response) {
			return Response.generateResponse(HttpStatus.OK, response, "Success", true);
		}
		return Response.generateResponse(HttpStatus.EXPECTATION_FAILED, null, "Failed", false);
	}	
	
	@PostMapping("/v1/user/session/recording")
	public ResponseEntity<Object> handleZoomSdkRecording(@RequestParam String sessionId , @RequestParam boolean status){
		boolean response = sessionService.handleRecordingStatus(sessionId, status);
		if (response) {
			return Response.generateResponse(HttpStatus.OK, response, "Success", true);
		}
		return Response.generateResponse(HttpStatus.EXPECTATION_FAILED, null, "Failed", false);
	}	
}
