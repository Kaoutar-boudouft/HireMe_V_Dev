package com.example.hireme.Service;

import com.example.hireme.Repository.EmployerProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployerProfileService {
    private final EmployerProfileRepository employerProfileRepository;

}
