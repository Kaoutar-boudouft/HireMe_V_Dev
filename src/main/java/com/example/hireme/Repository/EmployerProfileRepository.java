package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.CandidateProfile;
import com.example.hireme.Model.Entity.EmployerProfile;
import com.example.hireme.Model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployerProfileRepository extends JpaRepository<EmployerProfile,Long> {
    /*@Query(value = "select * from employers_profiles ep where ep.user_id=?1", nativeQuery = true)
    public List<User> findByUserId(Long id);*/

    EmployerProfile findByUserId(Long user_id);


}


