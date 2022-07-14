package com.playground.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class Member {
    
    @Id
    private String id;
    @Indexed(unique = true)
    private String memberUUID;
    @Indexed(unique = true)
    private String email;
    private List<String> roles;
    private String fullName;
    private LocalDate dob;
    private String inviteCode;
    private String password;
    private LocalDateTime creationDate;
    private boolean deleted;
}
