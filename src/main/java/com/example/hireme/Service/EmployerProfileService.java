package com.example.hireme.Service;

import com.example.hireme.Exceptions.ProfileAlreadyExistException;
import com.example.hireme.Model.Entity.CandidateProfile;
import com.example.hireme.Model.Entity.Company;
import com.example.hireme.Model.Entity.EmployerProfile;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Repository.EmployerProfileRepository;
import com.example.hireme.Requests.EmployerRegisterRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployerProfileService {
    private final EmployerProfileRepository employerProfileRepository;

    public EmployerProfile createNewEmployerProfile(EmployerRegisterRequest employerRegisterRequest, User user, Company company) {
        List<User> result = this.employerProfileRepository.findByUserId(user.getId());
        if (result.size()>0){
            throw new ProfileAlreadyExistException("Profile of user with email "+employerRegisterRequest.getEmail()+" already exist !");
        }
        EmployerProfile employerProfile = new EmployerProfile();
        employerProfile.setFirst_name(employerRegisterRequest.getFirst_name());
        employerProfile.setLast_name(employerRegisterRequest.getLast_name());
        employerProfile.setBirth_date(employerRegisterRequest.getBirth_date());
        employerProfile.setId_number(employerRegisterRequest.getId_number());
        employerProfile.setMobile_number(employerRegisterRequest.getMobile());
        employerProfile.setUser(user);
        employerProfile.setCompany(company);
        return employerProfileRepository.save(employerProfile);
    }

}
