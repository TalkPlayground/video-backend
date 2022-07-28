package com.playground.exception;

import java.time.format.DateTimeParseException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.playground.utils.Response;

@ControllerAdvice
public class ApplicationExceptionHandler {
	
	@ExceptionHandler
	public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException e, HttpServletRequest request) {
		return Response.generateResponse(HttpStatus.NOT_ACCEPTABLE, null, e.getMessage(), false);
	}
	
	@ExceptionHandler
	public ResponseEntity<Object> handleRuntimeJsonMappingException(RuntimeJsonMappingException e, HttpServletRequest request) {
		return Response.generateResponse(HttpStatus.NOT_ACCEPTABLE, null, e.getMessage(), false);
	}
	
	@ExceptionHandler
	public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException e, HttpServletRequest request) {
		return Response.generateResponse(HttpStatus.NOT_ACCEPTABLE, null, e.getMessage(), false);
	}
	
	@ExceptionHandler
	public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
		return Response.generateResponse(HttpStatus.OK, null, "User has already registered with this email.", false);
	}
	
	@ExceptionHandler
	public ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException e, HttpServletRequest request) {
		return Response.generateResponse(HttpStatus.BAD_REQUEST, null, e.getLocalizedMessage(), false);
	}
	
}
