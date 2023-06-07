package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.City;
import com.example.hireme.Model.Entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City,Long> {
    List<City> findByActiveAndCountryId(int active,Long country_id);

}
