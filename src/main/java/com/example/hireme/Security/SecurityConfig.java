package com.example.hireme.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        http.csrf().disable().authorizeRequests().antMatchers("/","/login","/css/**","/js/**").permitAll()
//                .and().formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/",true)
//                .passwordParameter("password")
//                .usernameParameter("username")
//                .and().rememberMe().tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)).key("somethingverysecured").rememberMeParameter("remember-me")
//                .and ().logout().logoutUrl("/logout").clearAuthentication (true).invalidateHttpSession(true).deleteCookies ("JSESSIONID", "remember-me")
//                .logoutSuccessUrl("/login");
//        return http.build();

        /*return http.httpBasic(httpBasic -> {} ).authorizeHttpRequests(authorizeHttpRequests ->
                authorizeHttpRequests.requestMatchers("login","/login","/assets/**","/sass/**","/scss/**").permitAll()).formLogin(formLogin -> formLogin.loginPage("/login").permitAll()).build();
   */
        return http
                .authorizeHttpRequests (
                        authorizeConfig -> {authorizeConfig.requestMatchers("/").permitAll();
                        authorizeConfig.requestMatchers("/","/login","/assets/**","/sass/**","/scss/**").permitAll();
                        //authorizeConfig.requestMatchers("/login").permitAll();
                        authorizeConfig.anyRequest().authenticated();
        }).build();
    }
}
