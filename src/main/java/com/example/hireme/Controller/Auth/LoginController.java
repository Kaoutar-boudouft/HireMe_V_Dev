package com.example.hireme.Controller.Auth;

import com.example.hireme.Events.Listener.CandidateRegistrationSuccessEvent;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Requests.CandidateRegisterRequest;
import com.example.hireme.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@AllArgsConstructor
public class LoginController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
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

    private String appUrl(HttpServletRequest httpServletRequest) {
        return "http://"+httpServletRequest.getServerName()+":"+httpServletRequest
                .getServerPort()+httpServletRequest.getContextPath();
    }
}
