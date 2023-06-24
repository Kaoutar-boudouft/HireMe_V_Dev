package com.example.hireme.Service;

import com.example.hireme.Model.Entity.City;
import com.example.hireme.Model.Entity.Country;
import com.example.hireme.Repository.CityRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public List<City> getActiveCitiesByCountry(Long country_id){
        return cityRepository.findByActiveAndCountryId(1,country_id);
    }

    public List<City> getCitiesByCountry(Long country_id){
        return cityRepository.findByCountryId(country_id);
    }

    public Optional<City> findById(Long city_id){
        return cityRepository.findById(city_id);
    }

    public City changeCityState(City city){
        int state=1;
        if (city.getActive()==1) state=0;
        city.setActive(state);
        return cityRepository.save(city);
    }

}
