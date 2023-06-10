package com.example.hireme.Requests.Candidate;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.time.DurationMin;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CandidateRegisterRequest {

    @NotEmpty()
    @Size(min = 3,max = 20)
    String first_name;

    @NotEmpty()
    @Size(min = 3,max = 20)
    String last_name;

    @NotNull()
    LocalDate birth_date;

    @NotEmpty()
    @Email()
    String email;

    @NotEmpty()
    @Size(min = 8,max = 15)
    String password;

    @NotEmpty()
    @Size(min = 8,max = 15)
    String password_confirmation;

}
