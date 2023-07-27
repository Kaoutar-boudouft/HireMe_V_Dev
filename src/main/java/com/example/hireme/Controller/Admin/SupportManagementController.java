package com.example.hireme.Controller.Admin;

import com.example.hireme.Model.Entity.NewsLetter;
import com.example.hireme.Model.Entity.Support;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Requests.Admin.CreateUpdateNewsLetterRequest;
import com.example.hireme.Requests.Admin.CreateUpdateSupportRequest;
import com.example.hireme.Service.JobOfferService;
import com.example.hireme.Service.NewsLetterService;
import com.example.hireme.Service.SupportService;
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
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/supports")
public class SupportManagementController {

    private final SupportService supportService;
    private final LanguageConfig languageConfig;


    @GetMapping()
    public String getSupportsPage(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        List<Support> supports = supportService.getAll();
        model.addAttribute("user",user);
        model.addAttribute("supports",supports);
        model.addAttribute("type","dashboard");
        model.addAttribute("selected", "settings");
        return "Admin/supports";
    }

    @GetMapping("/{contact_id}/edit")
    public String getNewsLettersUpdatePage(@PathVariable("contact_id") Long contact_id, Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        Optional<Support> support = supportService.findById(contact_id);
        if (support.isPresent()){
            CreateUpdateSupportRequest createUpdateSupportRequest = new CreateUpdateSupportRequest(support.get().getEmail());
            model = getCommAttr(model,user,createUpdateSupportRequest,contact_id);
            return "Admin/update_create_support";
        }
        return "redirect:/admin/supports";
    }

   @GetMapping("/create")
    public String getSupportCreatePage(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
            CreateUpdateSupportRequest createUpdateSupportRequest = new CreateUpdateSupportRequest();
            model = getCommAttr(model,user,createUpdateSupportRequest,null);
            return "Admin/update_create_support";
    }

    @PostMapping("/store")
    public String createSupport(Authentication authentication , @Valid CreateUpdateSupportRequest createUpdateSupportRequest,
                            BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model) {
        User user = (User) authentication.getPrincipal();
        model = getCommAttr(model,user,createUpdateSupportRequest,null);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            redirectAttributes.addFlashAttribute("createUpdateSupportRequest", createUpdateSupportRequest);
            return "Admin/update_create_support";
        }
        Boolean verifyEmail = supportService.checkEmailExistance(createUpdateSupportRequest.getEmail());
        if (verifyEmail){
            redirectAttributes.addFlashAttribute("errorMessage","Email exists !");
            return "redirect:/admin/supports/create";
        }
        else {
            supportService.create(createUpdateSupportRequest);
            redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("create",new Object[] {}, locale));
            return "redirect:/admin/supports";
        }
    }

    @PostMapping("/{contact_id}/update")
    public String updateNewsLetter(Authentication authentication,@PathVariable("contact_id") Long contact_id,@Valid CreateUpdateSupportRequest createUpdateSupportRequest,
                            BindingResult bindingResult,RedirectAttributes redirectAttributes,Model model,
                            Locale locale){
        User user = (User) authentication.getPrincipal();
        Optional<Support> support = supportService.findById(contact_id);
        if (support.isPresent()){
            model = getCommAttr(model,user,createUpdateSupportRequest,contact_id);
                if (bindingResult.hasErrors()){
                    redirectAttributes.addFlashAttribute("error", bindingResult);
                    redirectAttributes.addFlashAttribute("createUpdateSupportRequest", createUpdateSupportRequest);
                    return "Admin/update_create_support";
                }
                Boolean verifyEmail = supportService.checkEmailExistance(createUpdateSupportRequest.getEmail());
                if (verifyEmail){
                    redirectAttributes.addFlashAttribute("errorMessage","Email exists !");
                    return "redirect:/admin/supports/"+contact_id+"/edit";
                }
                else {
                    createUpdateSupportRequest.setId(contact_id);
                    supportService.update(createUpdateSupportRequest);
                    redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
                    return "redirect:/admin/supports/"+contact_id+"/edit";
                }
        }
        else {
            return "redirect:/admin/supports";
        }
    }

    public Model getCommAttr(Model model,User user,CreateUpdateSupportRequest createUpdateSupportRequest,Long contact_id){
        model.addAttribute("user",user);
        model.addAttribute("createUpdateSupportRequest",createUpdateSupportRequest);
        model.addAttribute("type","dashboard");
        model.addAttribute("contact_id",contact_id);
        model.addAttribute("selected", "settings");
        return model;
    }

    @GetMapping("/delete/{contact_id}")
    public String deleteNewsLetter(@PathVariable("contact_id") Long contact_id,
                                RedirectAttributes redirectAttributes,Locale locale,Model model){
        Optional<Support> support = supportService.findById(contact_id);
        if (support.isPresent()){
            supportService.remove(support.get());
            redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
        }
        return "redirect:/admin/supports";
    }


}
