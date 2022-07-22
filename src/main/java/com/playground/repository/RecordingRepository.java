package com.playground.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.playground.domain.Recordings;
@Repository
public interface RecordingRepository extends MongoRepository<Recordings, String> {

	Optional<Recordings> findByRecordingUUID(String recordingId);

}
