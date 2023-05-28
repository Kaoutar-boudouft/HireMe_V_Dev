package com.example.hireme.Service;

import com.example.hireme.Exceptions.UserAlreadyExistException;
import com.example.hireme.Model.Entity.Company;
import com.example.hireme.Model.Entity.EmployerProfile;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Model.Entity.VerificationToken;
import com.example.hireme.Model.Role;
import com.example.hireme.Repository.UserRepository;
import com.example.hireme.Repository.VerificationTokenRepository;
import com.example.hireme.Requests.CandidateRegisterRequest;
import com.example.hireme.Requests.EmployerRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService  implements UserDetailsService{
    private final UserRepository userRepository;
    private final CandidateProfileService candidateProfileService;
    private final EmployerProfileService employerProfileService;
    private final CompanyService companyService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
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
        newUser.setRole(Role.CANDIDATE);
        newUser.setActive(true);
        newUser.setPassword(passwordEncoder.encode(employerRegisterRequest.getPassword()));
        userRepository.save(newUser);
        Company newCompany = companyService.createNewCompany(employerRegisterRequest);
        employerProfileService.createNewEmployerProfile(employerRegisterRequest,newUser,newCompany);
        return newUser;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User with email "+email+" not found!"));
    }

    public void saveVerificationToken(User candidate, String verificationToken) {
        VerificationToken token = new VerificationToken(verificationToken,candidate);
        verificationTokenRepository.save(token);
    }
}
