package com.example.hireme.Controller.Admin;

import com.example.hireme.Model.Entity.JobOffer;
import com.example.hireme.Model.Entity.NewsLetter;
import com.example.hireme.Model.Entity.OfferCategory;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Requests.Admin.CreateUpdateCategoryRequest;
import com.example.hireme.Requests.Admin.CreateUpdateNewsLetterRequest;
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

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/news_letters")
public class News_LetterManagementController {

    private final JobOfferService jobOfferService;
    private final NewsLetterService newsLetterService;
    private final LanguageConfig languageConfig;


    @GetMapping()
    public String getNewsLettersPage(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        List<NewsLetter> newsLetters = newsLetterService.getAll();
        model.addAttribute("user",user);
        model.addAttribute("newsLetters",newsLetters);
        model.addAttribute("type","dashboard");
        model.addAttribute("selected", "news-letters");
        return "Admin/news_letters";
    }

    @GetMapping("/{contact_id}/edit")
    public String getNewsLettersUpdatePage(@PathVariable("contact_id") Long contact_id, Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        Optional<NewsLetter> newsLetter = newsLetterService.findById(contact_id);
        if (newsLetter.isPresent()){
            CreateUpdateNewsLetterRequest createUpdateNewsLetterRequest = new CreateUpdateNewsLetterRequest(newsLetter.get().getEmail());
            model = getCommAttr(model,user,createUpdateNewsLetterRequest,contact_id);
            return "Admin/update_create_news_letter";
        }
        return "redirect:/admin/news_letters";
    }

   @GetMapping("/create")
    public String getNewsLettersCreatePage(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
            CreateUpdateNewsLetterRequest createUpdateNewsLetterRequest = new CreateUpdateNewsLetterRequest();
            model = getCommAttr(model,user,createUpdateNewsLetterRequest,null);
            return "Admin/update_create_news_letter";
    }

    @PostMapping("/store")
    public String createNewsLetter(Authentication authentication , @Valid CreateUpdateNewsLetterRequest createUpdateNewsLetterRequest,
                            BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model) {
        User user = (User) authentication.getPrincipal();
        model = getCommAttr(model,user,createUpdateNewsLetterRequest,null);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            redirectAttributes.addFlashAttribute("createUpdateNewsLetterRequest", createUpdateNewsLetterRequest);
            return "Admin/update_create_news_letter";
        }
        Boolean verifyEmail = newsLetterService.checkEmailExistance(createUpdateNewsLetterRequest.getEmail());
        if (verifyEmail){
            redirectAttributes.addFlashAttribute("errorMessage","Email exists !");
            return "redirect:/admin/news_letters/create";
        }
        else {
            newsLetterService.create(createUpdateNewsLetterRequest);
            redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("create",new Object[] {}, locale));
            return "redirect:/admin/news_letters";
        }
    }

    @PostMapping("/{contact_id}/update")
    public String updateNewsLetter(Authentication authentication,@PathVariable("contact_id") Long contact_id,@Valid CreateUpdateNewsLetterRequest createUpdateNewsLetterRequest,
                            BindingResult bindingResult,RedirectAttributes redirectAttributes,Model model,
                            Locale locale){
        User user = (User) authentication.getPrincipal();
        Optional<NewsLetter> newsLetter = newsLetterService.findById(contact_id);
        if (newsLetter.isPresent()){
            model = getCommAttr(model,user,createUpdateNewsLetterRequest,contact_id);
                if (bindingResult.hasErrors()){
                    redirectAttributes.addFlashAttribute("error", bindingResult);
                    redirectAttributes.addFlashAttribute("createUpdateNewsLetterRequest", createUpdateNewsLetterRequest);
                    return "Admin/update_create_news_letter";
                }
                Boolean verifyEmail = newsLetterService.checkEmailExistance(createUpdateNewsLetterRequest.getEmail());
                if (verifyEmail){
                    redirectAttributes.addFlashAttribute("errorMessage","Email exists !");
                    return "redirect:/admin/news_letters/"+contact_id+"/edit";
                }
                else {
                    createUpdateNewsLetterRequest.setId(contact_id);
                    newsLetterService.update(createUpdateNewsLetterRequest);
                    redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
                    return "redirect:/admin/news_letters/"+contact_id+"/edit";
                }
        }
        else {
            return "redirect:/admin/news_letters";
        }
    }

    public Model getCommAttr(Model model,User user,CreateUpdateNewsLetterRequest createUpdateNewsLetterRequest,Long contact_id){
        model.addAttribute("user",user);
        model.addAttribute("createUpdateNewsLetterRequest",createUpdateNewsLetterRequest);
        model.addAttribute("type","dashboard");
        model.addAttribute("contact_id",contact_id);
        model.addAttribute("selected", "news-letters");
        return model;
    }

    @GetMapping("/delete/{contact_id}")
    public String deleteNewsLetter(@PathVariable("contact_id") Long contact_id,
                                RedirectAttributes redirectAttributes,Locale locale,Model model){
        Optional<NewsLetter> newsLetter = newsLetterService.findById(contact_id);
        if (newsLetter.isPresent()){
            newsLetterService.remove(newsLetter.get());
            redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
        }
        return "redirect:/admin/news_letters";
    }


}
