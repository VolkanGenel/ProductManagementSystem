package com.volkan.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.volkan.dto.response.GetIdRoleStatusFromTokenResponseDto;
import com.volkan.repository.enums.ERole;
import com.volkan.repository.enums.EStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenManager {
    @Value("${auth-service.secret-key}")
    String secretKey;
    @Value("${auth-service.issuer}")
    String issuer;

    public Optional<GetIdRoleStatusFromTokenResponseDto> getIdRoleStatusFromToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if(decodedJWT== null)
                return Optional.empty();
            Long id = decodedJWT.getClaim("id").asLong();
            ERole role = decodedJWT.getClaim("role").as(ERole.class);
            EStatus status = decodedJWT.getClaim("status").as(EStatus.class);
            GetIdRoleStatusFromTokenResponseDto getIdRoleStatusFromTokenResponseDto = GetIdRoleStatusFromTokenResponseDto.builder().id(id).role(role).status(status).build();
            return Optional.of(getIdRoleStatusFromTokenResponseDto);
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

}
