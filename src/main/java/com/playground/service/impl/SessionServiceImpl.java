package com.playground.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.playground.domain.Member;
import com.playground.domain.OtpSessions;
import com.playground.repository.MemberRepository;
import com.playground.service.MemberService;
import com.playground.service.SessionService;
@Service
public class SessionServiceImpl implements SessionService {
	
	@Autowired RestTemplate restTemplate;
	@Autowired MemberRepository memberRepository;
	@Autowired MemberService memberService;
	@Autowired JavaMailSender mailSender;
	@Autowired OtpSessions otpSessions;
	
	private static final Logger log = LoggerFactory.getLogger(SessionServiceImpl.class);

	@Override
	public boolean verifyEmail(String email, String name) {
		Optional<Member> memberStream = memberRepository.findByEmail(email);
		if (memberStream.isPresent()) {
			String generatedOtp = generateOtp();
			otpSessions.setOtp(email, generatedOtp);
			new Thread(() -> sendOTPMail(email, generatedOtp)).start();
			return true;
		}else {
			memberService.createAnonemousUser(email, name);
			String generatedOtp = generateOtp();
			otpSessions.setOtp(email, generatedOtp);
			new Thread(() -> sendOTPMail(email, generatedOtp)).start();
			return true;
		}
		
		
	}
	
	public void sendOTPMail(String toAddress, String otp) {
		try {
			String subject = "Welcome to Playground, please verify your email address to Join at " + LocalDateTime.now();
			String body = "System generated OPT :- " + otp;
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(toAddress);
			message.setSubject(subject);
			message.setText(body);
			mailSender.send(message);
		} catch (MailException ex) {
			log.error(ex.getLocalizedMessage());
		}
	}
	
	private String generateOtp() {
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		return String.format("%06d", number);
	}
	
	
}
