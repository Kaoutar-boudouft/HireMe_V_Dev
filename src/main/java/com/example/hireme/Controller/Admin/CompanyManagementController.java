package com.example.hireme.Controller.Admin;

import com.example.hireme.Model.Entity.*;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Requests.Admin.UpdateAdminProfileRequest;
import com.example.hireme.Requests.Candidate.UpdateCandidateProfileRequest;
import com.example.hireme.Requests.EmailUpdateRequest;
import com.example.hireme.Requests.Employer.UpdateEmployerCompanyRequest;
import com.example.hireme.Requests.PasswordUpdateRequest;
import com.example.hireme.Service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/companies")
public class CompanyManagementController {

    private final CompanyService companyService;
    private final CountryService countryService;
    private final CityService cityService;
    private final MediaService mediaService;
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final FileUploadService fileUploadService;
    private final LanguageConfig languageConfig;

    @GetMapping("/{type}")
    public String getCompaniesPage(@PathVariable("type") String type, Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        if (type.equals("all")){
            List<Company> companies = companyService.getAll();
            model.addAttribute("companies",companies);
        }
        else if (type.equals("to_validate")){
            List<Company> companies = companyService.getCompaniesByActive(false);
            model.addAttribute("companies",companies);
        }
        else {
            return "redirect:/admin/companies/all";
        }
        model.addAttribute("user",user);
        model.addAttribute("type","dashboard");
        return "Admin/companies";
    }

    @GetMapping("/{company_id}/edit")
    public String getCompanyUpdatePage(@PathVariable("company_id") Long company_id, Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        Optional<Company> company = companyService.findById(company_id);
        if (company.isPresent()){
            UpdateEmployerCompanyRequest updateEmployerCompanyRequest = companyService.prepareUpdateCompanyRequest(company.get().getEmployerProfile());
            model = getCommAttr(model,company.get(),updateEmployerCompanyRequest,user);
            return "Admin/update_company";
        }
        return "redirect:/admin/companies/all";
    }

    @PostMapping("/{company_id}/update")
    public String updateCompany(@PathVariable("company_id") Long company_id,Authentication authentication , @Valid UpdateEmployerCompanyRequest updateEmployerCompanyRequest,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model) {
        User user = (User) authentication.getPrincipal();
        Optional<Company> company = companyService.findById(company_id);
        if (company.isPresent()) {
            model = getCommAttr(model,company.get(),updateEmployerCompanyRequest,user);
            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("error", bindingResult);
                redirectAttributes.addFlashAttribute("updateEmployerCompanyRequest", updateEmployerCompanyRequest);
                return "Admin/update_company";
            }
            companyService.updateEmployerCompany(updateEmployerCompanyRequest,company.get().getEmployerProfile().getId());
            try {
                Media checkMedia = mediaService.getMedia("Company", company.get().getId(), "company_logo");
                Media media;
                if (checkMedia != null) {
                    media = checkMedia;
                } else {
                    media = new Media("Company",  company.get().getId(), "company_logo");
                }
                fileUploadService.uploadFile(updateEmployerCompanyRequest.getFile(), media, locale);
            } catch (IOException e) {
                model.addAttribute("file_error", e.getMessage());
                redirectAttributes.addFlashAttribute("updateEmployerCompanyRequest", updateEmployerCompanyRequest);
                return "Admin/update_company";
            }
            redirectAttributes.addFlashAttribute("success", languageConfig.messageSource().getMessage("update_profile", new Object[]{}, locale));
            return "redirect:/admin/companies/"+company_id+"/edit";
        }
        return "redirect:/admin/companies/all";
    }

    public Model getCommAttr(Model model,Company company,UpdateEmployerCompanyRequest updateEmployerCompanyRequest, User user){
        List<Country> countries = countryService.getActiveCountries();
        List<City> cities = cityService.getActiveCitiesByCountry(company.getId());
        Media media = mediaService.getMedia("Company",
                company.getId(),"company_logo");
        model.addAttribute("user",user);
        model.addAttribute("company_id",company.getId());
        model.addAttribute("countries",countries);
        model.addAttribute("cities",cities);
        model.addAttribute("media",media);
        model.addAttribute("type","dashboard");
        model.addAttribute("updateEmployerCompanyRequest",updateEmployerCompanyRequest);
        return model;
    }

    @GetMapping("/delete/{company_id}")
    public String deleteCompany(@PathVariable("company_id") Long company_id,
                                RedirectAttributes redirectAttributes,Locale locale,Model model){
        Optional<Company> company = companyService.findById(company_id);
        if (company.isPresent()){
            mediaService.deleteMedia(new Media("Company",company.get().getId(),"company_logo"));
            Optional<VerificationToken> verificationToken = verificationTokenService.findByUserId(company.get().getEmployerProfile().getUser().getId());
            if (verificationToken.isPresent()){
                verificationTokenService.remove(verificationToken.get());
            }
            userService.removeUser(company.get().getEmployerProfile().getUser());
            redirectAttributes.addAttribute("successMessage",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
        }
        return "redirect:/admin/companies/all";
    }


}
