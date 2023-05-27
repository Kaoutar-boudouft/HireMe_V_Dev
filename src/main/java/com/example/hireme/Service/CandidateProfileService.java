package com.example.hireme.Service;

import com.example.hireme.Exceptions.ProfileAlreadyExistException;
import com.example.hireme.Exceptions.UserAlreadyExistException;
import com.example.hireme.Model.Entity.CandidateProfile;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Repository.CandidateProfileRepository;
import com.example.hireme.Requests.CandidateRegisterRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CandidateProfileService {
    private final CandidateProfileRepository candidateProfileRepository;

    public CandidateProfile createNewCandidateProfile(CandidateRegisterRequest candidateRegisterRequest,User user){
        List<User> result = this.candidateProfileRepository.findByUserId(user.getId());
        if (result.size()>0){
            throw new ProfileAlreadyExistException("Profile of user with email "+candidateRegisterRequest.getEmail()+" already exist !");
        }
        CandidateProfile candidateProfile = new CandidateProfile();
        candidateProfile.setFirst_name(candidateRegisterRequest.getFirst_name());
        candidateProfile.setLast_name(candidateRegisterRequest.getLast_name());
        candidateProfile.setBirth_date(candidateRegisterRequest.getBirth_date());
        candidateProfile.setUser(user);
        return candidateProfileRepository.save(candidateProfile);
    }



}
