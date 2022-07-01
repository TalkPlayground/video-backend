package com.playground.service;

import org.springframework.stereotype.Service;

import com.playground.dto.LoginDTO;

@Service
public interface UserService {

	Object login(LoginDTO payload);
	
}
