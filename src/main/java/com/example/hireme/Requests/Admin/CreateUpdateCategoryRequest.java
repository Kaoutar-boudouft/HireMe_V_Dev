package com.example.hireme.Requests.Admin;

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
public class CreateUpdateCategoryRequest {

    Long id;

    @NotEmpty()
    @Size(min = 5,max = 30)
    String label;

    public CreateUpdateCategoryRequest(String label) {
        this.label = label;
    }
}
