package com.example.hireme.Controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/candidate")
public class CandidateController {

    @GetMapping("/profile")
    public String getCandidateProfile(){
        return "Candidate/profile";
    }
}
