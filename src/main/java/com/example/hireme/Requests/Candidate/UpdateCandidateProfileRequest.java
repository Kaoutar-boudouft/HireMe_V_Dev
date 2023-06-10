package com.example.hireme.Requests.Candidate;

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
public class UpdateCandidateProfileRequest {
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

    @NotNull()
    Long city;

    @NotNull()
    Long country;

    @NotEmpty()
    String motivation_letter;

    @NotNull
    MultipartFile file;

}
