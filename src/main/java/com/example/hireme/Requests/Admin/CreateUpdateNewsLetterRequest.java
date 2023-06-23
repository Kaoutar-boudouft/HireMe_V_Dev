package com.example.hireme.Requests.Admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateNewsLetterRequest {

    Long id;

    @NotEmpty()
    @Email()
    String email;

    public CreateUpdateNewsLetterRequest(String email) {
        this.email = email;
    }
}
