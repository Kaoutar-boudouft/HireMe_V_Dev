package com.example.hireme.Controller.Auth;

import com.example.hireme.Events.RegistrationSuccessEvent;
import com.example.hireme.Exceptions.UserAlreadyExistException;
import com.example.hireme.Model.Entity.Country;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Requests.Candidate.CandidateRegisterRequest;
import com.example.hireme.Requests.Employer.EmployerRegisterRequest;
import com.example.hireme.Service.AppService;
import com.example.hireme.Service.CountryService;
import com.example.hireme.Service.UserService;
import com.example.hireme.Service.VerificationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Controller
@AllArgsConstructor
public class LoginController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenService verificationTokenService;
    private final AppService appService;
    private final LanguageConfig languageConfig;
    private final CountryService countryService;
    @GetMapping("/login")
    public String getLoginPage(Model model){
        model.addAttribute("type","login");
        return "Auth/login";
    }

    @GetMapping("/registration/candidate")
    public String getCandidateRegisterPage(Model model){
        List<Country> countries = countryService.getActiveCountries();
        model.addAttribute("candidateRegisterRequest" ,new CandidateRegisterRequest());
        model.addAttribute("type","register");
        return "Auth/candidate_register";
    }

    @PostMapping("/registration/candidate")
    public String registerCandidateWithItsProfile(@Valid CandidateRegisterRequest candidateRegisterRequest,
                                                  BindingResult bindingResult,
                                                  final HttpServletRequest httpServletRequest,
                                                  RedirectAttributes redirectAttributes,Locale locale,Model model){
        model.addAttribute("type","register");
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            redirectAttributes.addFlashAttribute("candidateRegisterRequest", candidateRegisterRequest);
            return "Auth/candidate_register";
        }
        else {
            try {
                User user = userService.registerCandidateUser(candidateRegisterRequest);
                return afterRegisterRedirect(user,httpServletRequest,"candidate",redirectAttributes,locale,model);
            }
            catch (UserAlreadyExistException e){
                redirectAttributes.addFlashAttribute("warningRegister",languageConfig.messageSource().getMessage("email_exists",new Object[] {}, locale));
                return "redirect:/login";
            }
        }

    }

    @GetMapping("/registration/employer")
    public String getEmployerRegisterPage(Model model){
        List<Country> countries = countryService.getActiveCountries();
        model.addAttribute("type","register");
        model.addAttribute("countries",countries);
        model.addAttribute("employerRegisterRequest" ,new EmployerRegisterRequest());
        return "Auth/employer_register";
    }

    @PostMapping("/registration/employer")
    public String registerEmployerWithItsProfile(@Valid EmployerRegisterRequest employerRegisterRequest,
                                                 BindingResult bindingResult,
                                                 final HttpServletRequest httpServletRequest,
                                                 RedirectAttributes redirectAttributes,Locale locale,Model model){
        model.addAttribute("type","register");
        if (bindingResult.hasErrors()){
            List<Country> countries = countryService.getActiveCountries();
            model.addAttribute("countries",countries);
            redirectAttributes.addFlashAttribute("error", bindingResult);
            redirectAttributes.addFlashAttribute("employerRegisterRequest", employerRegisterRequest);
            return "Auth/employer_register";
        }
        else {
            try {
                User user = userService.registerEmployerUser(employerRegisterRequest);
                return afterRegisterRedirect(user,httpServletRequest,"employer",redirectAttributes,locale,model);
            }
            catch (UserAlreadyExistException e){
                redirectAttributes.addFlashAttribute("warningRegister",languageConfig.messageSource().getMessage("email_exists",new Object[] {}, locale));
                return "redirect:/login";
            }
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

    public String afterRegisterRedirect(User user, HttpServletRequest httpServletRequest, String type, RedirectAttributes redirectAttributes, Locale locale,Model model){
        if (user!=null){
            publisher.publishEvent(new RegistrationSuccessEvent(user,appService.appUrl(httpServletRequest),locale));
            redirectAttributes.addFlashAttribute("successRegister",languageConfig.messageSource().getMessage("verification_link",new Object[] {}, locale));
            return "redirect:/login";
        }
        else {
            List<Country> countries = countryService.getActiveCountries();
            model.addAttribute("countries",countries);
            return "Auth/"+type+"_register";
        }
    }





}
