package com.example.hireme.Controller;

import com.example.hireme.Model.Entity.*;
import com.example.hireme.Service.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@AllArgsConstructor
public class HomeController {
    private final CountryService countryService;
    private final OfferCategoryService offerCategoryService;
    private final CompanyService companyService;
    private final JobOfferService jobOfferService;
    private final MediaService mediaService;
    private final EmployerProfileService employerProfileService;
    @GetMapping("/")
    public String getHomePage(Authentication authentication, Model model){
        List<Country> countries = countryService.getActiveCountries();
        List<OfferCategory> categories = offerCategoryService.getAllCategories();
        List<Company> toCompanies = companyService.getTopCompaniesByJobOffersCount();
        List<JobOffer> jobOffers = jobOfferService.getRecentJobs();
        User user;
        if (authentication!=null){
            user = (User) authentication.getPrincipal();
            model.addAttribute("role",user.getRole().toString());
        }
        else user = null;
        model.addAttribute("user",user);
        model.addAttribute("countries",countries);
        model.addAttribute("categories",categories);
        model.addAttribute("type","home");
        model.addAttribute("companies",toCompanies);
        model.addAttribute("jobs",jobOffers);
        model.addAttribute("mediaService",mediaService);
        model.addAttribute("now", LocalDateTime.now());
        model.addAttribute("cronoUnit", ChronoUnit.DAYS);
        return "home";
    }

    @GetMapping("/error")
    public String getErrorPage(){
        return "error";
    }
}
