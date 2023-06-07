package com.example.hireme.Controller;

import com.example.hireme.Model.Entity.City;
import com.example.hireme.Service.CityService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {
    private final CityService cityService;

    @GetMapping("/active_cities/{country_id}")
    public List<City> getActiveCitiesByCountry(@PathVariable("country_id") Long country_id){
        return cityService.getActiveCitiesByCountry(country_id);
    }
}
