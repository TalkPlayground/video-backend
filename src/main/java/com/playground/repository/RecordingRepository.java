package com.playground.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.playground.domain.Recordings;
@Repository
public interface RecordingRepository extends MongoRepository<Recordings, String> {

}
