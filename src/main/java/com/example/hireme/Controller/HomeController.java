package com.example.hireme.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String getHomePage(Authentication authentication, Model model){
        model.addAttribute("user",authentication);
        return "home";
    }

    @GetMapping("/error")
    public String getErrorPage(){
        return "error_page";
    }
}
