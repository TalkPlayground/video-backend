package com.playground.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.playground.domain.Member;
import com.playground.dto.LoginDTO;
import com.playground.dto.SignupDTO;
import com.playground.repository.MemberRepository;
import com.playground.security.JwtTokenUtil;
import com.playground.service.MemberService;
@Service
public class MemberServiceImpl implements MemberService, UserDetailsService {

	@Autowired MemberRepository memberRepository;
	@Autowired AuthenticationManager authenticationManager;
	@Autowired JwtTokenUtil jwtTokenUtil;
	
	private static final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	@Override
	public Object login(LoginDTO payload) {
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getUsername(), payload.getPassword()));
		String username = ((org.springframework.security.core.userdetails.User) authenticate.getPrincipal()).getUsername();
		Member user = loadUserByEmail(username);
		Map<String, Object> response = new HashMap<>();
		response.put("accessToken", jwtTokenUtil.generateToken(user));
		return response;
	}

	@Override
	public boolean signup(SignupDTO payload) {
		Optional<Member> memberStream = memberRepository.findByEmail(payload.getEmail());
		if (memberStream.isPresent() && memberStream.get().getRoles().contains("ROLE_ANONYMOUS_USER")) {
			Member member = memberStream.get();
			return mapMemberDetails(member, payload);
		}else {
			Member member = new Member();
			member.setMemberUUID(generateUUID());
			return mapMemberDetails(member, payload);
		}
	}

	private boolean mapMemberDetails(Member member, SignupDTO payload) {
		member.setFullName(payload.getFullName());
		member.setEmail(payload.getEmail());
		member.setRoles(List.of("ROLE_USER"));
		member.setPassword(encoder.encode(payload.getPassword()));
		member.setCreationDate(LocalDateTime.now());
		member.setDob(payload.getDob());
		member.setInviteCode(payload.getInviteCode());
		member.setDeleted(false);
		memberRepository.save(member);
		return true;
	}

	private String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	@Override
	public boolean createAnonemousUser(String email, String name) {
		Member member = new Member();
		member.setMemberUUID(generateUUID());
		member.setFullName(name);
		member.setEmail(email);
		member.setRoles(List.of("ROLE_ANONYMOUS_USER"));
		member.setCreationDate(LocalDateTime.now());
		member.setDeleted(false);
		memberRepository.save(member);
		return true;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Member> userStream = memberRepository.findByEmailAndDeleted(username, false);
		if (userStream.isPresent()) {
			Member user = userStream.get();
			Set<GrantedAuthority> userRoles = user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
			return new org.springframework.security.core.userdetails.User(username, user.getPassword(), userRoles);
		}else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
	
	public Member loadUserByEmail(String email) throws UsernameNotFoundException {
		Optional<Member> userStream = memberRepository.findByEmailAndDeleted(email, false);
		if (userStream.isPresent()) {
			return userStream.get();
		}else {
			throw new UsernameNotFoundException("User not found with username: " + email);
		}
	}

}
