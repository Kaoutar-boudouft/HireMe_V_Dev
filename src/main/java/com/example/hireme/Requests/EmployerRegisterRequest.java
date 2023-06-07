package com.example.hireme.Requests;

import com.example.hireme.Model.Entity.City;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployerRegisterRequest {

    @NotEmpty()
    @Size(min = 5,max = 30)
    String company_name;

    @NotEmpty()
    @Size(min = 5,max = 30)
    String fiscal_id;

    @NotNull()
    @NumberFormat
    Integer company_phone_number;

    @NotEmpty()
    @Email()
    String company_email;

    @NotEmpty()
    @Size(min = 5,max = 30)
    @URL()
    String company_website;

    @NotEmpty()
    @Size(min = 10,max = 50)
    String company_address;

    @NotNull()
    Long company_city;

    @NotNull()
    Long company_country;

    @NotEmpty()
    @Size(min = 3,max = 15)
    String first_name;

    @NotEmpty()
    @Size(min = 3,max = 15)
    String last_name;

    @NotNull
    LocalDate birth_date;

    @NotEmpty()
    @Email()
    String email;

    @NotEmpty()
    @Size(min = 8,max = 15)
    String password;

    @NotNull()
    @NumberFormat
    Integer mobile;

    @NotEmpty()
    @Size(min = 5,max = 10)
    String id_number;
}
