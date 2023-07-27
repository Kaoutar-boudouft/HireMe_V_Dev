package com.example.hireme.Security;

import com.example.hireme.Model.Role;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http/*.csrf(csrf -> csrf.disable())*/
                .formLogin(formLogin -> formLogin.loginPage("/login").successHandler(loginSuccessHandler).permitAll())
                .authorizeHttpRequests (
                        authorizeConfig -> {
                            authorizeConfig.requestMatchers("/static/**","/assets/**","/sass/**","/scss/**","/Fragments/**").permitAll();
                            authorizeConfig.requestMatchers("/registration/**","/login").anonymous();
                            authorizeConfig.requestMatchers("/jobs/**","/blogs/**").permitAll();
                            authorizeConfig.requestMatchers("/","/error","api/**").permitAll();
                            authorizeConfig.requestMatchers("/candidate/**").hasAuthority(Role.CANDIDATE.name());
                            authorizeConfig.requestMatchers("/employer/**").hasAuthority(Role.EMPLOYER.name());
                            authorizeConfig.requestMatchers("/admin/**").hasAuthority(Role.ADMIN.name());
                            authorizeConfig.requestMatchers("/account/**").authenticated();
                            authorizeConfig.requestMatchers("/resources/**").permitAll();
        }).logout(logout -> logout.logoutUrl("/logout").clearAuthentication(true)
                        .deleteCookies("JSESSIONID").logoutSuccessUrl("/login")).build();
    }

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;




}
