package com.playground.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupDTO {
    private String fullName;
    private String email;
    private String password;
}
