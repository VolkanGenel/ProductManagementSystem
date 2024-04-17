package com.volkan.config.security;

import com.volkan.dto.response.GetIdRoleStatusFromTokenResponseDto;
import com.volkan.exception.ProductServiceException;
import com.volkan.exception.EErrorType;
import com.volkan.utility.JwtTokenManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.validator.internal.constraintvalidators.hv.ru.INNValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    JwtTokenManager jwtTokenManager;
    @Autowired
    JwtUserDetails jwtUserDetails;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, ProductServiceException {

        final String userHeader = request.getHeader("Authorization");
        if (userHeader!=null && userHeader.startsWith("Bearer ")) {
            Optional<String> token = Optional.of(userHeader.substring(7));
            Optional<GetIdRoleStatusFromTokenResponseDto> dto = jwtTokenManager.getIdRoleStatusFromToken(token.get());
            if (!dto.isPresent()) {
                sendResponse(response,EErrorType.INVALID_TOKEN);
            }
            else if (!Optional.of(dto.get().getStatus()).isPresent()) {
                sendResponse(response,EErrorType.INVALID_TOKEN);
            }
            else if (!dto.get().getStatus().toString().equals("ACTIVE")) {
                sendResponse(response,EErrorType.STATUS_NOT_ACTIVE);
            }
            else if (Optional.of(dto.get().getRole()).isPresent() && Optional.of(dto.get().getId()).isPresent()) {
                UserDetails userDetails = jwtUserDetails.loadUserByUserRole(dto.get().getRole().toString(),dto.get().getId());
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                sendResponse(response,EErrorType.INVALID_TOKEN);
            }

        }
            filterChain.doFilter(request, response);
    }

    public void sendResponse (HttpServletResponse response, EErrorType errorType) throws IOException {
        response.setHeader("Error-Reason",errorType.getMessage());
        response.setStatus(errorType.getCode());
        response.sendError(errorType.getCode(),errorType.getMessage());
    }
}
