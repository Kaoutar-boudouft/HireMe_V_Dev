package com.example.hireme.Controller.Admin;

import com.example.hireme.Model.Entity.User;
import com.example.hireme.Service.*;
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
    private final JobOfferService jobOfferService;
    private final CandidateProfileService candidateProfileService;
    private final OfferCategoryService offerCategoryService;
    private final CompanyService companyService;
    private final BlogService blogService;
    private final NewsLetterService newsLetterService;
    @GetMapping("/dashboard")
    public String getCandidateProfile(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        Long jobsCount = jobOfferService.countAll();
        Long candidatesCount = candidateProfileService.countAll();
        Long categoriesCount = offerCategoryService.countAll();
        Long companyCount = companyService.countAll();
        Long blogCount = blogService.countAll();
        Long newsLettersCount = newsLetterService.countAll();
        model.addAttribute("jobCount",jobsCount);
        model.addAttribute("candidatesCount",candidatesCount);
        model.addAttribute("categoriesCount",categoriesCount);
        model.addAttribute("companyCount",companyCount);
        model.addAttribute("blogCount",blogCount);
        model.addAttribute("blogCount",blogCount);
        model.addAttribute("newsLettersCount",newsLettersCount);
        model.addAttribute("type","dashboard");
        model.addAttribute("selected", "dashboard");
        return "Admin/dashboard";
    }
}
