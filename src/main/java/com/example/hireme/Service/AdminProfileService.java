package com.example.hireme.Service;

import com.example.hireme.Repository.AdminProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminProfileService {
    private final AdminProfileRepository adminProfileRepository;

}
