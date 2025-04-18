package com.danrley.gestao_tarefas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.danrley.gestao_tarefas.exception.custom.InvalidTokenException;
import com.danrley.gestao_tarefas.model.user.User;
import com.danrley.gestao_tarefas.security.auth.UserDetailsImpl;

public class JwtTokenServiceTest {

  private JwtTokenService jwtTokenService;
  private static final String SECRET_KEY = "testeSecret";

  @BeforeEach
  public void setUp() {
    jwtTokenService = new JwtTokenService();
    // Injeta o valor do secret para os testes
    ReflectionTestUtils.setField(jwtTokenService, "SECRET_KEY", SECRET_KEY);
  }

  private UserDetailsImpl createDummyUser() {
    User dummyUser = User.builder()
        .id(1L)
        .email("test@example.com")
        .password("password")
        .roles(new HashSet<>())
        .build();
    return new UserDetailsImpl(dummyUser);
  }

  @Test
  public void testGenerateTokenAndExtractSubject() {
    UserDetailsImpl user = createDummyUser();
    String token = jwtTokenService.generateToken(user);
    assertNotNull(token);

    String subject = jwtTokenService.getSubjectFromToken(token);
    assertEquals(user.getEmail(), subject);
  }

  @Test
  public void testGetSubjectFromInvalidToken() {
    assertThrows(InvalidTokenException.class, () -> jwtTokenService.getSubjectFromToken(null));

    assertThrows(InvalidTokenException.class, () -> jwtTokenService.getSubjectFromToken(" "));

    assertThrows(InvalidTokenException.class, () -> jwtTokenService.getSubjectFromToken("invalid-token"));
  }

  @Test
  public void testTokenWithDifferentSecretThrowsException() {
    UserDetailsImpl user = createDummyUser();
    String token = jwtTokenService.generateToken(user);
    ReflectionTestUtils.setField(jwtTokenService, "SECRET_KEY", "differentSecret");
    assertThrows(InvalidTokenException.class, () -> jwtTokenService.getSubjectFromToken(token));
  }

  @Test
  public void testGenerateTokenException() {
    UserDetailsImpl user = createDummyUser();
    ReflectionTestUtils.setField(jwtTokenService, "SECRET_KEY", "");
    assertThrows(RuntimeException.class, () -> jwtTokenService.generateToken(user));
  }
}
