package com.example.hireme.Controller.Admin;

import com.example.hireme.Events.Listener.RegisterSuccessEventListener;
import com.example.hireme.Events.RegistrationSuccessEvent;
import com.example.hireme.Exceptions.UserAlreadyExistException;
import com.example.hireme.Model.Entity.*;
import com.example.hireme.Model.Profile;
import com.example.hireme.Model.Role;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Requests.Candidate.UpdateCandidateProfileRequest;
import com.example.hireme.Requests.EmailUpdateRequest;
import com.example.hireme.Requests.PasswordUpdateRequest;
import com.example.hireme.Service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final CandidateProfileService candidateProfileService;
    private final EmployerProfileService employerProfileService;
    private final AdminProfileService adminProfileService;
    private final CountryService countryService;
    private final CityService cityService;
    private final MediaService mediaService;
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final AppService appService;
    private final LanguageConfig languageConfig;
    private final FileUploadService fileUploadService;

    @GetMapping("/{type}")
    public String getUsersPage(@PathVariable("type") String type, Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        if (type.equals(Role.CANDIDATE.name().toLowerCase())) {
            List<CandidateProfile> profiles = candidateProfileService.getAll();
            model.addAttribute("profiles",profiles);
        }
        else if (type.equals(Role.EMPLOYER.name().toLowerCase())){
            List<EmployerProfile> profiles = employerProfileService.getAll();
            model.addAttribute("profiles",profiles);
        }
        else if (type.equals(Role.ADMIN.name().toLowerCase())){
            List<AdminProfile> profiles = adminProfileService.getAll();
            model.addAttribute("profiles",profiles);

        }
        else {
            return "redirect:/admin/users/candidate";
        }
        model.addAttribute("user",user);
        model.addAttribute("type","dashboard");
        return "Admin/users";
    }
    @GetMapping("/candidate/{candidate_id}/edit")
    public String getCandidateUpdatePage(@PathVariable("candidate_id") Long candidate_id, Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        CandidateProfile candidateProfile = candidateProfileService.getCandidateProfile(candidate_id);
        EmailUpdateRequest emailUpdateRequest = new EmailUpdateRequest(candidateProfile.getUser().getEmail());
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest();
        UpdateCandidateProfileRequest updateCandidateProfileRequest = candidateProfileService.prepareUpdateCandidateRequest(candidateProfile);
        List<Country> countries = countryService.getActiveCountries();
        List<City> cities = cityService.getActiveCitiesByCountry(updateCandidateProfileRequest.getCountry());
        Media media = mediaService.getMedia("CandidateProfile", candidate_id,"cv");
        model.addAttribute("user",user);
        model.addAttribute("countries",countries);
        model.addAttribute("cities",cities);
        model.addAttribute("media",media);
        model.addAttribute("emailUpdateRequest",emailUpdateRequest);
        model.addAttribute("passwordUpdateRequest",passwordUpdateRequest);
        model.addAttribute("updateCandidateProfileRequest",updateCandidateProfileRequest);
        model.addAttribute("type","dashboard");
        model.addAttribute("user_id",candidate_id);
        return "Admin/update_candidate";
    }
    @PostMapping("/candidate/{candidate_id}/update_email")
    public String emailUpdate(Authentication authentication,@PathVariable("candidate_id") Long candidate_id, @Valid EmailUpdateRequest emailUpdateRequest,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model,
                              final HttpServletRequest httpServletRequest){
        User user = (User) authentication.getPrincipal();
        CandidateProfile candidateProfile = candidateProfileService.getCandidateProfile(candidate_id);
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest();
        UpdateCandidateProfileRequest updateCandidateProfileRequest = candidateProfileService.prepareUpdateCandidateRequest(candidateProfile);
        List<Country> countries = countryService.getActiveCountries();
        List<City> cities = cityService.getActiveCitiesByCountry(updateCandidateProfileRequest.getCountry());
        Media media = mediaService.getMedia("CandidateProfile", candidate_id,"cv");
        model.addAttribute("user",user);
        model.addAttribute("countries",countries);
        model.addAttribute("cities",cities);
        model.addAttribute("media",media);
        model.addAttribute("passwordUpdateRequest",passwordUpdateRequest);
        model.addAttribute("updateCandidateProfileRequest",updateCandidateProfileRequest);
        model.addAttribute("type","dashboard");
        model.addAttribute("user_id",candidate_id);
        redirectAttributes.addFlashAttribute("emailUpdateRequest", emailUpdateRequest);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            System.out.println("kaoutar error");
            return "Admin/update_candidate";
        }
        try {
            User u = userService.updateUserEmail(candidateProfile.getUser(),emailUpdateRequest);
            if (u!=null){
                publisher.publishEvent(new RegistrationSuccessEvent(u,appService.appUrl(httpServletRequest),locale));
                redirectAttributes.addFlashAttribute("success","Email updated successfully ! please verify it");
                return "redirect:/admin/users/candidate/"+candidate_id+"/edit";
            }
            redirectAttributes.addFlashAttribute("errorMessage","update error");
            return "redirect:/admin/users/candidate/"+candidate_id+"/edit";
        }
        catch (UserAlreadyExistException e){
            redirectAttributes.addFlashAttribute("warning",languageConfig.messageSource().getMessage("email_exists",new Object[] {}, locale));
            return "redirect:/admin/users/candidate/"+candidate_id+"/edit";
        }
    }

    @PostMapping("/candidate/{candidate_id}/update_password")
    public String passwordUpdate(Authentication authentication,@PathVariable("candidate_id") Long candidate_id, @Valid PasswordUpdateRequest passwordUpdateRequest,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model,
                              final HttpServletRequest httpServletRequest){
        User user = (User) authentication.getPrincipal();
        CandidateProfile candidateProfile = candidateProfileService.getCandidateProfile(candidate_id);
        EmailUpdateRequest emailUpdateRequest = new EmailUpdateRequest(candidateProfile.getUser().getEmail());
        UpdateCandidateProfileRequest updateCandidateProfileRequest = candidateProfileService.prepareUpdateCandidateRequest(candidateProfile);
        List<Country> countries = countryService.getActiveCountries();
        List<City> cities = cityService.getActiveCitiesByCountry(updateCandidateProfileRequest.getCountry());
        Media media = mediaService.getMedia("CandidateProfile", candidate_id,"cv");
        model.addAttribute("user",user);
        model.addAttribute("countries",countries);
        model.addAttribute("cities",cities);
        model.addAttribute("media",media);
        model.addAttribute("emailUpdateRequest",emailUpdateRequest);
        model.addAttribute("updateCandidateProfileRequest",updateCandidateProfileRequest);
        model.addAttribute("type","dashboard");
        model.addAttribute("user_id",candidate_id);
        redirectAttributes.addFlashAttribute("passwordUpdateRequest", passwordUpdateRequest);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            return "Admin/update_candidate";
        }
        redirectAttributes.addFlashAttribute("success",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
        return "redirect:/admin/users/candidate/"+candidate_id+"/edit";
    }

    @PostMapping("/candidate/{candidate_id}/update_profile")
    public String profileUpdate(Authentication authentication,@PathVariable("candidate_id") Long candidate_id, @Valid UpdateCandidateProfileRequest updateCandidateProfileRequest,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model,
                                 final HttpServletRequest httpServletRequest){
        User user = (User) authentication.getPrincipal();
        CandidateProfile candidateProfile = candidateProfileService.getCandidateProfile(candidate_id);
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest();
        EmailUpdateRequest emailUpdateRequest = new EmailUpdateRequest(candidateProfile.getUser().getEmail());
        List<Country> countries = countryService.getActiveCountries();
        List<City> cities = cityService.getActiveCitiesByCountry(updateCandidateProfileRequest.getCountry());
        Media media = mediaService.getMedia("CandidateProfile", candidate_id,"cv");
        model.addAttribute("user",user);
        model.addAttribute("countries",countries);
        model.addAttribute("cities",cities);
        model.addAttribute("media",media);
        model.addAttribute("emailUpdateRequest",emailUpdateRequest);
        model.addAttribute("type","dashboard");
        model.addAttribute("user_id",candidate_id);
        model.addAttribute("passwordUpdateRequest", passwordUpdateRequest);
        redirectAttributes.addFlashAttribute("updateCandidateProfileRequest", updateCandidateProfileRequest);
        if (bindingResult.hasErrors()){
            System.out.println("kaoutar error"+bindingResult);
            redirectAttributes.addFlashAttribute("error", bindingResult.getFieldError().toString());
            return "Admin/update_candidate";
        }
        candidateProfileService.updateCandidateProfile(updateCandidateProfileRequest,candidateProfile.getUser().getId());
        System.out.println("kaoutar"+updateCandidateProfileRequest.getActive());
        try {
            Media checkMedia = mediaService.getMedia("CandidateProfile",candidateProfile.getId(),"cv");
            if (checkMedia!=null){
                media = checkMedia;
            }
            else {
                media = new Media("CandidateProfile",candidateProfile.getId(),"cv");
            }
            fileUploadService.uploadFile(updateCandidateProfileRequest.getFile(),media,locale);
        }
        catch (IOException e){
            model.addAttribute("file_error", e.getMessage());
            redirectAttributes.addFlashAttribute("updateCandidateProfileRequest", updateCandidateProfileRequest);
            return "Admin/update_candidate";
        }
        redirectAttributes.addFlashAttribute("success",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
        return "redirect:/admin/users/candidate/"+candidate_id+"/edit";
    }
}
