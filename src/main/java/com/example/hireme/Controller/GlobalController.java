package com.example.hireme.Controller;

import com.example.hireme.Model.Entity.*;
import com.example.hireme.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class GlobalController {
    private final CountryService countryService;
    private final OfferCategoryService offerCategoryService;
    private final CompanyService companyService;
    private final JobOfferService jobOfferService;
    private final BlogService blogService;
    private final MediaService mediaService;
    @GetMapping("/")
    public String getHomePage(Authentication authentication, Model model, Locale locale){
        List<Country> countries = countryService.getActiveCountries();
        List<OfferCategory> categories = offerCategoryService.getAllCategories();
        List<Company> toCompanies = companyService.getTopCompaniesByJobOffersCount();
        List<JobOffer> jobOffers = jobOfferService.getRecentJobs();
        System.out.println("kaoutar"+locale.getLanguage().toString());
        List<Blog> blogs = blogService.findRecentBlogs(locale.getLanguage().toString());
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
        model.addAttribute("blogs",blogs);
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
