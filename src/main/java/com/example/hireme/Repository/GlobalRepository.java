package com.example.hireme.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GlobalRepository {
    @Modifying
    @Transactional
    @Query(value = "delete from candidatures where candidatures.offer_id=?1", nativeQuery = true)
    void deleteCandidaturesByJob(Long job_id);

    @Modifying
    @Transactional
    @Query(value = "delete from candidatures where candidatures.candidate_profile_id=?1", nativeQuery = true)
    void deleteCandidaturesByCandidate(Long candidate_id);

    @Modifying
    @Transactional
    @Query(value = "delete from jobs_offers where jobs_offers.company_id=?1", nativeQuery = true)
    void deleteJobsByCompany(Long company_id);

    @Modifying
    @Transactional
    @Query(value = "insert into blogs_tags(tag_id,blog_id) VALUES(?2,?1)", nativeQuery = true)
    void linkBlogWithTag(Long blog_id,Long tag_id);

    @Modifying
    @Transactional
    @Query(value = "delete from blogs_tags where blog_id=?1", nativeQuery = true)
    void removeLinkbetweenBlogAndTag(Long blog_id);
}
