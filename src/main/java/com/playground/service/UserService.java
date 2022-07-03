package com.playground.service;

import org.springframework.stereotype.Service;

import com.playground.dto.LoginDTO;
import com.playground.dto.SignupDTO;

@Service
public interface UserService {

	boolean login(LoginDTO payload);

	boolean signup(SignupDTO payload);
	
}
