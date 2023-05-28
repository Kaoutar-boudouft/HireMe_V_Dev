package com.example.hireme.Events;

import com.example.hireme.Model.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CandidateRegistrationSuccessEvent extends ApplicationEvent {
    private User user;
    private String applicationUrl;

    public CandidateRegistrationSuccessEvent(User user,String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
