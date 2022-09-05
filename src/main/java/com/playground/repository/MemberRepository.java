package com.playground.repository;

import com.playground.domain.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {

    Optional<Member> findByEmail(String username);

    Optional<Member> findByEmailAndDeleted(String username, boolean deleted);

    Optional<Member> findByMemberUUID(String userId);

}
