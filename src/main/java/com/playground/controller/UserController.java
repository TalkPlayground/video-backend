package com.playground.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.playground.dto.LoginDTO;
import com.playground.service.UserService;
import com.playground.utils.Response;

@RestController
public class UserController {
	
	@Autowired UserService userService;
	
	@PostMapping("/v1/user/login")
	public ResponseEntity<Object> login(@RequestBody LoginDTO payload){
		Object response = userService.login(payload);
		if (Objects.nonNull(response)) {
			return Response.generateResponse(HttpStatus.OK, response, "message", true);
		}
		return Response.generateResponse(HttpStatus.NOT_ACCEPTABLE, null, "Invalid username or password", false);
	}
}
