package com.example.hireme.Service.Auth;

import com.example.hireme.Model.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

public class TokenService {
    @Autowired
    private Environment env;
    private final byte[] secret = Base64.getDecoder().decode(env.getProperty("token.secret"));

    public String generate(User user){
        Instant now = Instant.now();
        return Jwts.builder()
                .claim("user_id",user.getId())
                .setIssuedAt(Date.from (now))
                .setExpiration(Date.from(now.plus( 3, ChronoUnit.HOURS)))
                .signWith(Keys.hmacShaKeyFor(secret))
                .compact();
    }

    public Jws<Claims> extractTokenResult(String token){
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secret))
                .parseClaimsJws (token);
    }

    public boolean isExpired(String token){
        Jws<Claims> result = extractTokenResult(token);
        Date issue = result.getBody().getIssuedAt();
        Date exp = result.getBody().getExpiration();

        long difference_In_Time = exp.getTime() - issue.getTime();
        long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;

        if (difference_In_Hours < 3){return false;}
        else{return true;}
    }

    public boolean isUser(String token, User user){
        Jws<Claims> result = extractTokenResult(token);
        Long userId = result.getBody().get("user_id",Long.class);
        if (Objects.equals(userId, user.getId())){return true;}
        else {return false;}
    }


}
