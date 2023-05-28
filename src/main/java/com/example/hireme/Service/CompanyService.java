package com.example.hireme.Service;

import com.example.hireme.Exceptions.CompanyAlreadyExistException;
import com.example.hireme.Exceptions.ProfileAlreadyExistException;
import com.example.hireme.Model.Entity.City;
import com.example.hireme.Model.Entity.Company;
import com.example.hireme.Model.Entity.EmployerProfile;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Repository.CityRepository;
import com.example.hireme.Repository.CompanyRepository;
import com.example.hireme.Requests.EmployerRegisterRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CityRepository cityRepository;

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
        return companyRepository.save(company);
    }

}
