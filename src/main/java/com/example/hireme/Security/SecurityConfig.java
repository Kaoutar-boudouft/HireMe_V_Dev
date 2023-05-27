package com.example.hireme.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

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
        return http.csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.loginPage("/login").permitAll().defaultSuccessUrl("/",true))
                .authorizeHttpRequests (
                        authorizeConfig -> {
                            authorizeConfig.requestMatchers(HttpMethod.POST,"/registration/**").permitAll();
                            authorizeConfig.requestMatchers(HttpMethod.GET,"/registration/**").permitAll();
                            authorizeConfig.requestMatchers("/","/login","/registration/**","/assets/**","/sass/**","/scss/**").permitAll();
                        //authorizeConfig.anyRequest().authenticated();
        }).build();
    }
}
