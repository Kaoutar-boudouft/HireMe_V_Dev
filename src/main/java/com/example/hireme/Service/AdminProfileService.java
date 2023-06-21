package com.example.hireme.Service;

import com.example.hireme.Model.Entity.AdminProfile;
import com.example.hireme.Model.Entity.CandidateProfile;
import com.example.hireme.Model.Entity.EmployerProfile;
import com.example.hireme.Repository.AdminProfileRepository;
import com.example.hireme.Repository.CityRepository;
import com.example.hireme.Requests.Admin.UpdateAdminProfileRequest;
import com.example.hireme.Requests.Employer.UpdateEmployerProfileRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminProfileService {
    private final AdminProfileRepository adminProfileRepository;
    private final CityRepository cityRepository;

    public List<AdminProfile> getAll(){
        return adminProfileRepository.findAll();
    }

    public AdminProfile getAdminProfile(Long user_id){
        return adminProfileRepository.findByUserId(user_id);
    }

    public UpdateAdminProfileRequest prepareUpdateAdminRequest(AdminProfile adminProfile){
        return new UpdateAdminProfileRequest(
                adminProfile.getFirst_name(),adminProfile.getLast_name(),adminProfile.getBirth_date(),
                adminProfile.getMobile_number(),adminProfile.getId_number(),adminProfile.getCity().getId(),
                adminProfile.getCity().getCountry().getId(),adminProfile.getUser().getActive());
    }

    public AdminProfile updateAdminProfile(UpdateAdminProfileRequest updateAdminProfileRequest,Long user_id){
        AdminProfile adminProfile = adminProfileRepository.findByUserId(user_id);
        adminProfile.setFirst_name(updateAdminProfileRequest.getFirst_name());
        adminProfile.setLast_name(updateAdminProfileRequest.getLast_name());
        adminProfile.setBirth_date(updateAdminProfileRequest.getBirth_date());
        adminProfile.setMobile_number(updateAdminProfileRequest.getMobile());
        adminProfile.setId_number(updateAdminProfileRequest.getId_number());
        adminProfile.setCity(cityRepository.getReferenceById(updateAdminProfileRequest.getCity()));
        if (updateAdminProfileRequest.getActive()!=null){
            adminProfile.getUser().setActive(updateAdminProfileRequest.getActive());
        }
        return adminProfileRepository.save(adminProfile);
    }
}
