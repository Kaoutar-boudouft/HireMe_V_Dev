package com.example.hireme.Controller.Admin;

import com.example.hireme.Events.RegistrationSuccessEvent;
import com.example.hireme.Exceptions.UserAlreadyExistException;
import com.example.hireme.Model.Entity.*;
import com.example.hireme.Model.Role;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Requests.Admin.CreateAdminRequest;
import com.example.hireme.Requests.Admin.UpdateAdminProfileRequest;
import com.example.hireme.Requests.Candidate.UpdateCandidateProfileRequest;
import com.example.hireme.Requests.EmailUpdateRequest;
import com.example.hireme.Requests.Employer.UpdateEmployerProfileRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/users")
public class UserManagementController {

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
    private final VerificationTokenService verificationTokenService;

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
        model.addAttribute("user_type",type);
        model.addAttribute("type","dashboard");
        return "Admin/users";
    }

   //candidate routes :
    @GetMapping("/candidate/{candidate_id}/edit")
    public String getCandidateUpdatePage(@PathVariable("candidate_id") Long candidate_id, Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        CandidateProfile candidateProfile = candidateProfileService.getCandidateProfileById(candidate_id);
        EmailUpdateRequest emailUpdateRequest = new EmailUpdateRequest(candidateProfile.getUser().getEmail());
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest();
        UpdateCandidateProfileRequest updateCandidateProfileRequest = candidateProfileService.prepareUpdateCandidateRequest(candidateProfile);
        model = getCandidateCommAttr(model,updateCandidateProfileRequest,candidate_id,user);
        model.addAttribute("emailUpdateRequest",emailUpdateRequest);
        model.addAttribute("passwordUpdateRequest",passwordUpdateRequest);
        model.addAttribute("updateCandidateProfileRequest",updateCandidateProfileRequest);
        return "Admin/update_candidate";
    }
    @PostMapping("/candidate/{user_id}/update_email")
    public String candidateEmailUpdate(Authentication authentication,@PathVariable("user_id") Long user_id, @Valid EmailUpdateRequest emailUpdateRequest,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model,
                              final HttpServletRequest httpServletRequest){
        User user = (User) authentication.getPrincipal();
        CandidateProfile candidateProfile = candidateProfileService.getCandidateProfileById(user_id);
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest();
        UpdateCandidateProfileRequest updateCandidateProfileRequest = candidateProfileService.prepareUpdateCandidateRequest(candidateProfile);
        model = getCandidateCommAttr(model,updateCandidateProfileRequest,user_id,user);
        model.addAttribute("passwordUpdateRequest",passwordUpdateRequest);
        model.addAttribute("updateCandidateProfileRequest",updateCandidateProfileRequest);
        redirectAttributes.addFlashAttribute("emailUpdateRequest", emailUpdateRequest);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            return "Admin/update_candidate";
        }
        try {
            User u = userService.updateUserEmail(candidateProfile.getUser(),emailUpdateRequest);
            if (u!=null){
                publisher.publishEvent(new RegistrationSuccessEvent(u,appService.appUrl(httpServletRequest),locale));
                redirectAttributes.addFlashAttribute("success","Email updated successfully ! please verify it");
                return "redirect:/admin/users/candidate/"+user_id+"/edit";
            }
            redirectAttributes.addFlashAttribute("errorMessage","update error");
            return "redirect:/admin/users/candidate/"+user_id+"/edit";
        }
        catch (UserAlreadyExistException e){
            redirectAttributes.addFlashAttribute("warning",languageConfig.messageSource().getMessage("email_exists",new Object[] {}, locale));
            return "redirect:/admin/users/candidate/"+user_id+"/edit";
        }
    }

    @PostMapping("/candidate/{user_id}/update_password")
    public String candidatePasswordUpdate(Authentication authentication,@PathVariable("user_id") Long user_id, @Valid PasswordUpdateRequest passwordUpdateRequest,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model,
                              final HttpServletRequest httpServletRequest){
        User user = (User) authentication.getPrincipal();
        CandidateProfile candidateProfile = candidateProfileService.getCandidateProfileById(user_id);
        EmailUpdateRequest emailUpdateRequest = new EmailUpdateRequest(candidateProfile.getUser().getEmail());
        UpdateCandidateProfileRequest updateCandidateProfileRequest = candidateProfileService.prepareUpdateCandidateRequest(candidateProfile);
        model = getCandidateCommAttr(model,updateCandidateProfileRequest,user_id,user);
        model.addAttribute("emailUpdateRequest",emailUpdateRequest);
        model.addAttribute("updateCandidateProfileRequest",updateCandidateProfileRequest);
        redirectAttributes.addFlashAttribute("passwordUpdateRequest", passwordUpdateRequest);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            return "Admin/update_candidate";
        }
        redirectAttributes.addFlashAttribute("success",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
        return "redirect:/admin/users/candidate/"+user_id+"/edit";
    }

    @PostMapping("/candidate/{candidate_id}/update_profile")
    public String candidateProfileUpdate(Authentication authentication,@PathVariable("candidate_id") Long candidate_id, @Valid UpdateCandidateProfileRequest updateCandidateProfileRequest,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model,
                                 final HttpServletRequest httpServletRequest){
        User user = (User) authentication.getPrincipal();
        CandidateProfile candidateProfile = candidateProfileService.getCandidateProfileById(candidate_id);
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest();
        EmailUpdateRequest emailUpdateRequest = new EmailUpdateRequest(candidateProfile.getUser().getEmail());
        model = getCandidateCommAttr(model,updateCandidateProfileRequest,candidate_id,user);
        model.addAttribute("emailUpdateRequest",emailUpdateRequest);
        model.addAttribute("passwordUpdateRequest", passwordUpdateRequest);
        redirectAttributes.addFlashAttribute("updateCandidateProfileRequest", updateCandidateProfileRequest);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult.getFieldError().toString());
            return "Admin/update_candidate";
        }
        candidateProfileService.updateCandidateProfile(updateCandidateProfileRequest,candidateProfile.getUser().getId());
        try {
            Media checkMedia = mediaService.getMedia("CandidateProfile",candidateProfile.getId(),"cv");
            Media media;
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

    public Model getCandidateCommAttr(Model model,UpdateCandidateProfileRequest updateCandidateProfileRequest,Long user_id,User user){
        List<Country> countries = countryService.getActiveCountries();
        List<City> cities = cityService.getActiveCitiesByCountry(updateCandidateProfileRequest.getCountry());
        Media media = mediaService.getMedia("CandidateProfile", user_id,"cv");
        model.addAttribute("user",user);
        model.addAttribute("countries",countries);
        model.addAttribute("cities",cities);
        model.addAttribute("media",media);
        model.addAttribute("type","dashboard");
        model.addAttribute("user_id",user_id);
        return model;
    }

    // candidate routes end

    //employer routes :
    @GetMapping("/employer/{employer_id}/edit")
    public String getEmployerUpdatePage(@PathVariable("employer_id") Long employer_id, Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        EmployerProfile employerProfile = employerProfileService.getEmployerProfileById(employer_id);
        EmailUpdateRequest emailUpdateRequest = new EmailUpdateRequest(employerProfile.getUser().getEmail());
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest();
        UpdateEmployerProfileRequest updateEmployerProfileRequest = employerProfileService.prepareUpdateEmployerRequest(employerProfile);
        model = getEmployerCommAttr(model,updateEmployerProfileRequest,employer_id,user);
        model.addAttribute("emailUpdateRequest",emailUpdateRequest);
        model.addAttribute("passwordUpdateRequest",passwordUpdateRequest);
        model.addAttribute("updateEmployerProfileRequest",updateEmployerProfileRequest);
        return "Admin/update_employer";
    }

    @PostMapping("/employer/{user_id}/update_password")
    public String employerPasswordUpdate(Authentication authentication,@PathVariable("user_id") Long user_id, @Valid PasswordUpdateRequest passwordUpdateRequest,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model,
                                 final HttpServletRequest httpServletRequest){
        User user = (User) authentication.getPrincipal();
        EmployerProfile employerProfile = employerProfileService.getEmployerProfileById(user_id);
        EmailUpdateRequest emailUpdateRequest = new EmailUpdateRequest(employerProfile.getUser().getEmail());
        UpdateEmployerProfileRequest updateEmployerProfileRequest = employerProfileService.prepareUpdateEmployerRequest(employerProfile);
        model = getEmployerCommAttr(model,updateEmployerProfileRequest,user_id,user);
        model.addAttribute("emailUpdateRequest",emailUpdateRequest);
        model.addAttribute("updateEmployerProfileRequest",updateEmployerProfileRequest);
        redirectAttributes.addFlashAttribute("passwordUpdateRequest", passwordUpdateRequest);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            return "Admin/update_employer";
        }
        redirectAttributes.addFlashAttribute("success",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
        return "redirect:/admin/users/employer/"+user_id+"/edit";
    }
    @PostMapping("/employer/{user_id}/update_email")
    public String employerEmailUpdate(Authentication authentication,@PathVariable("user_id") Long user_id, @Valid EmailUpdateRequest emailUpdateRequest,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model,
                              final HttpServletRequest httpServletRequest){
        User user = (User) authentication.getPrincipal();
        EmployerProfile employerProfile = employerProfileService.getEmployerProfileById(user_id);
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest();
        UpdateEmployerProfileRequest updateEmployerProfileRequest = employerProfileService.prepareUpdateEmployerRequest(employerProfile);
        model = getEmployerCommAttr(model,updateEmployerProfileRequest,user_id,user);
        model.addAttribute("passwordUpdateRequest",passwordUpdateRequest);
        model.addAttribute("updateEmployerProfileRequest",updateEmployerProfileRequest);
        redirectAttributes.addFlashAttribute("emailUpdateRequest", emailUpdateRequest);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            return "Admin/update_employer";
        }
        try {
            User u = userService.updateUserEmail(employerProfile.getUser(),emailUpdateRequest);
            if (u!=null){
                publisher.publishEvent(new RegistrationSuccessEvent(u,appService.appUrl(httpServletRequest),locale));
                redirectAttributes.addFlashAttribute("success","Email updated successfully ! please verify it");
                return "redirect:/admin/users/employer/"+user_id+"/edit";
            }
            redirectAttributes.addFlashAttribute("errorMessage","update error");
            return "redirect:/admin/users/employer/"+user_id+"/edit";
        }
        catch (UserAlreadyExistException e){
            redirectAttributes.addFlashAttribute("warning",languageConfig.messageSource().getMessage("email_exists",new Object[] {}, locale));
            return "redirect:/admin/users/employer/"+user_id+"/edit";
        }
    }

    @PostMapping("/employer/{employer_id}/update_profile")
    public String employerProfileUpdate(Authentication authentication,@PathVariable("employer_id") Long employer_id, @Valid UpdateEmployerProfileRequest updateEmployerProfileRequest,
                                        BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model) {
        User user = (User) authentication.getPrincipal();
        EmployerProfile employerProfile = employerProfileService.getEmployerProfileById(employer_id);
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest();
        EmailUpdateRequest emailUpdateRequest = new EmailUpdateRequest(employerProfile.getUser().getEmail());
        model = getEmployerCommAttr(model,updateEmployerProfileRequest,employer_id,user);
        model.addAttribute("emailUpdateRequest",emailUpdateRequest);
        model.addAttribute("passwordUpdateRequest", passwordUpdateRequest);
        redirectAttributes.addFlashAttribute("updateEmployerProfileRequest", updateEmployerProfileRequest);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            return "Admin/update_employer";
        }
        employerProfileService.updateEmployerProfile(updateEmployerProfileRequest,employer_id);
        redirectAttributes.addFlashAttribute("success",languageConfig.messageSource().getMessage("update_profile",new Object[] {}, locale));
        return "redirect:/admin/users/employer/"+employer_id+"/edit";
    }

    public Model getEmployerCommAttr(Model model,UpdateEmployerProfileRequest updateEmployerProfileRequest,Long user_id,User user){
        model.addAttribute("user",user);
        model.addAttribute("type","dashboard");
        model.addAttribute("user_id",user_id);
        return model;
    }

    //employer routes end

    //admin routes :

    @GetMapping("/admin/{admin_id}/edit")
    public String getAdminUpdatePage(@PathVariable("admin_id") Long admin_id, Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        AdminProfile adminProfile = adminProfileService.getAdminProfileById(admin_id);
        EmailUpdateRequest emailUpdateRequest = new EmailUpdateRequest(adminProfile.getUser().getEmail());
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest();
        UpdateAdminProfileRequest updateAdminProfileRequest = adminProfileService.prepareUpdateAdminRequest(adminProfile);
        model = getAdminCommAttr(model,updateAdminProfileRequest,admin_id,user);
        model.addAttribute("emailUpdateRequest",emailUpdateRequest);
        model.addAttribute("passwordUpdateRequest",passwordUpdateRequest);
        model.addAttribute("updateAdminProfileRequest",updateAdminProfileRequest);
        return "Admin/update_admin";
    }
    @PostMapping("/admin/{user_id}/update_email")
    public String adminEmailUpdate(Authentication authentication,@PathVariable("user_id") Long user_id, @Valid EmailUpdateRequest emailUpdateRequest,
                                       BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model,
                                       final HttpServletRequest httpServletRequest){
        User user = (User) authentication.getPrincipal();
        AdminProfile adminProfile = adminProfileService.getAdminProfileById(user_id);
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest();
        UpdateAdminProfileRequest updateAdminProfileRequest = adminProfileService.prepareUpdateAdminRequest(adminProfile);
        model = getAdminCommAttr(model,updateAdminProfileRequest,user_id,user);
        model.addAttribute("passwordUpdateRequest",passwordUpdateRequest);
        model.addAttribute("updateAdminProfileRequest",updateAdminProfileRequest);
        redirectAttributes.addFlashAttribute("emailUpdateRequest", emailUpdateRequest);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            return "Admin/update_admin";
        }
        try {
            User u = userService.updateUserEmail(adminProfile.getUser(),emailUpdateRequest);
            if (u!=null){
                publisher.publishEvent(new RegistrationSuccessEvent(u,appService.appUrl(httpServletRequest),locale));
                redirectAttributes.addFlashAttribute("success","Email updated successfully ! please verify it");
                return "redirect:/admin/users/admin/"+user_id+"/edit";
            }
            redirectAttributes.addFlashAttribute("errorMessage","update error");
            return "redirect:/admin/users/admin/"+user_id+"/edit";
        }
        catch (UserAlreadyExistException e){
            redirectAttributes.addFlashAttribute("warning",languageConfig.messageSource().getMessage("email_exists",new Object[] {}, locale));
            return "redirect:/admin/users/admin/"+user_id+"/edit";
        }
    }

    @PostMapping("/admin/{user_id}/update_password")
    public String adminPasswordUpdate(Authentication authentication,@PathVariable("user_id") Long user_id, @Valid PasswordUpdateRequest passwordUpdateRequest,
                                          BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model,
                                          final HttpServletRequest httpServletRequest){
        User user = (User) authentication.getPrincipal();
        AdminProfile adminProfile = adminProfileService.getAdminProfileById(user_id);
        EmailUpdateRequest emailUpdateRequest = new EmailUpdateRequest(adminProfile.getUser().getEmail());
        UpdateAdminProfileRequest updateAdminProfileRequest = adminProfileService.prepareUpdateAdminRequest(adminProfile);
        model = getAdminCommAttr(model,updateAdminProfileRequest,user_id,user);
        model.addAttribute("emailUpdateRequest",emailUpdateRequest);
        model.addAttribute("updateAdminProfileRequest",updateAdminProfileRequest);
        redirectAttributes.addFlashAttribute("passwordUpdateRequest", passwordUpdateRequest);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            return "Admin/update_admin";
        }
        redirectAttributes.addFlashAttribute("success",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
        return "redirect:/admin/users/admin/"+user_id+"/edit";
    }

    @PostMapping("/admin/{admin_id}/update_profile")
    public String adminProfileUpdate(Authentication authentication,@PathVariable("admin_id") Long admin_id, @Valid UpdateAdminProfileRequest updateAdminProfileRequest,
                                         BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model,
                                         final HttpServletRequest httpServletRequest){
        User user = (User) authentication.getPrincipal();
        AdminProfile adminProfile = adminProfileService.getAdminProfileById(admin_id);
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest();
        EmailUpdateRequest emailUpdateRequest = new EmailUpdateRequest(adminProfile.getUser().getEmail());
        model = getAdminCommAttr(model,updateAdminProfileRequest,admin_id,user);
        model.addAttribute("emailUpdateRequest",emailUpdateRequest);
        model.addAttribute("passwordUpdateRequest", passwordUpdateRequest);
        redirectAttributes.addFlashAttribute("updateAdminProfileRequest", updateAdminProfileRequest);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult.getFieldError().toString());
            return "Admin/update_admin";
        }
        adminProfileService.updateAdminProfile(updateAdminProfileRequest,adminProfile.getUser().getId());
        redirectAttributes.addFlashAttribute("success",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
        return "redirect:/admin/users/admin/"+admin_id+"/edit";
    }

    public Model getAdminCommAttr(Model model,UpdateAdminProfileRequest updateAdminProfileRequest,Long user_id,User user){
        List<Country> countries = countryService.getActiveCountries();
        List<City> cities = cityService.getActiveCitiesByCountry(updateAdminProfileRequest.getCountry());
        Media media = mediaService.getMedia("CandidateProfile", user_id,"cv");
        model.addAttribute("user",user);
        model.addAttribute("countries",countries);
        model.addAttribute("cities",cities);
        model.addAttribute("media",media);
        model.addAttribute("type","dashboard");
        model.addAttribute("user_id",user_id);
        return model;
    }

    @GetMapping("/admin/create")
    public String getAdminCreatePage(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        CreateAdminRequest createAdminRequest = new CreateAdminRequest();
        List<Country> countries = countryService.getActiveCountries();
        List<City> cities = new ArrayList<City>();
        model.addAttribute("createAdminRequest",createAdminRequest);
        model.addAttribute("countries",countries);
        model.addAttribute("cities",cities);
        model.addAttribute("user",user);
        model.addAttribute("type","dashboard");
        return "Admin/create_admin";
    }

    @PostMapping("/admin/store")
    public String registerAdmin(Authentication authentication,@Valid CreateAdminRequest createAdminRequest,
                                                  BindingResult bindingResult,
                                                  final HttpServletRequest httpServletRequest,
                                                  RedirectAttributes redirectAttributes,Locale locale,Model model){
        User user = (User) authentication.getPrincipal();
        List<Country> countries = countryService.getActiveCountries();
        List<City> cities = cityService.getActiveCitiesByCountry(createAdminRequest.getCountry());
        redirectAttributes.addFlashAttribute("createAdminRequest",createAdminRequest);
        model.addAttribute("countries",countries);
        model.addAttribute("cities",cities);
        model.addAttribute("user",user);
        model.addAttribute("type","dashboard");
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            return "Admin/create_admin";
        }
        else {
            try {
                User newUser = userService.registerAdminUser(createAdminRequest);
                if (newUser!=null){
                    publisher.publishEvent(new RegistrationSuccessEvent(newUser,appService.appUrl(httpServletRequest),locale));
                    redirectAttributes.addFlashAttribute("success",languageConfig.messageSource().getMessage("create",new Object[] {}, locale));
                    return "redirect:/admin/users/admin";
                }
                else {
                    redirectAttributes.addFlashAttribute("errorMessage","creation error");
                    return "redirect:/admin/users/admin/create";
                }
            }
            catch (UserAlreadyExistException e){
                model.addAttribute("createAdminRequest",createAdminRequest);
                model.addAttribute("warning",languageConfig.messageSource().getMessage("email_exists",new Object[] {}, locale));
                return "Admin/create_admin";
            }
        }

    }

    @GetMapping("/delete/{user_id}")
    public String deleteUser(@PathVariable("user_id") Long user_id,
                                RedirectAttributes redirectAttributes,Locale locale,Model model){
        String role = userService.getById(user_id).getRole().name().toLowerCase();
        Optional<VerificationToken> verificationToken = verificationTokenService.findByUserId(user_id);
        if (verificationToken.isPresent()){
            verificationTokenService.remove(verificationToken.get());
        }
        userService.removeUser(userService.getById(user_id));
        model.addAttribute("successMessage",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
        return "redirect:/admin/users/"+role;
    }

}
