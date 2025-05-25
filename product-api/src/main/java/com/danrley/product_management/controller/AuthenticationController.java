package com.danrley.product_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danrley.product_management.dto.auth.LoginRequestDto;
import com.danrley.product_management.dto.auth.RecoveryJwtTokenDto;
import com.danrley.product_management.dto.auth.RegisterRequestDto;
import com.danrley.product_management.dto.user.UserResponseDto;
import com.danrley.product_management.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Operações relacionadas à autenticação de usuários")
@SecurityRequirement(name = "")
public class AuthenticationController {

  private final UserService userService;

  @GetMapping("/health")
  @Operation(summary = "Verificar status da API", description = "Retorna uma mensagem informando que a API está funcionando")
  @ApiResponse(responseCode = "200", description = "API funcionando")
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok("API is running");
  }

  @PostMapping("login")
  @Operation(summary = "Autenticar usuário", description = "Realiza o login e retorna um token JWT")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Login bem-sucedido", content = @Content(examples = @ExampleObject(value = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"))),
      @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
  })
  public ResponseEntity<RecoveryJwtTokenDto> login(@RequestBody @Valid LoginRequestDto request) {
    RecoveryJwtTokenDto token = userService.authenticateUser(request);

    return ResponseEntity.ok(token);
  }

  @PostMapping("/register")
  @Operation(summary = "Registrar novo usuário", description = "Cria uma nova conta de usuário")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
      @ApiResponse(responseCode = "409", description = "Email já cadastrado")
  })
  public ResponseEntity<UserResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
    return ResponseEntity.status(201).body(userService.createUser(request));
  }

}
