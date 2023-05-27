package com.example.hireme.Events.Listener;

import com.example.hireme.Model.Entity.User;
import com.example.hireme.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Slf4j
@Component
@RequiredArgsConstructor
public class CandidateRegisterSuccessEventListener implements ApplicationListener<CandidateRegistrationSuccessEvent> {

    private final UserService userService;
    @Override
    public void onApplicationEvent(CandidateRegistrationSuccessEvent event) {
        User candidate = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        userService.saveVerificationToken(candidate,verificationToken);
        String url = event.getApplicationUrl()+"/registration/candidate/verify-token?token="+verificationToken;
        log.info("click to this link to verify your account : {} ",url);
    }
}
