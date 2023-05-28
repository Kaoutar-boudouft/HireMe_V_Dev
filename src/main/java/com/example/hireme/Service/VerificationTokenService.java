package com.example.hireme.Service;

import com.example.hireme.Model.Entity.User;
import com.example.hireme.Model.Entity.VerificationToken;
import com.example.hireme.Repository.UserRepository;
import com.example.hireme.Repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    public String verifyToken(String token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken!=null){
            if (verificationToken.getUser().isEnabled()){
                return "account already verified !";
            }
            else{
                User user = verificationToken.getUser();
                Calendar calendar = Calendar.getInstance();
                if ((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
                    verificationTokenRepository.delete(verificationToken);
                    return "token expired !";
                }
                else {
                    user.setEmail_verified_at(LocalDateTime.now());
                    verificationTokenRepository.delete(verificationToken);
                    return "";
                }
            }
        }
        else {
            return "invalid token !";
        }
    }
}
