package com.example.hireme.Controller.Employer;

import com.example.hireme.Model.Entity.City;
import com.example.hireme.Model.Entity.Country;
import com.example.hireme.Model.Entity.Media;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Requests.Candidate.UpdateCandidateProfileRequest;
import com.example.hireme.Requests.Employer.UpdateEmployerCompanyRequest;
import com.example.hireme.Requests.Employer.UpdateEmployerProfileRequest;
import com.example.hireme.Service.*;
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
@RequestMapping("/employer/company")
public class CompanyController {

    private final CountryService countryService;
    private final CompanyService companyService;
    private final CityService cityService;
    private final EmployerProfileService employerProfileService;
    private final LanguageConfig languageConfig;
    private final MediaService mediaService;
    private final FileUploadService fileUploadService;
    @GetMapping()
    public String getEmployerCompany(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        UpdateEmployerCompanyRequest updateEmployerCompanyRequest = companyService.prepareUpdateCompanyRequest(
                employerProfileService.getEmployerProfile(user.getId()));
        Media media = mediaService.getMedia("Company",
                employerProfileService.getEmployerProfile(user.getId()).getCompany().getId(),"company_logo");
        model.addAttribute("media",media);
        List<Country> countries = countryService.getActiveCountries();
        List<City> cities = cityService.getActiveCitiesByCountry(updateEmployerCompanyRequest.getCompany_country());
        model.addAttribute("user",user);
        model.addAttribute("type", "profile");
        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);
        model.addAttribute("updateEmployerCompanyRequest", updateEmployerCompanyRequest);
        return "Employer/company";
    }

    @PostMapping("/update")
    public String updateCompany(Authentication authentication , @Valid UpdateEmployerCompanyRequest updateEmployerCompanyRequest,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model) {
        User user = (User) authentication.getPrincipal();
        Media media1 = mediaService.getMedia("Company",
                employerProfileService.getEmployerProfile(user.getId()).getCompany().getId(),"company_logo");
        model.addAttribute("media",media1);
        List<Country> countries = countryService.getActiveCountries();
        List<City> cities = cityService.getActiveCitiesByCountry(updateEmployerCompanyRequest.getCompany_country());
        model.addAttribute("user",user);
        model.addAttribute("type", "profile");
        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            redirectAttributes.addFlashAttribute("updateEmployerCompanyRequest", updateEmployerCompanyRequest);
            return "Employer/company";
        }
        companyService.updateEmployerCompany(updateEmployerCompanyRequest,user.getId());
        try {
            Media checkMedia = mediaService.getMedia("Company",employerProfileService.getEmployerProfile(user.getId()).getCompany().getId(),"company_logo");
            Media media;
            if (checkMedia!=null){
                media = checkMedia;
            }
            else {
                media = new Media("Company",employerProfileService.getEmployerProfile(user.getId()).getCompany().getId(),"company_logo");
            }
            fileUploadService.uploadFile(updateEmployerCompanyRequest.getFile(),media,locale);
        }
        catch (IOException e){
            model.addAttribute("file_error", e.getMessage());
            redirectAttributes.addFlashAttribute("updateEmployerCompanyRequest", updateEmployerCompanyRequest);
            return "Employer/company";
        }
        redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update_profile",new Object[] {}, locale));
        return "redirect:/employer/company";
    }
}
