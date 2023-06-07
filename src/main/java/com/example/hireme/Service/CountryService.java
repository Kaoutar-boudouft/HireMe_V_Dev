package com.example.hireme.Service;

import com.example.hireme.Model.Entity.Country;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Repository.CountryRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    public List<Country> getActiveCountries(){
        return countryRepository.findByActive(1);
    }


}
