package com.example.hireme.Requests.Employer;

import com.example.hireme.Model.Currency;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateJobRequest {

    @NotEmpty()
    @Size(min = 5,max = 30)
    String title;

    @NotNull
    Long category_id;

    @NotEmpty
    String type;

    @NotNull
    Long country_id;

    @NotNull
    Long city_id;

    @NotNull
    Double salary;

    @NotEmpty()
    String currency;

    @NotEmpty()
    String job_description;

}
