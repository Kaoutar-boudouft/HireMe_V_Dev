package com.example.hireme.Service;

import com.example.hireme.Model.Entity.City;
import com.example.hireme.Repository.CityRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public List<City> getActiveCitiesByCountry(Long country_id){
        return cityRepository.findByActiveAndCountryId(1,country_id);
    }

}
