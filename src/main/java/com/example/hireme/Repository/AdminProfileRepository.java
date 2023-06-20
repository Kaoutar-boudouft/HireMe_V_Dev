package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.AdminProfile;
import com.example.hireme.Model.Entity.CandidateProfile;
import com.example.hireme.Model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminProfileRepository extends ProfileRepository<AdminProfile> {
}
