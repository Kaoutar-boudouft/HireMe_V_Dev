package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.CandidateProfile;
import com.example.hireme.Model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile,Long> {

    CandidateProfile findByUserId(Long user_id);




}
