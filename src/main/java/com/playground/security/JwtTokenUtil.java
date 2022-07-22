package com.playground.security;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.playground.domain.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil{

	@Value("${jwt.secret.key}")
	private String jwtSecretKey;
	
	private long jwtTokenValidity=8L;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public String generateToken(Member user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("name", user.getFullName());
		claims.put("userId", user.getMemberUUID());
		return Jwts.builder().setSubject(user.getEmail())
				.addClaims(claims).signWith(SignatureAlgorithm.HS256, jwtSecretKey.getBytes())
				.setExpiration(new Date(System.currentTimeMillis() + convertHourToMillis(jwtTokenValidity))).compact();
	}
	
	
	public boolean validateToken(String token) {
		return isValidSignedToken(token) && !isTokenExpired(token);
	}
	
	public boolean isValidSignedToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecretKey.getBytes()).isSigned(token);
	}	
	
	public boolean isTokenExpired(String token) {
		Date expirationTime = getClaimFromToken(token, Claims::getExpiration);
		return expirationTime.before(new Date(System.currentTimeMillis()));
	}
	
	public <T> T getClaimFromToken(String token , Function<Claims, T> claimResolver) {
		final Claims claims = getAllClaimsFromToken(token);			
		return claimResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecretKey.getBytes()).parseClaimsJws(token).getBody();
	}
	
	public long convertHourToMillis(long jwtTokenValidity) {
		return (1000L * 60L * 60L) * jwtTokenValidity;
	}
}