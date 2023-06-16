package com.example.hireme.Controller.Candidate;

import com.example.hireme.Model.Entity.JobOffer;
import com.example.hireme.Model.Entity.Media;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Service.JobOfferService;
import com.example.hireme.Service.MediaService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/candidate/applies")
public class AppliesController {

    private final JobOfferService jobOfferService;
    private final MediaService mediaService;

    @GetMapping("")
    public String getAppliesPage(@RequestParam(name="page_number",required=false,defaultValue ="1") Long page_number, Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        List<JobOffer> jobOffers = jobOfferService.getCandidateCandidatures(user.getId());
        if (page_number > 1 && jobOffers.size()<(page_number-1)*4+1){
            return "redirect:/candidate/applies";
        }
        long start = (page_number-1)*4;
        long end = start+4;
        int totalPages = (int) Math.ceil((double) jobOffers.size() / 4);
        List<JobOffer> paginatedJobsList = jobOfferService.getCandidateCandidaturesWithPagination(user.getId(),start,end);
        Media media = mediaService.getMedia("Company",user.getId(),"company_logo");
        model.addAttribute("user",user);
        model.addAttribute("media", media);
        model.addAttribute("type", "profile");
        model.addAttribute("jobOffers", paginatedJobsList);
        model.addAttribute("page",page_number);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("now", LocalDateTime.now());
        model.addAttribute("cronoUnit", ChronoUnit.DAYS);
        return "Candidate/applies";
    }
}
