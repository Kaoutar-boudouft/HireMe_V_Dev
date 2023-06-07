package com.example.hireme.Controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/employer")
public class EmployerController {

    @GetMapping("/profile")
    public String getCandidateProfile(){
        return "Employer/profile";
    }
}
