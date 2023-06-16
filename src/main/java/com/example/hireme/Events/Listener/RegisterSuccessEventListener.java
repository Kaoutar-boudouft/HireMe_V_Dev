package com.example.hireme.Events.Listener;

import com.example.hireme.Email.EmailService;
import com.example.hireme.Events.RegistrationSuccessEvent;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;
@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterSuccessEventListener implements ApplicationListener<RegistrationSuccessEvent> {

    private final LanguageConfig languageConfig;
    private final UserService userService;
    private final EmailService emailService;
    @Override
    public void onApplicationEvent(RegistrationSuccessEvent event) {
        User user = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        userService.saveVerificationToken(user,verificationToken);
        String url = event.getApplicationUrl()+"/registration/verify-token?token="+verificationToken;
        emailService.send(user.getEmail(),"<h1 align='left' >HIREME</h1><br><h3 align='center'>"+languageConfig.messageSource().getMessage("account_verification_needed",new Object[] {}, event.getLocale())+"</h1>" +
                "<br><h4 align='center'><a href='"+url+"'>"+languageConfig.messageSource().getMessage("Account_Verification",new Object[] {}, event.getLocale())+"</a></h4>",
                languageConfig.messageSource().getMessage("Account_Verification",new Object[] {},event.getLocale()),"HireMe");
    }
}
