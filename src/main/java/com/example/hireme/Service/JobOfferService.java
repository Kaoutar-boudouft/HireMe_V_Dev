package com.example.hireme.Service;

import com.example.hireme.Model.Currency;
import com.example.hireme.Model.Entity.JobOffer;
import com.example.hireme.Model.JobType;
import com.example.hireme.Repository.CityRepository;
import com.example.hireme.Repository.CompanyRepository;
import com.example.hireme.Repository.JobOfferRepository;
import com.example.hireme.Repository.OfferCategoryRepository;
import com.example.hireme.Requests.Employer.CreateJobRequest;
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

    public JobOffer create(CreateJobRequest createJobRequest,Long company_id){
        JobOffer jobOffer = new JobOffer();
        jobOffer.setActive(false);
        jobOffer.setCompany(companyRepository.getReferenceById(company_id));
        jobOffer.setCity(cityRepository.getReferenceById(createJobRequest.getCity_id()));
        jobOffer.setCurrency(Currency.valueOf(createJobRequest.getCurrency()));
        jobOffer.setCategory(offerCategoryRepository.getReferenceById(createJobRequest.getCategory_id()));
        jobOffer.setDescription(createJobRequest.getJob_description());
        jobOffer.setSalary(createJobRequest.getSalary());
        jobOffer.setTitle(createJobRequest.getTitle());
        jobOffer.setType(JobType.valueOf(createJobRequest.getType()));
        jobOffer.setPublished_at(LocalDateTime.now());
        return jobOfferRepository.save(jobOffer);
    }

    public List<JobOffer> getJobsByCompany(Long company_id){
        return jobOfferRepository.findByCompanyId(company_id);
    }
    public List<JobOffer> getJobsByCompanyWithPagination(Long company_id,Long start,Long end){
        return jobOfferRepository.findByCompanyIdWithPagination(company_id,start,end);
    }

    public Boolean checkJobExistance(Long job_id){
        Optional<JobOffer> jobOffer = jobOfferRepository.findById(job_id);
        if (jobOffer.isPresent()) return true;
        else return false;
    }

    public JobOffer getJobById(Long job_id){
        Boolean jobOfferCheck = checkJobExistance(job_id);
        if (jobOfferCheck){
            return jobOfferRepository.getReferenceById(job_id);
        }
        else return null;
    }

    public void changeJobState(JobOffer jobOffer){
            jobOffer.setActive(!jobOffer.getActive());
            jobOfferRepository.save(jobOffer);
    }

}
