package com.example.hireme.Security;

import com.example.hireme.Model.Entity.User;
import com.example.hireme.Model.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        User user = (User) authentication.getPrincipal();
        String redirectUrl = request.getContextPath();
        if (user.getRole().equals(Role.CANDIDATE)){
            redirectUrl += "/candidate/profile";
        } else if (user.getRole().equals(Role.EMPLOYER)) {
            redirectUrl += "/employer/profile";
        }
        else {
            redirectUrl += "/admin/dashboard";
        }
        response.sendRedirect(redirectUrl);
    }
}
