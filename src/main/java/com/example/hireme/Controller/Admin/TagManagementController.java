package com.example.hireme.Controller.Admin;

import com.example.hireme.Model.Entity.*;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Repository.BlogTagRepository;
import com.example.hireme.Requests.Admin.CreateUpdateCategoryRequest;
import com.example.hireme.Requests.Admin.CreateUpdateTagRequest;
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
@RequestMapping("/admin/tags")
public class TagManagementController {

    private final JobOfferService jobOfferService;
    private final BlogTagService blogTagService;
    private final CityService cityService;
    private final CompanyService companyService;
    private final OfferCategoryService offerCategoryService;
    private final LanguageConfig languageConfig;
    private final BlogTagRepository blogTagRepository;
    private final VerificationTokenService verificationTokenService;
    private final EmployerProfileService employerProfileService;

    @GetMapping()
    public String getTagsPage(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        List<BlogTag> tags = blogTagService.getAll();
        model.addAttribute("user",user);
        model.addAttribute("tags",tags);
        model.addAttribute("type","dashboard");
        return "Admin/tags";
    }

    @GetMapping("/{tag_id}/edit")
    public String getTagsUpdatePage(@PathVariable("tag_id") Long tag_id, Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        Optional<BlogTag> blogTag = blogTagService.findById(tag_id);
        if (blogTag.isPresent()){
            CreateUpdateTagRequest createUpdateTagRequest = new CreateUpdateTagRequest(blogTag.get().getLabel());
            model.addAttribute("user",user);
            model.addAttribute("createUpdateTagRequest",createUpdateTagRequest);
            model.addAttribute("type","dashboard");
            return "Admin/update_create_tag";
        }
        return "redirect:/admin/tags";
    }

   @GetMapping("/create")
    public String getTagCreatePage(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
            CreateUpdateTagRequest createUpdateTagRequest = new CreateUpdateTagRequest();
            model.addAttribute("user",user);
            model.addAttribute("createUpdateTagRequest",createUpdateTagRequest);
            model.addAttribute("type","dashboard");
            model.addAttribute("tag_id",null);
            return "Admin/update_create_tag";
    }

    @PostMapping("/store")
    public String createTag(Authentication authentication , @Valid CreateUpdateTagRequest createUpdateTagRequest,
                            BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user",user);
        model.addAttribute("type","dashboard");
        model.addAttribute("tag_id",null);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            redirectAttributes.addFlashAttribute("createUpdateTagRequest", createUpdateTagRequest);
            return "Admin/update_create_tag";
        }
        Boolean verifyLabel = blogTagService.checkTagExistance(createUpdateTagRequest.getLabel());
        if (verifyLabel){
            redirectAttributes.addFlashAttribute("errorMessage","Tag exists !");
            return "redirect:/admin/tags/create";
        }
        else {
            blogTagService.create(createUpdateTagRequest);
            redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("create",new Object[] {}, locale));
            return "redirect:/admin/tags";
        }
    }

    @PostMapping("/{tag_id}/update")
    public String updateJob(Authentication authentication,@PathVariable("tag_id") Long tag_id,@Valid CreateUpdateTagRequest createUpdateTagRequest,
                            BindingResult bindingResult,RedirectAttributes redirectAttributes,Model model,
                            Locale locale){
        User user = (User) authentication.getPrincipal();
        Optional<BlogTag> blogTag = blogTagService.findById(tag_id);
        if (blogTag.isPresent()){
            model.addAttribute("user",user);
            model.addAttribute("createUpdateTagRequest",createUpdateTagRequest);
            model.addAttribute("type","dashboard");
                if (bindingResult.hasErrors()){
                    redirectAttributes.addFlashAttribute("error", bindingResult);
                    redirectAttributes.addFlashAttribute("createUpdateTagRequest", createUpdateTagRequest);
                    return "Admin/update_create_tag";
                }
                Boolean verifyLabel = blogTagService.checkTagExistance(createUpdateTagRequest.getLabel());
                if (verifyLabel){
                    redirectAttributes.addFlashAttribute("errorMessage","Tag exists !");
                    return "redirect:/admin/tags/"+tag_id+"/edit";
                }
                else {
                    createUpdateTagRequest.setId(tag_id);
                    blogTagService.update(createUpdateTagRequest);
                    redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
                    return "redirect:/admin/tags/"+tag_id+"/edit";
                }
        }
        else {
            return "redirect:/admin/tags";
        }
    }

    @GetMapping("/delete/{tag_id}")
    public String deleteCompany(@PathVariable("tag_id") Long tag_id,
                                RedirectAttributes redirectAttributes,Locale locale,Model model){
        Optional<BlogTag> blogTag = blogTagService.findById(tag_id);
        if (blogTag.isPresent()){
            BlogTag Tag = blogTagService.removeBlogsLLinks(blogTag.get());
            blogTagService.remove(Tag);
            redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
        }
        return "redirect:/admin/tags";
    }


}
