package com.example.hireme.Service;

import com.example.hireme.Model.Entity.Country;
import com.example.hireme.Repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    public List<Country> getActiveCountries(){
        return countryRepository.findByActive(1);
    }
    public List<Country> getAll(){
        return countryRepository.findAll();
    }

    public Optional<Country> findById(Long country_id){
        return countryRepository.findById(country_id);
    }

    public Country changeCountryState(Country country){
        int state=1;
        if (country.getActive()==1) state=0;
        country.setActive(state);
        return countryRepository.save(country);
    }


}
