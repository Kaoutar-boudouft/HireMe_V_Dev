package com.example.hireme.Events;

import com.example.hireme.Model.Entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class EmployerRegistrationSuccessEvent extends ApplicationEvent {
    private User user;
    private String applicationUrl;

    public EmployerRegistrationSuccessEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
