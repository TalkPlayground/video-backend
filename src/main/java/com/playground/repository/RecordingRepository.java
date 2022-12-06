package com.playground.repository;

import com.playground.domain.Recordings;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecordingRepository extends MongoRepository<Recordings, String> {

    Optional<Recordings> findByRecordingUUID(String recordingId);

}
