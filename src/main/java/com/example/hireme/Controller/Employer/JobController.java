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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private final MediaService mediaService;
    private final LanguageConfig languageConfig;

    @GetMapping()
    public String getCompanyJobs(@RequestParam(name="page_number",required=false,defaultValue ="1") Long page_number, Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        List<JobOffer> jobOffers = jobOfferService.getJobsByCompany(employerProfileService.getEmployerProfile(user.getId()).getCompany().getId());
        if (page_number > 1 && jobOffers.size()<(page_number-1)*5+1){
            return "redirect:/employer/jobs";
        }
        long start = (page_number-1)*5;
        long end = start+5;
        List<JobOffer> paginatedJobsList = jobOfferService.getJobsByCompanyWithPagination(employerProfileService.getEmployerProfile(user.getId()).getCompany().getId(),start,end);

        Media media = mediaService.getMedia("Company",employerProfileService.getEmployerProfile(user.getId()).getCompany().getId(),"company_logo");
        int totalPages = (int) Math.ceil((double) jobOffers.size() / 5);;
        model.addAttribute("user", user);
        model.addAttribute("type", "profile");
        model.addAttribute("jobOffers", paginatedJobsList);
        model.addAttribute("media", media);
        model.addAttribute("now",LocalDateTime.now());
        model.addAttribute("cronoUnit",ChronoUnit.DAYS);
        model.addAttribute("page",page_number);
        model.addAttribute("totalPages",totalPages);
//        long diffTime = jobOffers.get(0).getPublished_at().until( LocalDateTime.now(), ChronoUnit.DAYS );
        return "Employer/jobs";
    }

    @GetMapping("/new")
    public String getCreateJobPage(Authentication authentication, Model model){
        CreateJobRequest createJobRequest = new CreateJobRequest();
        User user = (User) authentication.getPrincipal();
        model = getCommunAttr(model,createJobRequest,user);
        return "Employer/new_job";
    }

    @PostMapping("/store")
    public String createJob(Authentication authentication , @Valid CreateJobRequest createJobRequest,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model) {
        User user = (User) authentication.getPrincipal();
        model = getCommunAttr(model,createJobRequest,user);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            redirectAttributes.addFlashAttribute("createJobRequest", createJobRequest);
            return "Employer/new_job";
        }
        jobOfferService.create(createJobRequest,employerProfileService.getEmployerProfile(user.getId()).getCompany().getId());
        redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("create",new Object[] {}, locale));
        return "redirect:/employer/jobs/new";
    }



    public Model getCommunAttr(Model model,CreateJobRequest createJobRequest,User user){
        List<Country> countries = countryService.getActiveCountries();
        List<City> cities = cityService.getActiveCitiesByCountry(createJobRequest.getCountry_id());
        List<OfferCategory> categories = offerCategoryService.getAllCategories();
        JobType[] types = JobType.values();
        Currency[] currencies = Currency.values();
        model.addAttribute("user", user);
        model.addAttribute("createJobRequest", createJobRequest);
        model.addAttribute("type", "profile");
        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);
        model.addAttribute("categories", categories);
        model.addAttribute("types", types);
        model.addAttribute("currencies", currencies);
        return model;
    }

}
