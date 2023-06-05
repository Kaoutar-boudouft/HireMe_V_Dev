package com.example.hireme.Events;

import com.example.hireme.Model.Entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter
public class RegistrationSuccessEvent extends ApplicationEvent {
    private User user;
    private String applicationUrl;

    private Locale locale;

    public RegistrationSuccessEvent(User user, String applicationUrl, Locale locale) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
        this.locale = locale;
    }
}
