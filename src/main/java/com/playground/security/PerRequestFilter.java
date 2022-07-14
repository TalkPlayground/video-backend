package com.playground.security;
import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.playground.service.impl.MemberServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;


public class PerRequestFilter extends OncePerRequestFilter {

	private final JwtTokenUtil jwtTokenUtil;
	private final HandlerExceptionResolver handlerExceptionResolver;
	private final MemberServiceImpl memberService;

	public PerRequestFilter(JwtTokenUtil jwtTokenUtil, HandlerExceptionResolver handlerExceptionResolver, MemberServiceImpl memberService) {
		this.jwtTokenUtil=jwtTokenUtil;
		this.handlerExceptionResolver=handlerExceptionResolver;
		this.memberService=memberService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		try {
			String accessToken = request.getHeader("Authorization");
			if (Objects.nonNull(accessToken) && !accessToken.equals("") && accessToken.startsWith("Bearer")) {
				String filteredToken = accessToken.replace("Bearer", "");
				if (jwtTokenUtil.isValidSignedToken(filteredToken) && !jwtTokenUtil.isTokenExpired(filteredToken)) {
					UserDetails userDetails = memberService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(filteredToken));
					SecurityContextHolder.getContext().setAuthentication(getAuthentication(userDetails, request));
				}
			} 
			filterChain.doFilter(request, response);
		} catch (MalformedJwtException | SignatureException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
			handlerExceptionResolver.resolveException(request, response, null, e);
		}
	}

	private Authentication getAuthentication(UserDetails userDetails, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return authentication;
	}

}