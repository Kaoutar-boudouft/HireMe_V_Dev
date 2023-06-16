package com.example.hireme.Controller;

import com.example.hireme.Events.RegistrationSuccessEvent;
import com.example.hireme.Exceptions.UserAlreadyExistException;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Requests.EmailUpdateRequest;
import com.example.hireme.Requests.PasswordUpdateRequest;
import com.example.hireme.Service.AppService;
import com.example.hireme.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Locale;

@Controller
@AllArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final AppService appService;
    private final LanguageConfig languageConfig;


    @GetMapping()
    public String getAccountPage(Authentication authentication,Model model){
        User user = (User) authentication.getPrincipal();
        EmailUpdateRequest emailUpdateRequest = new EmailUpdateRequest(user.getEmail());
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest();
        model = getCommunAttr(model,user);
        model.addAttribute("emailUpdateRequest",emailUpdateRequest);
        model.addAttribute("passwordUpdateRequest",passwordUpdateRequest);
        return "account";
    }

    @PostMapping("/update/email")
    public String emailUpdate(Authentication authentication, @Valid EmailUpdateRequest emailUpdateRequest,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model,
                              final HttpServletRequest httpServletRequest){
        User user = (User) authentication.getPrincipal();
        model = getCommunAttr(model,user);
        if (bindingResult.hasErrors()){
            PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest();
            model.addAttribute("passwordUpdateRequest",passwordUpdateRequest);
            redirectAttributes.addFlashAttribute("error", bindingResult);
            redirectAttributes.addFlashAttribute("emailUpdateRequest", emailUpdateRequest);
            return "account";
        }
        try {
            User u = userService.updateUserEmail(user,emailUpdateRequest);
           if (u!=null){
               publisher.publishEvent(new RegistrationSuccessEvent(u,appService.appUrl(httpServletRequest),locale));
               redirectAttributes.addFlashAttribute("warningRegister","Email updated successfully ! please verify it");
               return "redirect:/logout";
           }
            redirectAttributes.addFlashAttribute("error","update error");
            return "redirect:/account";
        }
        catch (UserAlreadyExistException e){
            redirectAttributes.addFlashAttribute("warning",languageConfig.messageSource().getMessage("email_exists",new Object[] {}, locale));
            return "redirect:/account";
        }
    }

    @PostMapping("/update/password")
    public String passwordUpdate(Authentication authentication, @Valid PasswordUpdateRequest passwordUpdateRequest,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model,
                              final HttpServletRequest httpServletRequest){
        User user = (User) authentication.getPrincipal();
        model = getCommunAttr(model,user);
        if (bindingResult.hasErrors()){
            EmailUpdateRequest emailUpdateRequest = new EmailUpdateRequest(user.getEmail());
            model.addAttribute("emailUpdateRequest",emailUpdateRequest);
            redirectAttributes.addFlashAttribute("error", bindingResult);
            redirectAttributes.addFlashAttribute("passwordUpdateRequest", passwordUpdateRequest);
            return "account";
        }

        user = userService.updateUserPassword(user,passwordUpdateRequest);
        redirectAttributes.addFlashAttribute("success",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
        return "redirect:/account";
    }

    public Model getCommunAttr(Model model, User user){
        model.addAttribute("user",user);
        model.addAttribute("type", "profile");
        return model;
    }


}
