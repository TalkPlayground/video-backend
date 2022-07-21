package com.playground.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.playground.domain.Session;
@Repository
public interface SessionRepository extends MongoRepository<Session, String> {

	Optional<Session> findBySessionUUID(String sessionId);
	List<Session> findAllBySessionStatus(String status);

}
