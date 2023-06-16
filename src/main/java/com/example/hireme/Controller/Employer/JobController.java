package com.example.hireme.Controller.Employer;

import com.example.hireme.Model.Currency;
import com.example.hireme.Model.Entity.*;
import com.example.hireme.Model.JobType;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Requests.Employer.CreateUpdateJobRequest;
import com.example.hireme.Service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
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
    private final CandidateProfileService candidateProfileService;

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
        int totalPages = (int) Math.ceil((double) jobOffers.size() / 5);
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

    @GetMapping("/{job_id}/edit")
    public String getJobEditPage(@PathVariable("job_id") Long job_id,Model model,Authentication authentication){
        User user = (User) authentication.getPrincipal();
        JobOffer checkJobExistance = jobOfferService.getJobById(job_id);
        if (checkJobExistance != null){
            boolean checkUserAuthorityOnJob = jobOfferService.checkEmployerJobAuthority(job_id,user.getId());
            if (checkUserAuthorityOnJob){
                CreateUpdateJobRequest createUpdateJobRequest = jobOfferService.prepareUpdateJobRequest(job_id);
                model = getCommunAttr(model,createUpdateJobRequest,user);
                return "Employer/update_job";
            }
            else {
                return "redirect:/employer/jobs";
            }
        }
        else {
            return "redirect:/employer/jobs";
        }
    }

    @PostMapping("/{job_id}/update")
    public String updateJob(Authentication authentication,@PathVariable("job_id") Long job_id,@Valid CreateUpdateJobRequest createUpdateJobRequest,
                            BindingResult bindingResult,RedirectAttributes redirectAttributes,Model model,
                            Locale locale){
        User user = (User) authentication.getPrincipal();
        JobOffer checkJobExistance = jobOfferService.getJobById(job_id);
        if (checkJobExistance!=null){
            boolean checkUserAuthorityOnJob = jobOfferService.checkEmployerJobAuthority(job_id,user.getId());
            if (checkUserAuthorityOnJob){
                if (bindingResult.hasErrors()){
                    redirectAttributes.addFlashAttribute("error", bindingResult);
                    redirectAttributes.addFlashAttribute("createUpdateJobRequest", createUpdateJobRequest);
                    return "Employer/update_job";
                }
                model = getCommunAttr(model,createUpdateJobRequest,user);
                createUpdateJobRequest.setId(job_id);
                jobOfferService.updateJobOffer(createUpdateJobRequest);
                redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
                return "redirect:/employer/jobs";
            }
            else {
                return "redirect:/employer/jobs";
            }
        }
        else {
            return "redirect:/employer/jobs";
        }
    }

    @GetMapping("/new")
    public String getCreateJobPage(Authentication authentication, Model model){
        CreateUpdateJobRequest createUpdateJobRequest = new CreateUpdateJobRequest();
        User user = (User) authentication.getPrincipal();
        model = getCommunAttr(model, createUpdateJobRequest,user);
        return "Employer/new_job";
    }

    @PostMapping("/store")
    public String createJob(Authentication authentication , @Valid CreateUpdateJobRequest createUpdateJobRequest,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model) {
        User user = (User) authentication.getPrincipal();
        model = getCommunAttr(model, createUpdateJobRequest,user);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            redirectAttributes.addFlashAttribute("createJobRequest", createUpdateJobRequest);
            return "Employer/new_job";
        }
        jobOfferService.create(createUpdateJobRequest,employerProfileService.getEmployerProfile(user.getId()).getCompany().getId());
        redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("create",new Object[] {}, locale));
        return "redirect:/employer/jobs/new";
    }

    @GetMapping("/{job_id}/disable")
    public String changeJobState(@PathVariable("job_id") Long job_id,Model model,RedirectAttributes redirectAttributes,Locale locale){
        JobOffer jobOffer = jobOfferService.getJobById(job_id);
        if (jobOffer!=null){
            jobOfferService.changeJobState(jobOffer);
            redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("job_status_changed",new Object[] {}, locale));
        }
        else {
            redirectAttributes.addFlashAttribute("error","job with this id doesn't exist");
        }
        return "redirect:/employer/jobs";
    }

    @GetMapping("/{job_id}/candidatures")
    public String getJobCandidaturesPage(@RequestParam(name="page_number",required=false,defaultValue ="1") Long page_number,Authentication authentication,@PathVariable("job_id") Long job_id,Model model){
        User user = (User) authentication.getPrincipal();
        List<CandidateProfile> candidateProfiles = candidateProfileService.getCandidaturesByJob(job_id);
        JobOffer jobOffer = jobOfferService.getJobById(job_id);
        if (page_number > 1 && candidateProfiles.size()<(page_number-1)*6+1){
            return "redirect:/employer/jobs/"+job_id+"/candidatures";
        }
        long start = (page_number-1)*6;
        long end = start+6;
        int totalPages = (int) Math.ceil((double) candidateProfiles.size() / 6);
        List<CandidateProfile> candidateProfiles1 = candidateProfileService.getCandidaturesByJobWithPagination(job_id,start,end);
        model.addAttribute("user",user);
        model.addAttribute("mediaService", mediaService);
        model.addAttribute("type", "profile");
        model.addAttribute("candidateProfiles", candidateProfiles1);
        model.addAttribute("page",page_number);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("job",jobOffer);
        return "Employer/job_candidatures";
    }



    public Model getCommunAttr(Model model, CreateUpdateJobRequest createUpdateJobRequest, User user){
        List<Country> countries = countryService.getActiveCountries();
        List<City> cities = cityService.getActiveCitiesByCountry(createUpdateJobRequest.getCountry_id());
        List<OfferCategory> categories = offerCategoryService.getAllCategories();
        JobType[] types = JobType.values();
        List<JobType> jobTypes = Arrays.asList(types);
        Currency[] currencies = Currency.values();
        model.addAttribute("user", user);
        model.addAttribute("createUpdateJobRequest", createUpdateJobRequest);
        model.addAttribute("type", "profile");
        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);
        model.addAttribute("categories", categories);
        model.addAttribute("types", types);
        model.addAttribute("currencies", currencies);
        return model;
    }

}
