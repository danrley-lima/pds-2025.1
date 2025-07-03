package com.danrley.product_management.common.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.danrley.product_management.common.dto.auth.LoginRequestDto;
import com.danrley.product_management.common.dto.auth.RecoveryJwtTokenDto;
import com.danrley.product_management.common.dto.auth.RegisterRequestDto;
import com.danrley.product_management.common.dto.user.UserResponseDto;
import com.danrley.product_management.common.exception.custom.EmailAlreadyExistsException;
import com.danrley.product_management.common.exception.custom.InvalidCredentialsException;
import com.danrley.product_management.common.exception.custom.UserNotFoundException;
import com.danrley.product_management.common.model.role.Role;
import com.danrley.product_management.common.model.user.User;
import com.danrley.product_management.common.model.user.UserRoleEnum;
import com.danrley.product_management.common.repository.UserRepository;
import com.danrley.product_management.common.security.auth.UserDetailsImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleService roleService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenService jwtTokenService;

  @Transactional
  public UserResponseDto createUser(RegisterRequestDto registerRequestDto) {

    userRepository.findByEmail(registerRequestDto.email())
        .ifPresent(user -> {
          throw new EmailAlreadyExistsException("Email " + registerRequestDto.email() + " is already registered");
        });

    User newUser = User.builder()
        .email(registerRequestDto.email())
        .password(passwordEncoder.encode(registerRequestDto.password()))
        .name(registerRequestDto.name())
        .build();

    Role defaultRole = roleService.getRoleByName(UserRoleEnum.USER);
    newUser.addRole(defaultRole);

    if (registerRequestDto.isAdmin()) {
      Role adminRole = roleService.getRoleByName(UserRoleEnum.ADMIN);
      newUser.addRole(adminRole);
    }

    userRepository.save(newUser);

    return toResponse(newUser);
  }

  public List<UserResponseDto> getAllUsers() {
    return userRepository.findAll().stream()
        .map(this::toResponse)
        .toList();
  }

  public UserResponseDto getUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    return toResponse(user);
  }

  public RecoveryJwtTokenDto authenticateUser(LoginRequestDto request) {
    try {
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.login(),
          request.password());

      Authentication authentication = authenticationManager.authenticate(authToken);
      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    } catch (BadCredentialsException ex) {
      throw new InvalidCredentialsException();
    }
  }

  private UserResponseDto toResponse(User user) {
    return new UserResponseDto(
        user.getId(),
        user.getName(),
        user.getEmail());
  }
}
