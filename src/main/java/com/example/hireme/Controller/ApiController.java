package com.example.hireme.Controller;

import com.example.hireme.Model.Entity.City;
import com.example.hireme.Model.Role;
import com.example.hireme.Service.CityService;
import com.example.hireme.Service.JobOfferService;
import com.example.hireme.Service.UserService;
import com.fasterxml.jackson.core.JsonEncoding;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {
    private final CityService cityService;
    private final UserService userService;
    private final JobOfferService jobOfferService;

    @GetMapping("/active_cities/{country_id}")
    public List<City> getActiveCitiesByCountry(@PathVariable("country_id") Long country_id){
            return cityService.getActiveCitiesByCountry(country_id);
    }

    @GetMapping("/users_insights")
    public List<Integer> getUsersCountByRoles(){
        List<Integer> counter = new ArrayList<Integer>();
        counter.add(userService.getCountByRole(Role.ADMIN));
        counter.add(userService.getCountByRole(Role.EMPLOYER));
        counter.add(userService.getCountByRole(Role.CANDIDATE));
        return counter;
    }
    @GetMapping("/jobs_insights")
    public List<?> getJobsInsights(){
        List<?> insights =  jobOfferService.getInsights();
        return insights;
    }
}
