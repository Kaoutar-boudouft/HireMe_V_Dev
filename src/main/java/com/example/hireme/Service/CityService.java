package com.example.hireme.Service;

import com.example.hireme.Repository.CityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

}
