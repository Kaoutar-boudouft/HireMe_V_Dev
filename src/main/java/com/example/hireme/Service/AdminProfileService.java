package com.example.hireme.Service;

import com.example.hireme.Model.Entity.AdminProfile;
import com.example.hireme.Model.Entity.CandidateProfile;
import com.example.hireme.Repository.AdminProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminProfileService {
    private final AdminProfileRepository adminProfileRepository;

    public List<AdminProfile> getAll(){
        return adminProfileRepository.findAll();
    }
}
