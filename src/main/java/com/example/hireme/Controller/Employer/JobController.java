package com.example.hireme.Controller.Employer;

import com.example.hireme.Model.Currency;
import com.example.hireme.Model.Entity.*;
import com.example.hireme.Model.JobType;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Requests.Employer.CreateJobRequest;
import com.example.hireme.Requests.Employer.UpdateEmployerCompanyRequest;
import com.example.hireme.Requests.Employer.UpdateEmployerProfileRequest;
import com.example.hireme.Service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Controller
@AllArgsConstructor
@RequestMapping("/employer/jobs")
public class JobController {

    private final CountryService countryService;
    private final OfferCategoryService offerCategoryService;
    private final CityService cityService;
    private final JobOfferService jobOfferService;
    private final EmployerProfileService employerProfileService;
    private final LanguageConfig languageConfig;

    @GetMapping("/new")
    public String getCreateJobPage(Authentication authentication, Model model){
        CreateJobRequest createJobRequest = new CreateJobRequest();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("createJobRequest", createJobRequest);
        model.addAttribute("type", "profile");
        List<Country> countries = countryService.getActiveCountries();
        List<City> cities = cityService.getActiveCitiesByCountry(createJobRequest.getCountry_id());
        List<OfferCategory> categories = offerCategoryService.getAllCategories();
        JobType[] types = JobType.values();
        Currency[] currencies = Currency.values();
        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);
        model.addAttribute("categories", categories);
        model.addAttribute("types", types);
        model.addAttribute("currencies", currencies);

        return "Employer/new_job";
    }

    @PostMapping("/store")
    public String createJob(Authentication authentication , @Valid CreateJobRequest createJobRequest,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("createJobRequest", createJobRequest);
        model.addAttribute("type", "profile");
        List<Country> countries = countryService.getActiveCountries();
        List<City> cities = cityService.getActiveCitiesByCountry(createJobRequest.getCountry_id());
        List<OfferCategory> categories = offerCategoryService.getAllCategories();
        JobType[] types = JobType.values();
        Currency[] currencies = Currency.values();
        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);
        model.addAttribute("categories", categories);
        model.addAttribute("types", types);
        model.addAttribute("currencies", currencies);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            redirectAttributes.addFlashAttribute("createJobRequest", createJobRequest);
            return "Employer/new_job";
        }
        jobOfferService.create(createJobRequest,employerProfileService.getEmployerProfile(user.getId()).getCompany().getId());
        redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("create",new Object[] {}, locale));
        return "redirect:/employer/jobs/new";
    }


}
