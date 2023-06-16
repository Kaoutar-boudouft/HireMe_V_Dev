package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.CandidateProfile;
import com.example.hireme.Model.Entity.EmployerProfile;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployerProfileRepository extends ProfileRepository<EmployerProfile>{

    EmployerProfile findByUserId(Long user_id);
    EmployerProfile findByCompanyId(Long company_id);


}


