package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.Company;
import com.example.hireme.Model.Entity.JobOffer;
import com.example.hireme.Model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {

    Company findByEmployerProfile_Id(Long employer_profile_id);

    @Query(value = "select id,name,active,address,email,fiscal_id,phone_number,priority,website,city_id,created_at from (select c.*,count(j.id) as total from companies c,jobs_offers j where j.company_id=c.id GROUP BY c.id order by total limit 4) t", nativeQuery = true)
    List<Company> findTopByJobOffersCount();


}
