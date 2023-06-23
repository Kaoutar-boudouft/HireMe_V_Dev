package com.example.hireme.Controller.Admin;

import com.example.hireme.Model.Entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminManagementController {
    @GetMapping("/dashboard")
    public String getCandidateProfile(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user",user);
        model.addAttribute("type","dashboard");
        return "Admin/dashboard";
    }
}
