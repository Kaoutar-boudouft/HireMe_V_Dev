package com.example.hireme.Security;

import com.example.hireme.Model.Role;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.loginPage("/login").successHandler(loginSuccessHandler).permitAll())
                .authorizeHttpRequests (
                        authorizeConfig -> {
                            authorizeConfig.requestMatchers("/static/**","/assets/**","/sass/**","/scss/**","/Fragments/**").permitAll();
                            authorizeConfig.requestMatchers("/registration/**").permitAll();
                            authorizeConfig.requestMatchers("/","/login","/error","api/**").permitAll();
                            authorizeConfig.requestMatchers("/candidate/**").hasAuthority(Role.CANDIDATE.name());
                            authorizeConfig.requestMatchers("/employer/**").hasAuthority(Role.EMPLOYER.name());
                            authorizeConfig.requestMatchers("/admin/**").hasAuthority(Role.ADMIN.name());
        }).logout(logout -> logout.logoutUrl("/logout").clearAuthentication(true)
                        .deleteCookies("JSESSIONID").logoutSuccessUrl("/login")).build();
    }

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;




}
