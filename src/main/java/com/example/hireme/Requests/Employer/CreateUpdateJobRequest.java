package com.example.hireme.Requests.Employer;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateJobRequest {

    Long id;

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

    public CreateUpdateJobRequest(String title, Long category_id, String type, Long country_id, Long city_id, Double salary, String currency, String job_description) {
        this.title = title;
        this.category_id = category_id;
        this.type = type;
        this.country_id = country_id;
        this.city_id = city_id;
        this.salary = salary;
        this.currency = currency;
        this.job_description = job_description;
    }
}
