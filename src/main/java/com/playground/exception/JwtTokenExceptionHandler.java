package com.playground.exception;

import com.playground.utils.Response;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class JwtTokenExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleMalformedJwtException(MalformedJwtException e, HttpServletRequest request) {
        return Response.generateResponse(HttpStatus.FORBIDDEN, null, e.getMessage(), false);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleSignatureException(SignatureException e, HttpServletRequest request) {
        return Response.generateResponse(HttpStatus.FORBIDDEN, null, e.getMessage(), false);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException e, HttpServletRequest request) {
        return Response.generateResponse(HttpStatus.FORBIDDEN, null, e.getMessage(), false);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleUnsupportedJwtException(UnsupportedJwtException e, HttpServletRequest request) {
        return Response.generateResponse(HttpStatus.FORBIDDEN, null, e.getMessage(), false);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handlellegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        return Response.generateResponse(HttpStatus.FORBIDDEN, null, e.getMessage(), false);
    }

}
