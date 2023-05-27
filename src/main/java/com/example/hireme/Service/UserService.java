package com.example.hireme.Service;

import com.example.hireme.Exceptions.UserAlreadyExistException;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.Model.Role;
import com.example.hireme.Repository.UserRepository;
import com.example.hireme.Requests.CandidateRegisterRequest;
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
    private final PasswordEncoder passwordEncoder;

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


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
