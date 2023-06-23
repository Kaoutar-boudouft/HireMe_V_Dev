package com.example.hireme.Requests.Admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateSupportRequest {

    Long id;

    @NotEmpty()
    @Email()
    String email;

    public CreateUpdateSupportRequest(String email) {
        this.email = email;
    }
}
