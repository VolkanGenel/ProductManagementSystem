package com.volkan.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserSecurityConfig {

    @Bean
    JwtTokenFilter getTokenFilter() {
        return new JwtTokenFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(req->
                req.requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/product/**").permitAll()         //.hasAuthority("ADMIN")
                        .requestMatchers("/auth/**").permitAll()    //.hasAuthority("ADMIN")

                        .anyRequest()
                        .authenticated()
        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        //log.info("*****  Tüm istekler buradan geçecek. *****");

        httpSecurity.addFilterBefore(getTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
