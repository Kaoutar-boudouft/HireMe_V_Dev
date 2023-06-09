package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.CandidateProfile;
import com.example.hireme.Model.Entity.JobOffer;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Model.Profile;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateProfileRepository extends ProfileRepository<CandidateProfile> {

    CandidateProfile findByUserId(Long user_id);

    @Query(value = "select * from candidates_profiles c where c.user_id=?1", nativeQuery = true)
    CandidateProfile findCandidateProfileByUserId(Long user_id);

    @Query(value = "select * from candidates_profiles cp where cp.id in(select candidate_profile_id from candidatures where offer_id=?1)", nativeQuery = true)
    List<CandidateProfile> findCandidaturesByJob(Long offer_id);

    @Query(value = "select * from candidates_profiles cp where cp.id in(select candidate_profile_id from candidatures where offer_id=?1) limit ?2,?3", nativeQuery = true)
    List<CandidateProfile> findCandidaturesByJobWithPagination(Long offer_id,long start,long end);

    @Modifying
    @Transactional
    @Query(value = "delete from candidatures where candidatures.candidate_profile_id=?1", nativeQuery = true)
    void deleteCandidaturesByCandidate(Long candidate_id);



}
