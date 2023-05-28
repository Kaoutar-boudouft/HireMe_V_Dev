package com.example.hireme.Controller.Auth;

import com.example.hireme.Events.CandidateRegistrationSuccessEvent;
import com.example.hireme.Events.EmployerRegistrationSuccessEvent;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Requests.CandidateRegisterRequest;
import com.example.hireme.Requests.EmployerRegisterRequest;
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
        User user = userService.registerCandidateUser(candidateRegisterRequest);
        if (user!=null){
            publisher.publishEvent(new CandidateRegistrationSuccessEvent(user,appUrl(httpServletRequest)));
            return "redirect:/login";
        }
        else {
            return "redirect:/registration/candidate";
        }
    }

    @GetMapping("/registration/employer")
    public String getEmployerRegisterPage(){
        return "Auth/employer_register";
    }

    @PostMapping("/registration/employer")
    public String registerEmployerWithItsProfile(EmployerRegisterRequest employerRegisterRequest,
                                                 final HttpServletRequest httpServletRequest){
        User user = userService.registerEmployerUser(employerRegisterRequest);
        if (user!=null){
            publisher.publishEvent(new EmployerRegistrationSuccessEvent(user,appUrl(httpServletRequest)));
            return "redirect:/login";
        }
        else {
            return "redirect:/registration/employer";
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

    private String appUrl(HttpServletRequest httpServletRequest) {
        return "http://"+httpServletRequest.getServerName()+":"+httpServletRequest
                .getServerPort()+httpServletRequest.getContextPath();
    }
}
