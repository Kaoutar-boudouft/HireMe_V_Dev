package com.example.hireme.Service;

import com.example.hireme.Model.Currency;
import com.example.hireme.Model.Entity.EmployerProfile;
import com.example.hireme.Model.Entity.JobOffer;
import com.example.hireme.Model.JobType;
import com.example.hireme.Repository.*;
import com.example.hireme.Requests.Employer.CreateUpdateJobRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class JobOfferService {
    private final JobOfferRepository jobOfferRepository;
    private final CompanyRepository companyRepository;
    private final CityRepository cityRepository;
    private final OfferCategoryRepository offerCategoryRepository;
    private final EmployerProfileRepository employerProfileRepository;

    public List<JobOffer> getAll(){
        return jobOfferRepository.findAll();
    }
    public List<?> getInsights(){
        return jobOfferRepository.jobsInsights();
    }

    public Long countAll(){return jobOfferRepository.count();}
    public JobOffer create(CreateUpdateJobRequest createUpdateJobRequest, Long company_id){
        JobOffer jobOffer = new JobOffer();
        jobOffer.setActive(false);
        jobOffer.setCompany(companyRepository.getReferenceById(company_id));
        jobOffer.setCity(cityRepository.getReferenceById(createUpdateJobRequest.getCity_id()));
        jobOffer.setCurrency(Currency.valueOf(createUpdateJobRequest.getCurrency()));
        jobOffer.setCategory(offerCategoryRepository.getReferenceById(createUpdateJobRequest.getCategory_id()));
        jobOffer.setDescription(createUpdateJobRequest.getJob_description());
        jobOffer.setSalary(createUpdateJobRequest.getSalary());
        jobOffer.setTitle(createUpdateJobRequest.getTitle());
        jobOffer.setType(JobType.valueOf(createUpdateJobRequest.getType()));
        jobOffer.setPublished_at(LocalDateTime.now());
        return jobOfferRepository.save(jobOffer);
    }

    public List<JobOffer> getJobsByCompany(Long company_id){
        return jobOfferRepository.findByCompanyId(company_id);
    }
    public List<JobOffer> getJobsByCompanyWithPagination(Long company_id,Long start,Long end){
        return jobOfferRepository.findByCompanyIdWithPagination(company_id,start,end);
    }

    public List<JobOffer> getCandidateCandidatures(Long candidate_profile_id){
        return jobOfferRepository.findCandidateCandidatures(candidate_profile_id);
    }
    public List<JobOffer> getCandidateCandidaturesWithPagination(Long candidate_profile_id,Long start,Long end){
        return jobOfferRepository.findCandidateCandidaturesWithPagination(candidate_profile_id,start,end);
    }

    public Boolean checkEmployerJobAuthority(Long job_id,Long user_id){
        EmployerProfile employerProfile = employerProfileRepository.findByUserId(user_id);
        return employerProfile.getCompany().getJobs_offers().contains(jobOfferRepository.getReferenceById(job_id));
    }

    public JobOffer getJobById(Long job_id){
        Optional<JobOffer> jobOfferCheck = jobOfferRepository.findById(job_id);
        return jobOfferCheck.orElse(null);
    }

    public void changeJobState(JobOffer jobOffer){
            jobOffer.setActive(!jobOffer.getActive());
            jobOfferRepository.save(jobOffer);
    }

    public CreateUpdateJobRequest prepareUpdateJobRequest(Long job_id){
        JobOffer jobOffer = jobOfferRepository.getReferenceById(job_id);
        return new CreateUpdateJobRequest(
                job_id,jobOffer.getTitle(),jobOffer.getCategory().getId(),jobOffer.getType().name(),
                jobOffer.getCity().getCountry().getId(),jobOffer.getCity().getId(),jobOffer.getSalary(),
                jobOffer.getCurrency().name(),jobOffer.getDescription(),jobOffer.getActive(),jobOffer.getCompany().getId()
        );
    }

    public JobOffer updateJobOffer(CreateUpdateJobRequest createUpdateJobRequest){
        JobOffer jobOffer = jobOfferRepository.getReferenceById(createUpdateJobRequest.getId());
        jobOffer.setType(JobType.valueOf(createUpdateJobRequest.getType()));
        jobOffer.setSalary(createUpdateJobRequest.getSalary());
        jobOffer.setTitle(createUpdateJobRequest.getTitle());
        jobOffer.setDescription(createUpdateJobRequest.getJob_description());
        jobOffer.setCity(cityRepository.getReferenceById(createUpdateJobRequest.getCity_id()));
        jobOffer.setCategory(offerCategoryRepository.getReferenceById(createUpdateJobRequest.getCategory_id()));
        jobOffer.setCurrency(Currency.valueOf(createUpdateJobRequest.getCurrency()));
        System.out.println("kaoutar"+createUpdateJobRequest.getActive());
        if (createUpdateJobRequest.getActive()!=null){
            System.out.println("kaoutar not null");
            jobOffer.setActive(createUpdateJobRequest.getActive());
        }
        return jobOfferRepository.save(jobOffer);
    }

    public List<JobOffer> getRecentJobs(){
        return jobOfferRepository.findRecentJobs();
    }

    public List<JobOffer> searchJobByTitleAndLocationAndCategoryWithPagination
            (String title,String location,String category,long start,long end){
        if (location.equals("")){
            location = "%%";
        }
        if (category.equals("")){
            category = "%%";
        }
        return jobOfferRepository.findByTitleAndCategoryAndLocationWithPagination(title,location,category,start,end);
    }

    public List<JobOffer> searchJobByTitleAndLocationAndCategory
            (String title,String location,String category){
        if (location.equals("")){
            location = "%%";
        }
        if (category.equals("")){
            category = "%%";
        }
        return jobOfferRepository.findByTitleAndCategoryAndLocation(title,location,category);
    }

    public void removeJob(JobOffer jobOffer){
        jobOfferRepository.deleteCandidaturesByJob(jobOffer.getId());
        jobOfferRepository.delete(jobOffer);
    }



}
