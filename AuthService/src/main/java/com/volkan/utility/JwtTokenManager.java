package com.volkan.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.volkan.repository.enums.ERole;
import com.volkan.repository.enums.EStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class JwtTokenManager {
    @Value("${auth-service.secret-key}")
    String secretKey;
    @Value("${auth-service.issuer}")
    String issuer;
    @Value("${auth-service.audience}")
    String audience;

    public Optional<String> createToken(Long id, ERole role, EStatus status){
        String token= null;
        Long exDate = 1000L*60*15;
        try{

            token = JWT.create().withAudience(audience)
                    .withClaim("id",id)
                    .withClaim("howtopage","AuthMicroService")
                    .withClaim("lastjoin", System.currentTimeMillis())
                    .withClaim("role",String.valueOf(role))
                    .withClaim("status", String.valueOf(status))
                    .withIssuer(issuer)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis()+exDate))
                    .sign(Algorithm.HMAC512(secretKey));
            return Optional.of(token);
        }catch (Exception exception){
            return Optional.empty();
        }
    }
    public Optional<Long> getIdFromToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if(decodedJWT== null)
                return Optional.empty();
            Long id = decodedJWT.getClaim("id").asLong();
            return Optional.of(id);
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

}
