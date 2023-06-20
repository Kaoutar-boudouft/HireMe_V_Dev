package com.example.hireme.Service;

import com.example.hireme.Exceptions.ProfileAlreadyExistException;
import com.example.hireme.Model.Entity.CandidateProfile;
import com.example.hireme.Model.Entity.Company;
import com.example.hireme.Model.Entity.EmployerProfile;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Repository.EmployerProfileRepository;
import com.example.hireme.Requests.Candidate.UpdateCandidateProfileRequest;
import com.example.hireme.Requests.Employer.EmployerRegisterRequest;
import com.example.hireme.Requests.Employer.UpdateEmployerProfileRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployerProfileService {
    private final EmployerProfileRepository employerProfileRepository;

    public List<EmployerProfile> getAll(){
        return employerProfileRepository.findAll();
    }
    public EmployerProfile createNewEmployerProfile(EmployerRegisterRequest employerRegisterRequest, User user, Company company) {

        EmployerProfile employerProfileCheck = this.employerProfileRepository.findByUserId(user.getId());
        if (employerProfileCheck != null){
            throw new ProfileAlreadyExistException("Profile of user with email "+employerRegisterRequest.getEmail()+" already exist !");
        }
        EmployerProfile employerProfile = new EmployerProfile();
        employerProfile.setId(user.getId());
        employerProfile.setFirst_name(employerRegisterRequest.getFirst_name());
        employerProfile.setLast_name(employerRegisterRequest.getLast_name());
        employerProfile.setBirth_date(employerRegisterRequest.getBirth_date());
        employerProfile.setId_number(employerRegisterRequest.getId_number());
        employerProfile.setMobile_number(employerRegisterRequest.getMobile());
        employerProfile.setUser(user);
        employerProfile.setCompany(company);
        return employerProfileRepository.save(employerProfile);
    }

    public EmployerProfile getEmployerProfile(Long user_id){
        return employerProfileRepository.findByUserId(user_id);
    }

    public UpdateEmployerProfileRequest prepareUpdateEmployerRequest(EmployerProfile employerProfile){
        return new UpdateEmployerProfileRequest(
                employerProfile.getFirst_name(),employerProfile.getLast_name(),employerProfile.getBirth_date(),
                employerProfile.getMobile_number(),employerProfile.getId_number());
    }

    public EmployerProfile updateEmployerProfile(UpdateEmployerProfileRequest updateEmployerProfileRequest,Long user_id){
        EmployerProfile employerProfile = employerProfileRepository.findByUserId(user_id);
        employerProfile.setFirst_name(updateEmployerProfileRequest.getFirst_name());
        employerProfile.setLast_name(updateEmployerProfileRequest.getLast_name());
        employerProfile.setBirth_date(updateEmployerProfileRequest.getBirth_date());
        employerProfile.setMobile_number(updateEmployerProfileRequest.getMobile());
        employerProfile.setId_number(updateEmployerProfileRequest.getId_number());
        return employerProfileRepository.save(employerProfile);
    }

    public EmployerProfile getEmployerProfileByCompanyId(Long company_id){
        return employerProfileRepository.findByCompanyId(company_id);
    }

}
