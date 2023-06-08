package com.example.hireme.Controller;

import com.example.hireme.Model.Entity.CandidateProfile;
import com.example.hireme.Model.Entity.City;
import com.example.hireme.Model.Entity.Country;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Requests.UpdateCandidateProfileRequest;
import com.example.hireme.Service.CandidateProfileService;
import com.example.hireme.Service.CityService;
import com.example.hireme.Service.CountryService;
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

import java.util.List;
import java.util.Locale;

@Controller
@AllArgsConstructor
@RequestMapping("/candidate")
public class CandidateController {

    private final CandidateProfileService candidateProfileService;
    private final CountryService countryService;

    private final CityService cityService;
    private final LanguageConfig languageConfig;


    @GetMapping("/profile")
    public String getCandidateProfile(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        UpdateCandidateProfileRequest updateCandidateProfileRequest = candidateProfileService.prepareUpdateCandidateRequest(
                candidateProfileService.getCandidateProfile(user.getId()));
        List<Country> countries = countryService.getActiveCountries();
        List<City> cities = cityService.getActiveCitiesByCountry(updateCandidateProfileRequest.getCountry());
        model.addAttribute("updateCandidateProfileRequest",updateCandidateProfileRequest);
        model.addAttribute("countries",countries);
        model.addAttribute("cities",cities);
        model.addAttribute("type", "profile");
        model.addAttribute("user",user);
        return "Candidate/profile";
    }

    @PostMapping("/profile/update")
    public String updateCandidate(Authentication authentication ,@Valid UpdateCandidateProfileRequest updateCandidateProfileRequest,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale,Model model){
        if (bindingResult.hasErrors()){
            List<Country> countries = countryService.getActiveCountries();
            List<City> cities = cityService.getActiveCitiesByCountry(updateCandidateProfileRequest.getCountry());
            model.addAttribute("countries", countries);
            model.addAttribute("cities", cities);
            redirectAttributes.addFlashAttribute("error", bindingResult);
            redirectAttributes.addFlashAttribute("updateCandidateProfileRequest", updateCandidateProfileRequest);
            return "Candidate/profile";
        }
        User user = (User) authentication.getPrincipal();
        candidateProfileService.updateCandidateProfile(updateCandidateProfileRequest,user.getId());
        redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update_profile",new Object[] {}, locale));
        return "redirect:/candidate/profile";
    }
}
