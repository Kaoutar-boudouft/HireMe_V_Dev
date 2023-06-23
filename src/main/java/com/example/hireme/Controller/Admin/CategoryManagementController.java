package com.example.hireme.Controller.Admin;

import com.example.hireme.Model.Currency;
import com.example.hireme.Model.Entity.*;
import com.example.hireme.Model.JobType;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Requests.Admin.CreateUpdateCategoryRequest;
import com.example.hireme.Requests.Employer.CreateUpdateJobRequest;
import com.example.hireme.Service.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryManagementController {

    private final JobOfferService jobOfferService;
    private final CountryService countryService;
    private final CityService cityService;
    private final CompanyService companyService;
    private final OfferCategoryService offerCategoryService;
    private final LanguageConfig languageConfig;
    private final VerificationTokenService verificationTokenService;
    private final EmployerProfileService employerProfileService;

    @GetMapping()
    public String getCategoriesPage(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        List<OfferCategory> categories = offerCategoryService.getAllCategories();
        model.addAttribute("user",user);
        model.addAttribute("categories",categories);
        model.addAttribute("type","dashboard");
        return "Admin/categories";
    }

    @GetMapping("/{category_id}/edit")
    public String getCategoryUpdatePage(@PathVariable("category_id") Long job_id, Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        Optional<OfferCategory> offerCategory = offerCategoryService.findById(job_id);
        if (offerCategory.isPresent()){
            CreateUpdateCategoryRequest createUpdateCategoryRequest = new CreateUpdateCategoryRequest(offerCategory.get().getLabel());
            model = getCommAttr(model,user,createUpdateCategoryRequest,null);
            return "Admin/update_create_category";
        }
        return "redirect:/admin/categories";
    }

   @GetMapping("/create")
    public String getCategoryCreatePage(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
            CreateUpdateCategoryRequest createUpdateCategoryRequest = new CreateUpdateCategoryRequest();
            model = getCommAttr(model,user,createUpdateCategoryRequest,null);
            return "Admin/update_create_category";
    }

    @PostMapping("/store")
    public String createCategory(Authentication authentication , @Valid CreateUpdateCategoryRequest createUpdateCategoryRequest,
                            BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model) {
        User user = (User) authentication.getPrincipal();
        model = getCommAttr(model,user,createUpdateCategoryRequest,null);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            redirectAttributes.addFlashAttribute("createUpdateCategoryRequest", createUpdateCategoryRequest);
            return "Admin/update_create_category";
        }
        Boolean verifyLabel = offerCategoryService.checkCategoryExistance(createUpdateCategoryRequest.getLabel());
        if (verifyLabel){
            redirectAttributes.addFlashAttribute("errorMessage","Category exists !");
            return "redirect:/admin/categories/create";
        }
        else {
            offerCategoryService.create(createUpdateCategoryRequest);
            redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("create",new Object[] {}, locale));
            return "redirect:/admin/categories";
        }
    }

    @PostMapping("/{category_id}/update")
    public String updateJob(Authentication authentication,@PathVariable("category_id") Long category_id,@Valid CreateUpdateCategoryRequest createUpdateCategoryRequest,
                            BindingResult bindingResult,RedirectAttributes redirectAttributes,Model model,
                            Locale locale){
        User user = (User) authentication.getPrincipal();
        Optional<OfferCategory> offerCategory = offerCategoryService.findById(category_id);
        if (offerCategory.isPresent()){
            model = getCommAttr(model,user,createUpdateCategoryRequest,category_id);
                if (bindingResult.hasErrors()){
                    redirectAttributes.addFlashAttribute("error", bindingResult);
                    redirectAttributes.addFlashAttribute("createUpdateCategoryRequest", createUpdateCategoryRequest);
                    return "Admin/update_create_category";
                }
                Boolean verifyLabel = offerCategoryService.checkCategoryExistance(createUpdateCategoryRequest.getLabel());
                if (verifyLabel){
                    redirectAttributes.addFlashAttribute("errorMessage","Category exists !");
                    return "redirect:/admin/categories/"+category_id+"/edit";
                }
                else {
                    createUpdateCategoryRequest.setId(category_id);
                    offerCategoryService.update(createUpdateCategoryRequest);
                    redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
                    return "redirect:/admin/categories/"+category_id+"/edit";
                }
        }
        else {
            return "redirect:/admin/categories";
        }
    }

    public Model getCommAttr(Model model,User user,CreateUpdateCategoryRequest createUpdateCategoryRequest,Long category_id){
        model.addAttribute("user",user);
        model.addAttribute("createUpdateCategoryRequest",createUpdateCategoryRequest);
        model.addAttribute("type","dashboard");
        model.addAttribute("category_id",category_id);
        return model;
    }

    @GetMapping("/delete/{category_id}")
    public String deleteCategory(@PathVariable("category_id") Long category_id,
                                RedirectAttributes redirectAttributes,Locale locale,Model model){
        Optional<OfferCategory> offerCategory = offerCategoryService.findById(category_id);
        if (offerCategory.isPresent()){
            List<JobOffer> jobOffers = offerCategory.get().getJobs_offers();
            for (JobOffer jobOffer : jobOffers) {
                jobOfferService.removeJob(jobOffer);
            }
            offerCategoryService.remove(offerCategory.get());
            redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
        }
        return "redirect:/admin/categories";
    }


}
