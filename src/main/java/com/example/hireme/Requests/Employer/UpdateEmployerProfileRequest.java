package com.example.hireme.Requests.Employer;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployerProfileRequest {
    @NotEmpty()
    @Size(min = 3,max = 20)
    String first_name;

    @NotEmpty()
    @Size(min = 3,max = 20)
    String last_name;

    @NotNull()
    LocalDate birth_date;

    @NotNull()
    @NumberFormat
    Integer mobile;

    @NotEmpty()
    @Size(min = 5,max = 10)
    String id_number;


}
