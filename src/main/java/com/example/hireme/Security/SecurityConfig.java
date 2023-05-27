package com.example.hireme.Security;

import lombok.AllArgsConstructor;
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
                .formLogin(formLogin -> formLogin.loginPage("/login").permitAll().defaultSuccessUrl("/",true))
                .authorizeHttpRequests (
                        authorizeConfig -> {
                            authorizeConfig.requestMatchers("/static/**","/assets/**","/sass/**","/scss/**").permitAll();
                            authorizeConfig.requestMatchers("/registration/**").permitAll();
                            authorizeConfig.requestMatchers("/","/login").permitAll();

                            //authorizeConfig.anyRequest().authenticated();
        }).logout(logout -> logout.logoutUrl("/logout").clearAuthentication(true)
                        .deleteCookies("JSESSIONID").logoutSuccessUrl("/")).build();
    }




}
