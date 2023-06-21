package com.example.hireme.Service;

import com.example.hireme.Exceptions.ProfileAlreadyExistException;
import com.example.hireme.Model.Entity.CandidateProfile;
import com.example.hireme.Model.Entity.JobOffer;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Repository.CandidateProfileRepository;
import com.example.hireme.Repository.CityRepository;
import com.example.hireme.Repository.JobOfferRepository;
import com.example.hireme.Requests.Candidate.CandidateRegisterRequest;
import com.example.hireme.Requests.Candidate.UpdateCandidateProfileRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CandidateProfileService {
    private final CandidateProfileRepository candidateProfileRepository;
    private final CityRepository cityRepository;
    private final JobOfferRepository jobOfferRepository;

    public List<CandidateProfile> getAll(){
        return candidateProfileRepository.findAll();
    }

    public CandidateProfile createNewCandidateProfile(CandidateRegisterRequest candidateRegisterRequest,User user){
        CandidateProfile candidateProfilecheck = this.candidateProfileRepository.findByUserId(user.getId());
        if (candidateProfilecheck != null){
            throw new ProfileAlreadyExistException("Profile of user with email "+candidateRegisterRequest.getEmail()+" already exist !");
        }
        CandidateProfile candidateProfile = new CandidateProfile();
        candidateProfile.setId(user.getId());
        candidateProfile.setFirst_name(candidateRegisterRequest.getFirst_name());
        candidateProfile.setLast_name(candidateRegisterRequest.getLast_name());
        candidateProfile.setBirth_date(candidateRegisterRequest.getBirth_date());
        candidateProfile.setUser(user);
        return candidateProfileRepository.save(candidateProfile);
    }

    public CandidateProfile getCandidateProfile(Long user_id){
        return candidateProfileRepository.findByUserId(user_id);
    }

    public CandidateProfile getCandidateProfileById(Long profile_id){
        return candidateProfileRepository.getReferenceById(profile_id);
    }

    public UpdateCandidateProfileRequest prepareUpdateCandidateRequest(CandidateProfile candidateProfile){
        return new UpdateCandidateProfileRequest(
                candidateProfile.getFirst_name(),candidateProfile.getLast_name(),candidateProfile.getBirth_date(),
                candidateProfile.getMobile_number(),candidateProfile.getId_number(),
                (candidateProfile.getCity()!=null?candidateProfile.getCity().getId():null),
                (candidateProfile.getCity()!=null?candidateProfile.getCity().getCountry().getId():null)
                ,candidateProfile.getMotivation_letter()
                ,null,candidateProfile.getUser().getActive());
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
        if (updateCandidateProfileRequest.getActive()!=null){
            candidateProfile.getUser().setActive(updateCandidateProfileRequest.getActive());
        }
        return candidateProfileRepository.save(candidateProfile);
    }

    public CandidateProfile addCandidature(CandidateProfile candidateProfile, JobOffer jobOffer){
        candidateProfile.getJob_offers().add(jobOffer);
        return candidateProfileRepository.save(candidateProfile);
    }

    public List<CandidateProfile> getCandidaturesByJob(Long offer_id){
        return candidateProfileRepository.findCandidaturesByJob(offer_id);
    }
    public List<CandidateProfile> getCandidaturesByJobWithPagination(Long offer_id,Long start,Long end){
        return candidateProfileRepository.findCandidaturesByJobWithPagination(offer_id,start,end);
    }

}
