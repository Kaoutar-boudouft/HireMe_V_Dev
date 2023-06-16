package com.example.hireme;

import com.example.hireme.Repository.AdminProfileRepository;
import com.example.hireme.Repository.CandidateProfileRepository;
import com.example.hireme.Repository.EmployerProfileRepository;
import com.example.hireme.Repository.ProfileRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HireMeApplication {

    public static void main(String[] args) {
        /*ConfigurableApplicationContext configurableApplicationContext =*/ SpringApplication.run(HireMeApplication.class, args);
//        ProfileRepository profileRepository = configurableApplicationContext.getBean(ProfileRepository.class);
//        CandidateProfileRepository candidateProfileRepository = configurableApplicationContext.getBean(CandidateProfileRepository.class);
//        AdminProfileRepository adminProfileRepository = configurableApplicationContext.getBean(AdminProfileRepository.class);
//        EmployerProfileRepository employerProfileRepository = configurableApplicationContext.getBean(EmployerProfileRepository.class);
    }

}
