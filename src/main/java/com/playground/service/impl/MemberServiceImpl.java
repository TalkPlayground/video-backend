package com.playground.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playground.domain.Member;
import com.playground.dto.LoginDTO;
import com.playground.dto.SignupDTO;
import com.playground.repository.MemberRepository;
import com.playground.service.MemberService;
@Service
public class MemberServiceImpl implements MemberService {

	@Autowired MemberRepository memberRepository;

	@Override
	public boolean login(LoginDTO payload) {
		Optional<Member> userStream = memberRepository.findByEmail(payload.getUsername());
		if(userStream.isPresent()){
			Member member = userStream.get();
			return member.getPassword().equals(payload.getPassword());
		}
		return false;
	}

	@Override
	public boolean signup(SignupDTO payload) {
		Member member = new Member();
		member.setMemberUUID(generateUUID());
		member.setFullName(payload.getFullName());
		member.setEmail(payload.getEmail());
		member.setPassword(payload.getPassword());
		member.setCreationDate(LocalDateTime.now());
		member.setDeleted(false);
		memberRepository.save(member);
		return true;
	}

	private String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

}
