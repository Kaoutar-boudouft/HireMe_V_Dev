package com.example.hireme.Service;

import com.example.hireme.Model.Entity.Company;
import com.example.hireme.Model.Entity.EmployerProfile;
import com.example.hireme.Repository.CityRepository;
import com.example.hireme.Repository.CompanyRepository;
import com.example.hireme.Repository.EmployerProfileRepository;
import com.example.hireme.Requests.Employer.EmployerRegisterRequest;
import com.example.hireme.Requests.Employer.UpdateEmployerCompanyRequest;
import com.example.hireme.Requests.Employer.UpdateEmployerProfileRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CityRepository cityRepository;
    private final EmployerProfileRepository employerProfileRepository;

    public Company createNewCompany(EmployerRegisterRequest employerRegisterRequest) {
        Company company = new Company();
        company.setCreated_at(LocalDateTime.now());
        company.setEmail(employerRegisterRequest.getCompany_email());
        company.setAddress(employerRegisterRequest.getCompany_address());
        company.setActive(false);
        company.setName(employerRegisterRequest.getCompany_name());
        company.setFiscal_id(employerRegisterRequest.getFiscal_id());
        company.setPhone_number(employerRegisterRequest.getCompany_phone_number());
        company.setWebsite(employerRegisterRequest.getCompany_website());
        company.setCity(cityRepository.getReferenceById(employerRegisterRequest.getCompany_city()));
        return companyRepository.save(company);
    }

    public UpdateEmployerCompanyRequest prepareUpdateCompanyRequest(EmployerProfile employerProfile){
            return new UpdateEmployerCompanyRequest(
                employerProfile.getCompany().getName(),employerProfile.getCompany().getFiscal_id(),
                    employerProfile.getCompany().getPhone_number(), employerProfile.getCompany().getEmail(),
                    employerProfile.getCompany().getWebsite(),employerProfile.getCompany().getAddress(),
                    employerProfile.getCompany().getCity().getId(),
                    employerProfile.getCompany().getCity().getCountry().getId(),null);
    }

    public Company updateEmployerCompany(UpdateEmployerCompanyRequest updateEmployerCompanyRequest,Long user_id){
        Company company = companyRepository.findByEmployerProfile_Id(employerProfileRepository.findByUserId(user_id).getId());
        company.setName(updateEmployerCompanyRequest.getCompany_name());
        company.setFiscal_id(updateEmployerCompanyRequest.getFiscal_id());
        company.setAddress(updateEmployerCompanyRequest.getCompany_address());
        company.setEmail(updateEmployerCompanyRequest.getCompany_email());
        company.setPhone_number(updateEmployerCompanyRequest.getCompany_phone_number());
        company.setCity(cityRepository.getReferenceById(updateEmployerCompanyRequest.getCompany_city()));
        return companyRepository.save(company);
    }

    public List<Company> getTopCompaniesByJobOffersCount(){
        return companyRepository.findTopByJobOffersCount();
    }


}
