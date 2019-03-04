package com.inspiral.inspiralbackend.security;

import com.inspiral.inspiralbackend.models.JwtTrainer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {


    public String generate(JwtTrainer jwtTrainer) {

        Claims claims = Jwts.claims()
                .setSubject(jwtTrainer.getEmail());
        claims.put("id", String.valueOf(jwtTrainer.getId()));
        claims.put("password", jwtTrainer.getPassword());
        claims.put("role", jwtTrainer.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, "morvai")
                .compact();
    }
}
