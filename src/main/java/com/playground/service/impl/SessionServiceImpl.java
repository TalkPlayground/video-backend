package com.playground.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.domain.Member;
import com.playground.domain.OtpSessions;
import com.playground.domain.Recordings;
import com.playground.domain.Session;
import com.playground.dto.HandleRecordingDTO;
import com.playground.dto.RecordingPayload;
import com.playground.dto.SessionPayload;
import com.playground.dto.SessionTranscriptFile;
import com.playground.dto.StoreSessionDTO;
import com.playground.repository.MemberRepository;
import com.playground.repository.RecordingRepository;
import com.playground.repository.SessionRepository;
import com.playground.service.MemberService;
import com.playground.service.SessionService;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class SessionServiceImpl implements SessionService {
	
	@Autowired RestTemplate restTemplate;
	@Autowired MemberRepository memberRepository;
	@Autowired MemberService memberService;
	@Autowired JavaMailSender mailSender;
	@Autowired OtpSessions otpSessions;
	@Autowired SessionRepository sessionRepository;
	@Autowired RecordingRepository recordingRepository;
	@Autowired ObjectMapper mapper;

	@Autowired
	private WebClient.Builder webClientBuilder;
	
	private static final Logger log = LoggerFactory.getLogger(SessionServiceImpl.class);
	@Value("${zoom.video-sdk.jwt.token}")
	private String zoomJwtToken;
	
	private String awsUrl="https://playground-audio.s3.us-west-2.amazonaws.com/stage/";

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
			String date = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC")).format(DateTimeFormatter
					.ofLocalizedDate(FormatStyle.FULL).withZone(ZoneId.of("Asia/Kolkata")));
			String subject = "Welcome to Playground, please verify your email address to Join at " + date;
			String body = "System generated OPT :- " + otp;
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(toAddress);
			message.setSubject(subject);
			message.setText(body);
			mailSender.send(message);
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
		}
	}
	
	private String generateOtp() {
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		return String.format("%06d", number);
	}

	@Override
	public boolean verifyOtp(String email, String otp) {
		String otpData = otpSessions.getOtp(email);
		return Objects.nonNull(otpData) ? otpData.equals(otp) : false;
	}
	
	@Override
	public String joinSession(String name, String email) {
		return memberService.createAnonemousUser(email, name);
	}
	
	public boolean storeSession(StoreSessionDTO data) {
		Optional<Member> memberStream = memberRepository.findByMemberUUID(data.getUserId());
		if (!memberStream.isPresent()) {
			throw new UsernameNotFoundException("User not found with id - " + data.getUserId());
		}
		
		Optional<Session> sessionStream = sessionRepository.findBySessionUUID(data.getSessionId());
		if (!sessionStream.isPresent()) {
			Session session = new Session();
			session.setSessionUUID(data.getSessionId());
			session.setMemberUUID(Set.of(data.getUserId()));
			session.setCreatorUUID(data.getUserId());
			session.setHasRecording(false);
			session.setSessionStatus("LIVE");
			sessionRepository.save(session);
			webClientBuilder.build().post().uri("localhost:8080/v1/user/session/store/airtable").body(Mono.just(session), Session.class).retrieve().bodyToMono(Object.class).block();

			return true;
		}else {
			Session session = sessionStream.get();
			session.getMemberUUID().add(data.getUserId());
			sessionRepository.save(session);
			webClientBuilder.build().post().uri("localhost:8080/v1/user/session/store/airtable").body(Mono.just(session), Session.class).retrieve().bodyToMono(Object.class).block();
			return true;
		}
	}
	
	@Override
	public SessionPayload checkLiveSessionDetails(Session session){
		try {
			String url = "https://api.zoom.us/v2/videosdk/sessions/"+session.getSessionUUID()+"?type=live";
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(zoomJwtToken);
			HttpEntity<SessionPayload> entity = new HttpEntity<>(headers);
			restTemplate.exchange(url, HttpMethod.GET, entity, SessionPayload.class);
			return null;
		} catch (Exception e) {
			return checkPastSessionDetails(session);
		}
	}
	@Override
	public SessionPayload checkPastSessionDetails(Session session){
		try {
			String url = "https://api.zoom.us/v2/videosdk/sessions/"+session.getSessionUUID()+"?type=past";
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(zoomJwtToken);
			HttpEntity<SessionPayload> entity = new HttpEntity<>(headers);
			ResponseEntity<SessionPayload> data = restTemplate.exchange(url, HttpMethod.GET, entity, SessionPayload.class);
			if (data.getStatusCodeValue()==200) {
				session.setHasRecording(data.getBody().isHas_recording());
				session.setStartTime(data.getBody().getStart_time());
				session.setEndTime(data.getBody().getEnd_time());
				session.setSessionStatus("PAST");
				sessionRepository.save(session);
			}
			return data.getStatusCodeValue()==200 ? data.getBody() : null;
		} catch (Exception e) {
			session.setSessionStatus("NOT_FOUND");
			sessionRepository.save(session);
			log.warn(e.getLocalizedMessage());
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List<RecordingPayload> fetchRecordingOfSession(String sessionId){
		try {
			String url = "https://api.zoom.us/v2/videosdk/sessions/"+sessionId+"/recordings";
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(zoomJwtToken);
			HttpEntity<HashMap<String, Object>> entity = new HttpEntity<>(headers);
			ResponseEntity<HashMap> result = restTemplate.exchange(url, HttpMethod.GET, entity, HashMap.class);
			if (result.getStatusCodeValue()==200) {
				Object object = result.getBody().get("participant_audio_files");
				return mapper.convertValue(object, new TypeReference<List<RecordingPayload>>(){});
			}
			return Collections.emptyList();
		} catch (Exception e) {
			updateSessionIfRecordingInProcess(sessionId);
			log.warn(e.getLocalizedMessage() + " at this sessionId :-  " + sessionId);
			return Collections.emptyList();
		}
	}
	
	public void updateSessionIfRecordingInProcess(String sessionId) {
		Optional<Session> sessionStream = sessionRepository.findBySessionUUID(sessionId);
		if (sessionStream.isPresent()) {
			Session session = sessionStream.get();
			session.setSessionStatus("LIVE");
			sessionRepository.save(session);
		}
	}

	@Override
	public void saveRecordingOfSession(String sessionId) {
		List<RecordingPayload> listOfData = fetchRecordingOfSession(sessionId);
		List<Recordings> mapedData = listOfData.stream().map(data->{
			Recordings recordings = new Recordings();
			recordings.setRecordingUUID(data.getId());
			recordings.setSessionUUID(sessionId);
			recordings.setMemberUUID(getMemberUUID(data.getFile_name()));
			recordings.setZoomUrl(data.getDownload_url());
			recordings.setRecordingStart(data.getRecording_start());
			recordings.setRecordingEnd(data.getRecording_end());
			return recordings;
		}).collect(Collectors.toList());
		List<Recordings> savedData = recordingRepository.saveAll(mapedData);
		savedData.parallelStream().forEach(rData->{
			updateAwsUrlInRecording(rData.getMemberUUID(), rData.getRecordingUUID(), rData.getZoomUrl());
		});
	}

	private String getMemberUUID(String fileName) {
		try {
			int first = fileName.indexOf('-');
			String userId = fileName.substring(first+2, first+16);
			return userId;
		} catch (Exception e) {
			log.error("Issues in memberId generating " + fileName + " - " +e.getLocalizedMessage());
			return null;
		}
	}
	
	@Override
	public boolean handleRecordingStatus(HandleRecordingDTO data) {
		try {
			String url = "https://api.zoom.us/v2/videosdk/sessions/"+data.getSessionId()+"/events";
			Map<String, Object> payload = new HashMap<>();
			payload.put("method", data.isStatus() ? "recording.start" : "recording.stop");
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(zoomJwtToken);
			HttpEntity<Object> entity = new HttpEntity<>(payload,headers);
			ResponseEntity<Object> result = restTemplate.exchange(url, HttpMethod.PATCH, entity, Object.class);
			return result.getStatusCodeValue()==202;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			return false;
		}
	}

	@Override
	public boolean insertTranscriptFiles(SessionTranscriptFile data) {
		Optional<Session> sessionStream = sessionRepository.findBySessionUUID(data.getSessionId());
		if (sessionStream.isPresent()) {
			Session session = sessionStream.get();
			session.setTranscriptFiles(data.getTranscriptFiles());
			sessionRepository.save(session);
			return true;
		}
		return false;
	}
	
	public void updateAwsUrlInRecording(String memberId, String recordingId, String zoomUrl) {
		try {
			String url = "https://om4c8gljhj.execute-api.us-west-2.amazonaws.com/stage/PushAudioOnS3";
			String fileName = memberId+"_"+recordingId+".m4a";
			Map<String, Object> payload = new HashMap<>();
			payload.put("url", zoomUrl);
			payload.put("fileName", fileName);
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<Object> entity = new HttpEntity<>(payload,headers);
			ResponseEntity<Object> result = restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
			if (result.getStatusCodeValue()==200) {
				Optional<Recordings> recordingStream = recordingRepository.findByRecordingUUID(recordingId);
				if (recordingStream.isPresent()) {
					Recordings recordings = recordingStream.get();
					recordings.setAwsUrl(awsUrl+fileName);
					recordingRepository.save(recordings);
					webClientBuilder.build().post().uri("localhost:8080/v1/user/session/recording/airtable").body(Mono.just(recordings), Recordings.class).retrieve().bodyToMono(Object.class).block();
				}
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
	}
}
