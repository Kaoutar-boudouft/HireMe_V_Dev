package com.example.hireme.Controller;

import com.example.hireme.Model.Entity.CandidateProfile;
import com.example.hireme.Model.Entity.EmployerProfile;
import com.example.hireme.Model.Entity.JobOffer;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Model.Role;
import com.example.hireme.Service.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller("JobController")
@AllArgsConstructor
@RequestMapping("/jobs")
public class JobController {

    private final JobOfferService jobOfferService;
    private final MediaService mediaService;
    private final EmployerProfileService employerProfileService;
    private final CandidateProfileService candidateProfileService;

    @GetMapping("/search")
    public String getSearchResult(Authentication authentication, @RequestParam(name="t") String title, @RequestParam(name="l") String location,
                                  @RequestParam(name="c") String category,
                                  @RequestParam(name="page_number",required = false,defaultValue = "1") long page_number, Model model){
        List<JobOffer> jobOffers = jobOfferService.searchJobByTitleAndLocationAndCategory(title,location,category);
        if (page_number > 1 && jobOffers.size()<(page_number-1)*5+1){
            return "redirect:/jobs/search?t="+title+"&&l="+location+"&&c="+category;
        }
        long start = (page_number-1)*5;
        long end = start+5;
        List<JobOffer> jobOffers1 = jobOfferService.searchJobByTitleAndLocationAndCategoryWithPagination(title,location,category,start,end);
        int totalPages = (int) Math.ceil((double) jobOffers.size() / 5);
        User user;
        if (authentication!=null){
            user = (User) authentication.getPrincipal();
            model.addAttribute("role",user.getRole().toString());
        }
        else user = null;
        model.addAttribute("user",user);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("jobOffers",jobOffers1);
        model.addAttribute("now", LocalDateTime.now());
        model.addAttribute("cronoUnit", ChronoUnit.DAYS);
        model.addAttribute("page",page_number);
        model.addAttribute("title",title);
        model.addAttribute("location",location);
        model.addAttribute("category",category);
        model.addAttribute("mediaService",mediaService);
        model.addAttribute("type","search");
        return "Job/job_search_result";
    }

    @GetMapping("/{job_id}/view")
    public String getJobDetailsPage(@PathVariable("job_id") Long job_id,Model model,Authentication authentication){
        JobOffer jobOffer = jobOfferService.getJobById(job_id);
        EmployerProfile employerProfile = employerProfileService.getEmployerProfileByCompanyId(jobOffer.getCompany().getId());
        if (jobOffer==null || !jobOffer.getActive()){
            return "redirect:/";
        }
        User user;
        if (authentication!=null){
            user = (User) authentication.getPrincipal();
            model.addAttribute("role",user.getRole().toString());
        }
        else user = null;
        model.addAttribute("user",user);
        model.addAttribute("type","job_details");
        model.addAttribute("job",jobOffer);
        model.addAttribute("employerProfile",employerProfile);
        model.addAttribute("mediaService",mediaService);
        model.addAttribute("candidateProfileService",candidateProfileService);
        model.addAttribute("now", LocalDateTime.now());
        model.addAttribute("cronoUnit", ChronoUnit.DAYS);
        return "Job/job_details";
    }
}
