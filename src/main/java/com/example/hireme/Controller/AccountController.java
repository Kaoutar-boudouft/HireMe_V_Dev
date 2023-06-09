package com.example.hireme.Controller;

import com.example.hireme.Model.Entity.User;
import com.example.hireme.Requests.UpdateCandidateProfileRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/candidate")
public class AccountController {
    @GetMapping("/account")
    public String getAccount(Model model){
        model.addAttribute("type","profile");
        return "Candidate/account";
    }
}
