package com.danrley.gestao_tarefas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.danrley.gestao_tarefas.dto.auth.LoginRequestDto;
import com.danrley.gestao_tarefas.dto.auth.RecoveryJwtTokenDto;
import com.danrley.gestao_tarefas.dto.auth.RegisterRequestDto;
import com.danrley.gestao_tarefas.dto.user.UserResponseDto;
import com.danrley.gestao_tarefas.exception.custom.EmailAlreadyExistsException;
import com.danrley.gestao_tarefas.exception.custom.InvalidCredentialsException;
import com.danrley.gestao_tarefas.exception.custom.UserNotFoundException;
import com.danrley.gestao_tarefas.model.role.Role;
import com.danrley.gestao_tarefas.model.user.User;
import com.danrley.gestao_tarefas.model.user.UserRoleEnum;
import com.danrley.gestao_tarefas.repository.UserRepository;
import com.danrley.gestao_tarefas.security.auth.UserDetailsImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private RoleService roleService;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private JwtTokenService jwtTokenService;

  @InjectMocks
  private UserService userService;

  private static final String SECRET_KEY = "testeSecret";

  private User dummyUser;
  private Role userRole;

  @BeforeEach
  public void setUp() {
    // Injeta o valor do secret para os testes
    ReflectionTestUtils.setField(jwtTokenService, "SECRET_KEY", SECRET_KEY);

    dummyUser = User.builder()
        .id(1L)
        .email("test@example.com")
        .name("Test User")
        .password("encodedPassword")
        .build();

    userRole = Role.builder().id(10L).name(UserRoleEnum.USER).build();
  }

  @Test
  public void testCreateUserSuccess_UserOnly() {
    RegisterRequestDto request = new RegisterRequestDto("newuser@example.com", "New User", "password123", false);

    when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword123");
    when(roleService.getRoleByName(UserRoleEnum.USER)).thenReturn(userRole);
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
      User u = invocation.getArgument(0);
      u.setId(2L);
      return u;
    });

    UserResponseDto response = userService.createUser(request);
    assertNotNull(response);
    assertEquals(2L, response.id());
    assertEquals("New User", response.name());
    assertEquals("newuser@example.com", response.email());
  }

  @Test
  public void testCreateUserFailure_EmailAlreadyExists() {
    RegisterRequestDto request = new RegisterRequestDto("test@example.com", "Test User", "password123", false);
    when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(dummyUser));

    assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(request));
  }

  @Test
  public void testGetUserByIdSuccess() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(dummyUser));
    UserResponseDto response = userService.getUserById(1L);
    assertNotNull(response);
    assertEquals(dummyUser.getId(), response.id());
  }

  @Test
  public void testGetUserByIdNotFound() {
    when(userRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> userService.getUserById(99L));
  }

  @Test
  public void testGetAllUsers() {
    when(userRepository.findAll()).thenReturn(Arrays.asList(dummyUser));
    List<UserResponseDto> list = userService.getAllUsers();
    assertNotNull(list);
    assertEquals(1, list.size());
    assertEquals(dummyUser.getId(), list.get(0).id());
  }

  @Test
  public void testAuthenticateUserSuccess() {
    LoginRequestDto loginRequest = new LoginRequestDto("test@example.com", "password123");

    UserDetailsImpl userDetails = new UserDetailsImpl(dummyUser);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);

    when(jwtTokenService.generateToken(userDetails)).thenReturn("dummyJwtToken");

    RecoveryJwtTokenDto recoveryDto = userService.authenticateUser(loginRequest);
    assertNotNull(recoveryDto);
    assertEquals("dummyJwtToken", recoveryDto.token());
  }

  @Test
  public void testAuthenticateUserFailure_BadCredentials() {
    LoginRequestDto loginRequest = new LoginRequestDto("test@example.com", "wrongPassword");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException("Bad credentials"));

    assertThrows(InvalidCredentialsException.class, () -> userService.authenticateUser(loginRequest));
  }
}
