package com.example.hireme.Controller.Employer;

import com.example.hireme.Model.Entity.User;
import com.example.hireme.Requests.Candidate.UpdateCandidateProfileRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/employer")
public class EmployerController {
    @GetMapping("/profile")
    public String getEmployerProfile(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user",user);
        model.addAttribute("type","profile");
        return "Employer/profile";
    }
}
