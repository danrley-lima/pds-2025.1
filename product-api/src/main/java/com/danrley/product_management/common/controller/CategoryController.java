package com.danrley.product_management.common.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.danrley.product_management.common.dto.category.CategoryRequestDTO;
import com.danrley.product_management.common.dto.category.CategoryResponseDTO;
import com.danrley.product_management.common.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categorias", description = "API para categorias de produtos")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService service) {
    this.categoryService = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Criar nova categoria", description = "Cria uma nova categoria com o nome fornecido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso", content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public CategoryResponseDTO create(
      @Parameter(description = "Dados da categoria a ser criada") @RequestBody CategoryRequestDTO request) {
    return categoryService.create(request);
  }

  @GetMapping
  @Operation(summary = "Listar todas as categorias", description = "Retorna todas as categorias cadastradas no sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categorias listadas com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDTO.class)))),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public List<CategoryResponseDTO> findAll() {
    return categoryService.findAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar categoria por ID", description = "Retorna uma única categoria com base no ID fornecido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso", content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public CategoryResponseDTO findById(
      @Parameter(description = "ID da categoria a ser buscada") @PathVariable Long id) {
    return categoryService.findById(id);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar categoria", description = "Atualiza uma categoria existente com base no ID e nome fornecidos")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso", content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
      @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public CategoryResponseDTO update(
      @Parameter(description = "ID da categoria a ser atualizada") @PathVariable Long id,
      @Parameter(description = "Novos dados da categoria") @RequestBody CategoryRequestDTO request) {
    return categoryService.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Excluir categoria", description = "Remove uma categoria com base no ID fornecido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso"),
      @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public void delete(
      @Parameter(description = "ID da categoria a ser excluída") @PathVariable Long id) {
    categoryService.delete(id);
  }
}