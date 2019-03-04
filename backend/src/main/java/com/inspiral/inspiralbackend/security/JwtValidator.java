package com.inspiral.inspiralbackend.security;

import com.inspiral.inspiralbackend.models.JwtTrainer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;


@Component
public class JwtValidator {


    private String secret = "morvai";

    public JwtTrainer validate(String token) {

        JwtTrainer jwtTrainer = null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            jwtTrainer = new JwtTrainer();

            jwtTrainer.setEmail(body.getSubject());
            jwtTrainer.setId(Integer.parseInt((String) body.get("id")));
            jwtTrainer.setPassword((String) body.get("password"));
            jwtTrainer.setRole((String) body.get("role"));
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return jwtTrainer;
    }
}
