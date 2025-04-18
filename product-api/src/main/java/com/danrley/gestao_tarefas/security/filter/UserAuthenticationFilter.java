package com.danrley.gestao_tarefas.security.filter;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.danrley.gestao_tarefas.model.user.User;
import com.danrley.gestao_tarefas.repository.UserRepository;
import com.danrley.gestao_tarefas.security.auth.UserDetailsImpl;
import com.danrley.gestao_tarefas.service.JwtTokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenService jwtTokenService;
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      filterChain.doFilter(request, response);
      return;
    }

    if (isPublicRoute(request)) {
      filterChain.doFilter(request, response);
      return;
    }
    String token = recoveryToken(request);
    if (token != null) {
      String subject = jwtTokenService.getSubjectFromToken(token);
      User user = userRepository.findByEmail(subject).get();
      UserDetailsImpl userDetails = new UserDetailsImpl(user);

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          userDetails.getUsername(), null,
          userDetails.getAuthorities());

      // Define o usuário autenticado no contexto de segurança
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } else {
      throw new RuntimeException("The token is missing.");
    }
    filterChain.doFilter(request, response);
  }

  private String recoveryToken(HttpServletRequest request) {
    String authorizationHeader = request.getHeader("Authorization");
    if (authorizationHeader != null) {
      return authorizationHeader.replace("Bearer ", "");
    }
    return null;
  }

  private boolean isPublicRoute(HttpServletRequest request) {
    String path = request.getServletPath();
    return path.startsWith("/api/auth") ||
        path.startsWith("/swagger-ui") ||
        path.startsWith("/v3/api-docs");
  }
}
