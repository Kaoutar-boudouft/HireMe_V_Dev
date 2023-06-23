package com.example.hireme.Service;

import com.example.hireme.Model.Entity.NewsLetter;
import com.example.hireme.Model.Entity.Support;
import com.example.hireme.Repository.SupportRepository;
import com.example.hireme.Requests.Admin.CreateUpdateNewsLetterRequest;
import com.example.hireme.Requests.Admin.CreateUpdateSupportRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SupportService {
    private final SupportRepository supportRepository;

    public List<Support> getAll(){
        return supportRepository.findAll();
    }

    public Optional<Support> findById(Long support_id){
        return supportRepository.findById(support_id);
    }

    public Support create(CreateUpdateSupportRequest createUpdateSupportRequest){
        Support support = new Support();
        support.setEmail(createUpdateSupportRequest.getEmail());
        return supportRepository.save(support);
    }
    public Support update(CreateUpdateSupportRequest createUpdateSupportRequest){
        Support support = supportRepository.getReferenceById(createUpdateSupportRequest.getId());
        support.setEmail(createUpdateSupportRequest.getEmail());
        return supportRepository.save(support);
    }

    public Boolean checkEmailExistance(String email){
        Optional<Support> support = supportRepository.findByEmail(email);
        return support.isPresent();
    }

    public void remove(Support support){
        supportRepository.delete(support);
    }
}
