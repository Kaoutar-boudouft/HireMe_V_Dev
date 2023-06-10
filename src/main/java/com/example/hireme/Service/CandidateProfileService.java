package com.example.hireme.Service;

import com.example.hireme.Exceptions.ProfileAlreadyExistException;
import com.example.hireme.Model.Entity.CandidateProfile;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Repository.CandidateProfileRepository;
import com.example.hireme.Repository.CityRepository;
import com.example.hireme.Requests.Candidate.CandidateRegisterRequest;
import com.example.hireme.Requests.Candidate.UpdateCandidateProfileRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CandidateProfileService {
    private final CandidateProfileRepository candidateProfileRepository;
    private final CityRepository cityRepository;

    public CandidateProfile createNewCandidateProfile(CandidateRegisterRequest candidateRegisterRequest,User user){
        CandidateProfile candidateProfilecheck = this.candidateProfileRepository.findByUserId(user.getId());
        if (candidateProfilecheck != null){
            throw new ProfileAlreadyExistException("Profile of user with email "+candidateRegisterRequest.getEmail()+" already exist !");
        }
        CandidateProfile candidateProfile = new CandidateProfile();
        candidateProfile.setFirst_name(candidateRegisterRequest.getFirst_name());
        candidateProfile.setLast_name(candidateRegisterRequest.getLast_name());
        candidateProfile.setBirth_date(candidateRegisterRequest.getBirth_date());
        candidateProfile.setUser(user);
        return candidateProfileRepository.save(candidateProfile);
    }

    public CandidateProfile getCandidateProfile(Long user_id){
        return candidateProfileRepository.findByUserId(user_id);
    }

    public UpdateCandidateProfileRequest prepareUpdateCandidateRequest(CandidateProfile candidateProfile){
        return new UpdateCandidateProfileRequest(
                candidateProfile.getFirst_name(),candidateProfile.getLast_name(),candidateProfile.getBirth_date(),
                candidateProfile.getMobile_number(),candidateProfile.getId_number(),candidateProfile.getCity().getId(),
                candidateProfile.getCity().getCountry().getId(),candidateProfile.getMotivation_letter()
                ,null);
    }

    public CandidateProfile updateCandidateProfile(UpdateCandidateProfileRequest updateCandidateProfileRequest,Long user_id){
        CandidateProfile candidateProfile = candidateProfileRepository.findByUserId(user_id);
        candidateProfile.setFirst_name(updateCandidateProfileRequest.getFirst_name());
        candidateProfile.setLast_name(updateCandidateProfileRequest.getLast_name());
        candidateProfile.setBirth_date(updateCandidateProfileRequest.getBirth_date());
        candidateProfile.setMobile_number(updateCandidateProfileRequest.getMobile());
        candidateProfile.setId_number(updateCandidateProfileRequest.getId_number());
        candidateProfile.setCity(cityRepository.getReferenceById(updateCandidateProfileRequest.getCity()));
        candidateProfile.setMotivation_letter(updateCandidateProfileRequest.getMotivation_letter());
        return candidateProfileRepository.save(candidateProfile);
    }

}
