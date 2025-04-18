package com.danrley.gestao_tarefas.service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.danrley.gestao_tarefas.exception.custom.InvalidTokenException;
import com.danrley.gestao_tarefas.exception.custom.TokenGenerationException;
import com.danrley.gestao_tarefas.security.auth.UserDetailsImpl;

@Service
public class JwtTokenService {
  @Value("${api.security.token.secret}")
  private String SECRET_KEY;

  public String generateToken(UserDetailsImpl user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
      return JWT.create()
          .withIssuer("auth-api")
          .withIssuedAt(creationDate())
          .withExpiresAt(expirationDate())
          .withSubject(user.getEmail())
          .sign(algorithm);
    } catch (JWTCreationException | IllegalArgumentException ex) {
      throw new TokenGenerationException("Failed to generate authentication token", ex);
    }
  }

  public String getSubjectFromToken(String token) {
    if (token == null || token.isBlank()) {
      throw new InvalidTokenException("No token provided");
    }

    try {
      Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
      return JWT.require(algorithm)
          .withIssuer("auth-api")
          .build()
          .verify(token)
          .getSubject();
    } catch (JWTVerificationException | IllegalArgumentException ex) {
      throw new InvalidTokenException("Invalid or expired token");
    }
  }

  private Instant creationDate() {
    return ZonedDateTime.now(ZoneId.of("America/Recife")).toInstant();
  }

  private Instant expirationDate() {
    return ZonedDateTime.now(ZoneId.of("America/Recife")).plusHours(4).toInstant();
  }
}
