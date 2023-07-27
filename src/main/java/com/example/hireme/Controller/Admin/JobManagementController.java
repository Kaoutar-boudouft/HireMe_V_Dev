package com.example.hireme.Controller.Admin;

import com.example.hireme.Model.Currency;
import com.example.hireme.Model.Entity.*;
import com.example.hireme.Model.JobType;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Requests.Employer.CreateUpdateJobRequest;
import com.example.hireme.Requests.Employer.UpdateEmployerCompanyRequest;
import com.example.hireme.Service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/jobs")
public class JobManagementController {

    private final JobOfferService jobOfferService;
    private final CountryService countryService;
    private final CityService cityService;
    private final CompanyService companyService;
    private final OfferCategoryService offerCategoryService;
    private final LanguageConfig languageConfig;


    @GetMapping()
    public String getJobsPage(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        List<JobOffer> jobOffers = jobOfferService.getAll();
        model.addAttribute("user",user);
        model.addAttribute("jobs",jobOffers);
        model.addAttribute("type","dashboard");
        model.addAttribute("selected", "jobs");
        return "Admin/jobs";
    }

    @GetMapping("/{job_id}/edit")
    public String getJobUpdatePage(@PathVariable("job_id") Long job_id, Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        JobOffer jobOffer = jobOfferService.getJobById(job_id);
        if (jobOffer!=null){
            CreateUpdateJobRequest createUpdateJobRequest = jobOfferService.prepareUpdateJobRequest(job_id);
            List<City> cities = cityService.getActiveCitiesByCountry(jobOffer.getCity().getCountry().getId());
            model = getCommAttr(model,cities,user,createUpdateJobRequest,job_id);
            return "Admin/update_job";
        }
        return "redirect:/admin/jobs";
    }

    @GetMapping("/create")
    public String getJobCreatePage(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
            CreateUpdateJobRequest createUpdateJobRequest = new CreateUpdateJobRequest();
            List<City> cities = new ArrayList<>();
            model = getCommAttr(model,cities,user,createUpdateJobRequest,null);
            return "Admin/update_job";
    }

    @PostMapping("/store")
    public String createJob(Authentication authentication , @Valid CreateUpdateJobRequest createUpdateJobRequest,
                            BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model) {
        User user = (User) authentication.getPrincipal();
        List<City> cities = new ArrayList<>();
        model = getCommAttr(model,cities,user,createUpdateJobRequest,null);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            redirectAttributes.addFlashAttribute("createUpdateJobRequest", createUpdateJobRequest);
            return "Admin/update_job";
        }
        jobOfferService.create(createUpdateJobRequest,createUpdateJobRequest.getCompany_id());
        redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("create",new Object[] {}, locale));
        return "redirect:/admin/jobs";
    }

    @PostMapping("/{job_id}/update")
    public String updateJob(Authentication authentication,@PathVariable("job_id") Long job_id,@Valid CreateUpdateJobRequest createUpdateJobRequest,
                            BindingResult bindingResult,RedirectAttributes redirectAttributes,Model model,
                            Locale locale){
        User user = (User) authentication.getPrincipal();
        JobOffer checkJobExistance = jobOfferService.getJobById(job_id);
        if (checkJobExistance!=null){
            List<City> cities = cityService.getActiveCitiesByCountry(jobOfferService.getJobById(job_id).getCity().getCountry().getId());
            model = getCommAttr(model,cities,user,createUpdateJobRequest,job_id);
                if (bindingResult.hasErrors()){
                    redirectAttributes.addFlashAttribute("error", bindingResult);
                    redirectAttributes.addFlashAttribute("createUpdateJobRequest", createUpdateJobRequest);
                    return "Admin/update_job";
                }
                createUpdateJobRequest.setId(job_id);
                jobOfferService.updateJobOffer(createUpdateJobRequest);
                redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
                return "redirect:/admin/jobs/"+job_id+"/edit";
        }
        else {
            return "redirect:/admin/jobs";
        }
    }

    public Model getCommAttr(Model model,List<City> cities,User user,CreateUpdateJobRequest createUpdateJobRequest,Long job_id){
        List<Country> countries = countryService.getActiveCountries();
        JobType[] types = JobType.values();
        Currency[] currencies = Currency.values();
        List<Company> companies = companyService.getCompaniesByActive(true);
        List<OfferCategory> categories = offerCategoryService.getAllCategories();
        model.addAttribute("user",user);
        model.addAttribute("createUpdateJobRequest",createUpdateJobRequest);
        model.addAttribute("type","dashboard");
        model.addAttribute("countries",countries);
        model.addAttribute("cities",cities);
        model.addAttribute("types",types);
        model.addAttribute("currencies",currencies);
        model.addAttribute("companies",companies);
        model.addAttribute("categories",categories);
        model.addAttribute("job_id",job_id);
        model.addAttribute("selected", "jobs");
        return model;
    }

    @GetMapping("/delete/{job_id}")
    public String deleteJob(@PathVariable("job_id") Long job_id,
                                RedirectAttributes redirectAttributes,Locale locale,Model model){
        JobOffer jobOffer = jobOfferService.getJobById(job_id);
        if (jobOffer!=null){
            jobOfferService.removeJob(jobOffer);
            redirectAttributes.addAttribute("successMessage",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
        }
        return "redirect:/admin/jobs";
    }


}
