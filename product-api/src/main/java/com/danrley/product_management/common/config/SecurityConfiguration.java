package com.danrley.product_management.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.danrley.product_management.common.security.filter.UserAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final UserAuthenticationFilter userAuthenticationFilter;

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .cors(cors -> cors.configure(httpSecurity))
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .formLogin(login -> login.disable())
        .httpBasic(basic -> basic.disable())
        .authorizeHttpRequests(requests -> requests
            // .requestMatchers(
            // "/api/auth/**",
            // "/swagger-ui.html",
            // "/swagger-ui/**",
            // "/v3/api-docs/**")
            // // "/error")
            // .permitAll()
            // .anyRequest().authenticated())
            .anyRequest().permitAll())
        .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return httpSecurity.build();
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
