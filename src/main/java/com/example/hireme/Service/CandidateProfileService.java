package com.example.hireme.Service;

import com.example.hireme.Repository.CandidateProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CandidateProfileService {
    private final CandidateProfileRepository candidateProfileRepository;

}
