package com.example.hireme.Controller.Auth;

import com.example.hireme.Events.RegistrationSuccessEvent;
import com.example.hireme.Exceptions.UserAlreadyExistException;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Requests.CandidateRegisterRequest;
import com.example.hireme.Requests.EmployerRegisterRequest;
import com.example.hireme.Service.AppService;
import com.example.hireme.Service.UserService;
import com.example.hireme.Service.VerificationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class LoginController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenService verificationTokenService;
    private final AppService appService;
    private final LanguageConfig languageConfig;
    @GetMapping("/login")
    public String getLoginPage(){
        return "Auth/login";
    }

    @GetMapping("/registration/candidate")
    public String getCandidateRegisterPage(){
        return "Auth/candidate_register";
    }

    @PostMapping("/registration/candidate")
    public String registerCandidateWithItsProfile(CandidateRegisterRequest candidateRegisterRequest,
                                                  final HttpServletRequest httpServletRequest,
                                                  RedirectAttributes redirectAttributes,Locale locale){
        try {
            User user = userService.registerCandidateUser(candidateRegisterRequest);
            return afterRegisterRedirect(user,httpServletRequest,"candidate",redirectAttributes,locale);
        }
        catch (UserAlreadyExistException e){
            redirectAttributes.addFlashAttribute("warningRegister",languageConfig.messageSource().getMessage("email_exists",new Object[] {}, locale));
            return "redirect:/login";
        }
    }

    @GetMapping("/registration/employer")
    public String getEmployerRegisterPage(){
        return "Auth/employer_register";
    }

    @PostMapping("/registration/employer")
    public String registerEmployerWithItsProfile(EmployerRegisterRequest employerRegisterRequest,
                                                 final HttpServletRequest httpServletRequest,
                                                 RedirectAttributes redirectAttributes,Locale locale){
        try {
            User user = userService.registerEmployerUser(employerRegisterRequest);
            return afterRegisterRedirect(user,httpServletRequest,"employer",redirectAttributes,locale);
        }
        catch (UserAlreadyExistException e){
            redirectAttributes.addFlashAttribute("warningRegister",languageConfig.messageSource().getMessage("email_exists",new Object[] {}, locale));
            return "redirect:/login";
        }
    }

    @GetMapping("/registration/verify-token")
    public String verifyEmailByToken(@RequestParam("token") String token){
        String verificationResult = verificationTokenService.verifyToken(token);
        if (verificationResult.equals("")){
            return "redirect:/login";
        }
        else{
            return "redirect:/error";
        }
    }

    public String afterRegisterRedirect(User user, HttpServletRequest httpServletRequest, String type, RedirectAttributes redirectAttributes, Locale locale){
        if (user!=null){
            publisher.publishEvent(new RegistrationSuccessEvent(user,appService.appUrl(httpServletRequest),locale));
            redirectAttributes.addFlashAttribute("successRegister",languageConfig.messageSource().getMessage("verification_link",new Object[] {}, locale));
            return "redirect:/login";
        }
        else {
            return "redirect:/registration/"+type;
        }
    }





}
