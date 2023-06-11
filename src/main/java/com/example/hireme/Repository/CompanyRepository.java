package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.Company;
import com.example.hireme.Model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {

    Company findByEmployerProfile_Id(Long employer_profile_id);


}
