package com.example.hireme.Controller.Candidate;

import com.example.hireme.Model.Entity.*;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Requests.Candidate.UpdateCandidateProfileRequest;
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
import java.time.temporal.ChronoUnit;
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
    private final FileUploadService fileUploadService;
    private final MediaService mediaService;


    @GetMapping("/profile")
    public String getCandidateProfile(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        UpdateCandidateProfileRequest updateCandidateProfileRequest = candidateProfileService.prepareUpdateCandidateRequest(
                candidateProfileService.getCandidateProfile(user.getId()));
        model = getCommunAttr(model,updateCandidateProfileRequest,user);
        model.addAttribute("updateCandidateProfileRequest",updateCandidateProfileRequest);
        model.addAttribute("selected", "profile");
        return "Candidate/profile";
    }

    @PostMapping("/profile/update")
    public String updateCandidate(Authentication authentication , @Valid UpdateCandidateProfileRequest updateCandidateProfileRequest,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model) throws IOException {
        User user = (User) authentication.getPrincipal();
        model = getCommunAttr(model,updateCandidateProfileRequest,user);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            redirectAttributes.addFlashAttribute("updateCandidateProfileRequest", updateCandidateProfileRequest);
            return "Candidate/profile";
        }
        candidateProfileService.updateCandidateProfile(updateCandidateProfileRequest,user.getId());
        try {
            Media checkMedia = mediaService.getMedia("CandidateProfile",candidateProfileService.getCandidateProfile(user.getId()).getId(),"cv");
            Media media;
            if (checkMedia!=null){
                media = checkMedia;
            }
            else {
                media = new Media("CandidateProfile",candidateProfileService.getCandidateProfile(user.getId()).getId(),"cv");
            }
            fileUploadService.uploadFile(updateCandidateProfileRequest.getFile(),media,locale);
        }
        catch (IOException e){
            model.addAttribute("file_error", e.getMessage());
            redirectAttributes.addFlashAttribute("updateCandidateProfileRequest", updateCandidateProfileRequest);
            return "Candidate/profile";
        }
        redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update_profile",new Object[] {}, locale));
        return "redirect:/candidate/profile";
    }


    public Model getCommunAttr(Model model,UpdateCandidateProfileRequest updateCandidateProfileRequest,User user){
        List<Country> countries = countryService.getActiveCountries();
        List<City> cities = cityService.getActiveCitiesByCountry(updateCandidateProfileRequest.getCountry());
        Media media = mediaService.getMedia("CandidateProfile",
                candidateProfileService.getCandidateProfile(user.getId()).getId(),"cv");
        model.addAttribute("user",user);
        model.addAttribute("countries",countries);
        model.addAttribute("cities",cities);
        model.addAttribute("media",media);
        model.addAttribute("type", "profile");
        model.addAttribute("selected", "profile");
        return model;
    }
}
