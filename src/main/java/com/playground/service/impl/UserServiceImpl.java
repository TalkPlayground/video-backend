package com.playground.service.impl;

import org.springframework.stereotype.Service;

import com.playground.dto.LoginDTO;
import com.playground.service.UserService;
@Service
public class UserServiceImpl implements UserService {

	@Override
	public Object login(LoginDTO payload) {
		return payload;
	}

}
