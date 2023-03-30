package com.spring.oauth2.social.security;

import com.spring.oauth2.social.security.jwt.AuthEntryPointJwt;
import com.spring.oauth2.social.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
public class WebSecurityConfig {
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    SecurityUtil securityUtil;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and()
                .authorizeRequests()
                .requestMatchers("/", "/api/auth/**", "/login/**", "/oauth2/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login();
        ;
        http.authenticationProvider(securityUtil.authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
