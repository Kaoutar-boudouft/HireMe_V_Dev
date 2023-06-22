package com.example.hireme.Service;

import com.example.hireme.Exceptions.UserAlreadyExistException;
import com.example.hireme.Model.Entity.*;
import com.example.hireme.Model.Role;
import com.example.hireme.Repository.*;
import com.example.hireme.Requests.Admin.CreateAdminRequest;
import com.example.hireme.Requests.Candidate.CandidateRegisterRequest;
import com.example.hireme.Requests.EmailUpdateRequest;
import com.example.hireme.Requests.Employer.EmployerRegisterRequest;
import com.example.hireme.Requests.PasswordUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService  implements UserDetailsService{
    private final UserRepository userRepository;
    private final CandidateProfileService candidateProfileService;
    private final CandidateProfileRepository candidateProfileRepository;
    private final EmployerProfileRepository employerProfileRepository;
    private final AdminProfileRepository adminProfileRepository;
    private final CompanyRepository companyRepository;
    private final EmployerProfileService employerProfileService;
    private final AdminProfileService adminProfileService;
    private final CompanyService companyService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final JobOfferRepository jobOfferRepository;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public User getById(Long id){
        return userRepository.getReferenceById(id);
    }

    public User registerCandidateUser(CandidateRegisterRequest candidateRegisterRequest){
        Optional<User> user = this.findByEmail(candidateRegisterRequest.getEmail());
        if (user.isPresent()){
            throw new UserAlreadyExistException("User with email "+candidateRegisterRequest.getEmail()+" already exist !");
        }
        User newUser = new User();
        newUser.setEmail(candidateRegisterRequest.getEmail());
        newUser.setCreated_at(LocalDateTime.now());
        newUser.setRole(Role.CANDIDATE);
        newUser.setActive(true);
        newUser.setPassword(passwordEncoder.encode(candidateRegisterRequest.getPassword()));
        userRepository.save(newUser);
        candidateProfileService.createNewCandidateProfile(candidateRegisterRequest,newUser);
        return newUser;
    }

    public User registerEmployerUser(EmployerRegisterRequest employerRegisterRequest){
        Optional<User> user = this.findByEmail(employerRegisterRequest.getEmail());
        if (user.isPresent()){
            throw new UserAlreadyExistException("User with email "+employerRegisterRequest.getEmail()+" already exist !");
        }
        User newUser = new User();
        newUser.setEmail(employerRegisterRequest.getEmail());
        newUser.setCreated_at(LocalDateTime.now());
        newUser.setRole(Role.EMPLOYER);
        newUser.setActive(false);
        newUser.setPassword(passwordEncoder.encode(employerRegisterRequest.getPassword()));
        userRepository.save(newUser);
        Company newCompany = companyService.createNewCompany(employerRegisterRequest);
        employerProfileService.createNewEmployerProfile(employerRegisterRequest,newUser,newCompany);
        return newUser;
    }

    public User registerAdminUser(CreateAdminRequest createAdminRequest){
        Optional<User> user = this.findByEmail(createAdminRequest.getEmail());
        if (user.isPresent()){
            throw new UserAlreadyExistException("User with email "+createAdminRequest.getEmail()+" already exist !");
        }
        User newUser = new User();
        newUser.setEmail(createAdminRequest.getEmail());
        newUser.setCreated_at(LocalDateTime.now());
        newUser.setRole(Role.ADMIN);
        newUser.setActive(true);
        newUser.setPassword(passwordEncoder.encode(createAdminRequest.getPassword()));
        userRepository.save(newUser);
        System.out.println("inside user sevice kaoutar "+newUser.getId());
        adminProfileService.createNewAdminProfile(createAdminRequest,newUser);
        return newUser;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User with email "+email+" not found!"));
    }

    public User updateUserEmail(User user, EmailUpdateRequest emailUpdateRequest){
        Optional<User> u = findByEmail(emailUpdateRequest.getEmail());
        if (u.isPresent()){
            throw new UserAlreadyExistException("User with email "+user.getEmail()+" already exist !");
        }
        user.setEmail(emailUpdateRequest.getEmail());
        user.setEmail_verified_at(null);
        return userRepository.save(user);
    }

    public User updateUserPassword(User user, PasswordUpdateRequest passwordUpdateRequest){
        Optional<User> u = findByEmail(user.getEmail());
        if (u.isPresent()){
            user.setPassword(passwordEncoder.encode(passwordUpdateRequest.getPassword()));
            return userRepository.save(user);
        }
        return null;
    }


    public void saveVerificationToken(User candidate, String verificationToken) {
        VerificationToken token = new VerificationToken(verificationToken,candidate);
        verificationTokenRepository.save(token);
    }

    public List<User> getUsersByRole(Role role){
        return userRepository.findByRole(role);
    }

    public void removeUser(User user){
        if (user.getRole().equals(Role.CANDIDATE)){
            CandidateProfile candidateProfile = candidateProfileService.getCandidateProfile(user.getId());
            jobOfferRepository.deleteCandidaturesByCandidate(candidateProfile.getId());
            candidateProfileRepository.delete(candidateProfile);
        }
        else if (user.getRole().equals(Role.EMPLOYER)) {
            EmployerProfile employerProfile = employerProfileService.getEmployerProfile(user.getId());
            Company company = employerProfile.getCompany();
            jobOfferRepository.deleteJobsByCompany(company.getId());
            employerProfileRepository.delete(employerProfile);
            companyRepository.delete(company);
        }
        else if (user.getRole().equals(Role.ADMIN)) {
            AdminProfile adminProfile = adminProfileService.getAdminProfile(user.getId());
            adminProfileRepository.delete(adminProfile);
        }
        userRepository.delete(user);

    }
}
