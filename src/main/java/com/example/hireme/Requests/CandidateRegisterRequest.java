package com.example.hireme.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CandidateRegisterRequest {

    String first_name;
    String last_name;
    LocalDate birth_date;
    String email;
    String password;

}
