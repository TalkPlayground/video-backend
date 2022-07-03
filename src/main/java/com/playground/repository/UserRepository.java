package com.playground.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.playground.domain.User;
@Repository
public interface UserRepository extends MongoRepository<User, Long> {

    Optional<User> findTopByOrderByUserIdDesc();
    Optional<User> findByEmail(String username);

}
