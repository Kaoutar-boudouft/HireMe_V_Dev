package com.example.hireme.Service;

import com.example.hireme.Repository.SupportRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SupportService {
    private final SupportRepository supportRepository;

}
