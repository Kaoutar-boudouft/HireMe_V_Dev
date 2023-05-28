package com.example.hireme.Requests;

import com.example.hireme.Model.Entity.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployerRegisterRequest {

    String company_name;
    String fiscal_id;
    Integer company_phone_number;
    String company_email;
    String company_website;
    String Company_address;
    Long company_city;
    String first_name;
    String last_name;
    LocalDate birth_date;
    String email;
    String password;
    Integer mobile;
    String id_number;
}
