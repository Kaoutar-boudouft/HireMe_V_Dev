package com.example.hireme.Service;

import com.example.hireme.Repository.CountryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

}
