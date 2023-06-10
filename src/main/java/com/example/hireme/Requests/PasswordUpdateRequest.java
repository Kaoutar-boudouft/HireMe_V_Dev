package com.example.hireme.Requests;

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
public class PasswordUpdateRequest {
    @NotEmpty()
    @Size(min = 8,max = 15)
    String password;

    @NotEmpty()
    @Size(min = 8,max = 15)
    String password_confirmation;
}
