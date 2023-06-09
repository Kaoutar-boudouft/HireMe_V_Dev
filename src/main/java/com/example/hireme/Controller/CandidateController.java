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
import com.example.hireme.Service.FileUploadService;
import jakarta.mail.Multipart;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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
    public String updateCandidate(@RequestParam("file") MultipartFile file, Authentication authentication , @Valid UpdateCandidateProfileRequest updateCandidateProfileRequest,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model) throws IOException {
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
        try {
            fileUploadService.uploadFile(file);
        }
        catch (IOException e){
            redirectAttributes.addFlashAttribute("file_error", e.getMessage());
            redirectAttributes.addFlashAttribute("updateCandidateProfileRequest", updateCandidateProfileRequest);
            return "Candidate/profile";
        }
        redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update_profile",new Object[] {}, locale));
        return "redirect:/candidate/profile";
    }
}
