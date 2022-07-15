package com.playground.controller;

import java.util.Objects;

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
	
	@PostMapping("/v1/user/email/otp")
	public ResponseEntity<Object> verifyEmailViaOTP(@RequestParam String email, @RequestParam String name){
		Object response = sessionService.verifyEmail(email, name);
		if (Objects.nonNull(response)) {
			return Response.generateResponse(HttpStatus.OK, response, "Success", true);
		}
		return Response.generateResponse(HttpStatus.BAD_REQUEST, null, "Failed", false);
	}
	
}
