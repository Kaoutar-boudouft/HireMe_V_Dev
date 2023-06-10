package com.example.hireme.Controller.Candidate;

import com.example.hireme.Model.Entity.CandidateProfile;
import com.example.hireme.Model.Entity.JobOffer;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Service.CandidateProfileService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/candidate/applies")
public class AppliesController {

    private final CandidateProfileService candidateProfileService;

    @GetMapping("")
    public String getAppliesPage(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        CandidateProfile candidateProfile = candidateProfileService.getCandidateProfile(user.getId());
        List<JobOffer> jobOffers = candidateProfile.getJob_offers();
        model.addAttribute("user",user);
        model.addAttribute("type", "profile");
        model.addAttribute("jobOffers", jobOffers);
        return "Candidate/applies";
    }
}
