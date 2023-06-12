package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.CandidateProfile;
import com.example.hireme.Model.Entity.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer,Long> {
    List<JobOffer> findByCompanyId(Long company_id);

    @Query(value = "select * from jobs_offers jobs where jobs.company_id=?1 limit ?2,?3", nativeQuery = true)
    List<JobOffer> findByCompanyIdWithPagination(Long company_id,long start,long end);

}
