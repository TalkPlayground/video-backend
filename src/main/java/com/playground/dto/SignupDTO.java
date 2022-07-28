package com.playground.dto;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupDTO {
	@NotEmpty @NotBlank
    private String fullName;
	@Email
    private String email;
	@NotEmpty @NotBlank
    private String password;
	@NotNull
    private LocalDate dob;
}
