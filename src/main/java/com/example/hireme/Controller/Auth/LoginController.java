package com.example.hireme.Controller.Auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String getLoginPage(){
        return "Auth/login";
    }

    @GetMapping("/registration/candidate")
    public String test(){
        return "Auth/candidateRegistration";
    }
}
