package com.playground.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.playground.domain.Session;
@Repository
public interface SessionRepository extends MongoRepository<Session, String> {

}
