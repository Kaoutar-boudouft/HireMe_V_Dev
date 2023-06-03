package com.example.hireme.Controller.Auth;

import com.example.hireme.Events.RegistrationSuccessEvent;
import com.example.hireme.Exceptions.UserAlreadyExistException;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Requests.CandidateRegisterRequest;
import com.example.hireme.Requests.EmployerRegisterRequest;
import com.example.hireme.Service.AppService;
import com.example.hireme.Service.UserService;
import com.example.hireme.Service.VerificationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class LoginController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenService verificationTokenService;
    private final AppService appService;
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
                                                  final HttpServletRequest httpServletRequest){
        try {
            User user = userService.registerCandidateUser(candidateRegisterRequest);
            return afterRegisterRedirect(user,httpServletRequest,"candidate");
        }
        catch (UserAlreadyExistException e){
            return  "redirect:/login";
        }
    }

    @GetMapping("/registration/employer")
    public String getEmployerRegisterPage(){
        return "Auth/employer_register";
    }

    @PostMapping("/registration/employer")
    public String registerEmployerWithItsProfile(EmployerRegisterRequest employerRegisterRequest,
                                                 final HttpServletRequest httpServletRequest){
        try {
            User user = userService.registerEmployerUser(employerRegisterRequest);
            return afterRegisterRedirect(user,httpServletRequest,"employer");
        }
        catch (UserAlreadyExistException e){
            return  "redirect:/login";
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

    public String afterRegisterRedirect(User user,HttpServletRequest httpServletRequest,String type){
        if (user!=null){
            publisher.publishEvent(new RegistrationSuccessEvent(user,appService.appUrl(httpServletRequest)));
            return "redirect:/login";
        }
        else {
            return "redirect:/registration/"+type;
        }
    }




}
