package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.CandidateProfile;
import com.example.hireme.Model.Entity.JobOffer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer,Long> {
    List<JobOffer> findByCompanyId(Long company_id);

    @Query(value = "select count(jobs.id),companies.name from jobs_offers jobs,companies where jobs.company_id=companies.id group by companies.name", nativeQuery = true)
    List<?> jobsInsights();

    @Query(value = "select * from jobs_offers jobs where jobs.company_id=?1  limit ?2,?3", nativeQuery = true)
    List<JobOffer> findByCompanyIdWithPagination(Long company_id,long start,long end);

    @Query(value = "select jobs.* from jobs_offers jobs,companies c where jobs.company_id=c.id and jobs.active=b'1' and c.active=b'1' order by published_at desc limit 4", nativeQuery = true)
    List<JobOffer> findRecentJobs();

    @Query(value = "select jobs.* from jobs_offers jobs,companies c where jobs.company_id=c.id and jobs.active=b'1' and c.active=b'1' and jobs.title like %?1% and jobs.category_id like ?3 and jobs.city_id in(select id from cities where country_id like ?2)", nativeQuery = true)
    List<JobOffer> findByTitleAndCategoryAndLocation(String title,String location,String category);

    @Query(value = "select jobs.* from jobs_offers jobs,companies c where jobs.company_id=c.id and jobs.active=b'1' and c.active=b'1' and jobs.title like %?1% and jobs.category_id like ?3 and jobs.city_id in(select id from cities where country_id like ?2) limit ?4,?5", nativeQuery = true)
    List<JobOffer> findByTitleAndCategoryAndLocationWithPagination(String title,String location,String category,long start,long end);

    @Query(value = "select * from jobs_offers jobs where jobs.id in(select offer_id from candidatures where candidate_profile_id=?1)", nativeQuery = true)
    List<JobOffer> findCandidateCandidatures(Long candidate_profile_id);

    @Query(value = "select * from jobs_offers jobs where jobs.id in(select offer_id from candidatures where candidate_profile_id=?1) limit ?2,?3", nativeQuery = true)
    List<JobOffer> findCandidateCandidaturesWithPagination(Long candidate_profile_id,long start,long end);


    @Modifying
    @Transactional
    @Query(value = "delete from candidatures where candidatures.offer_id=?1", nativeQuery = true)
    void deleteCandidaturesByJob(Long job_id);




}
