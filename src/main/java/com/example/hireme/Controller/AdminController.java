package com.example.hireme.Controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/dashboard")
    public String getCandidateProfile(){
        return "Admin/dashboard";
    }
}
