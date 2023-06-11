package com.example.hireme.Controller.Employer;

import com.example.hireme.Model.Entity.City;
import com.example.hireme.Model.Entity.Country;
import com.example.hireme.Model.Entity.Media;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Requests.Candidate.UpdateCandidateProfileRequest;
import com.example.hireme.Requests.Employer.UpdateEmployerProfileRequest;
import com.example.hireme.Service.EmployerProfileService;
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
@RequestMapping("/employer")
public class EmployerController {
    private final EmployerProfileService employerProfileService;
    private final LanguageConfig languageConfig;
    @GetMapping("/profile")
    public String getEmployerProfile(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        UpdateEmployerProfileRequest updateEmployerProfileRequest = employerProfileService.prepareUpdateEmployerRequest(
                employerProfileService.getEmployerProfile(user.getId()));
        model = getCommunAttr(model,user);
        model.addAttribute("updateEmployerProfileRequest",updateEmployerProfileRequest);
        return "Employer/profile";
    }

    @PostMapping("/profile/update")
    public String updateEmployer(Authentication authentication , @Valid UpdateEmployerProfileRequest updateEmployerProfileRequest,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model) {
        User user = (User) authentication.getPrincipal();
        model = getCommunAttr(model,user);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            redirectAttributes.addFlashAttribute("updateEmployerProfileRequest", updateEmployerProfileRequest);
            return "Employer/profile";
        }
        employerProfileService.updateEmployerProfile(updateEmployerProfileRequest,user.getId());
        redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update_profile",new Object[] {}, locale));
        return "redirect:/employer/profile";
    }

    public Model getCommunAttr(Model model,User user){
        model.addAttribute("user",user);
        model.addAttribute("type", "profile");
        return model;
    }


}
