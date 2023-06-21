package com.example.hireme.Requests.Admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAdminRequest {
    @NotEmpty()
    @Size(min = 3,max = 20)
    String first_name;

    @NotEmpty()
    @Size(min = 3,max = 20)
    String last_name;

    @NotEmpty()
    @Email()
    String email;

    @NotEmpty()
    @Size(min = 8,max = 15)
    String password;

    @NotNull()
    LocalDate birth_date;

    @NotNull()
    @NumberFormat
    Integer mobile;

    @NotEmpty()
    @Size(min = 5,max = 10)
    String id_number;

    @NotNull()
    Long city;

    @NotNull()
    Long country;


}
