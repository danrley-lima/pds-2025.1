package com.danrley.product_management.core.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.danrley.product_management.common.dto.product.ProductRequestDTO;
import com.danrley.product_management.common.dto.product.ProductResponseDTO;
import com.danrley.product_management.core.model.BaseProduct;
import com.danrley.product_management.core.service.BaseProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controller base abstrato para operações CRUD de produtos.
 * Implementa operações comuns que podem ser reutilizadas por todos os domínios.
 * 
 * Template Method Pattern: Define endpoints comuns, subclasses fornecem
 * serviços específicos.
 * 
 * @param <T> Tipo da entidade específica do domínio
 * @param <S> Tipo do serviço específico do domínio
 */
public abstract class BaseProductController<T extends BaseProduct, S extends BaseProductService<T>> {

  protected final S productService;

  protected BaseProductController(S productService) {
    this.productService = productService;
  }

  @GetMapping
  @Operation(summary = "Listar todos os produtos", description = "Retorna todos os produtos do domínio")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produtos listados com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class))))
  })
  public List<ProductResponseDTO> getAll() {
    return productService.getAll().stream()
        .map(productService::toResponseDTO)
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar produto por ID", description = "Retorna um produto específico pelo ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produto encontrado", content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado")
  })
  public ProductResponseDTO getById(
      @Parameter(description = "ID do produto") @PathVariable Long id) {
    T product = productService.getById(id)
        .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
    return productService.toResponseDTO(product);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Criar novo produto", description = "Cria um novo produto no domínio")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Produto criado com sucesso", content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos")
  })
  public ProductResponseDTO create(
      @Parameter(description = "Dados do produto") @RequestBody ProductRequestDTO dto) {
    T product = productService.create(dto);
    return productService.toResponseDTO(product);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar produto", description = "Atualiza um produto existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso", content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado")
  })
  public ProductResponseDTO update(
      @Parameter(description = "ID do produto") @PathVariable Long id,
      @Parameter(description = "Novos dados do produto") @RequestBody ProductRequestDTO dto) {
    T product = productService.update(id, dto);
    return productService.toResponseDTO(product);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Deletar produto", description = "Remove um produto do domínio")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado")
  })
  public void delete(
      @Parameter(description = "ID do produto") @PathVariable Long id) {
    productService.delete(id);
  }

  @GetMapping("/category/{category}")
  @Operation(summary = "Buscar por categoria", description = "Retorna produtos de uma categoria específica")
  public List<ProductResponseDTO> getByCategory(
      @Parameter(description = "Nome da categoria") @PathVariable String category) {
    return productService.getByCategory(category).stream()
        .map(productService::toResponseDTO)
        .collect(Collectors.toList());
  }

  @GetMapping("/brand/{brand}")
  @Operation(summary = "Buscar por marca", description = "Retorna produtos de uma marca específica")
  public List<ProductResponseDTO> getByBrand(
      @Parameter(description = "Nome da marca") @PathVariable String brand) {
    return productService.getByBrand(brand).stream()
        .map(productService::toResponseDTO)
        .collect(Collectors.toList());
  }

  @GetMapping("/available")
  @Operation(summary = "Buscar produtos disponíveis", description = "Retorna produtos em estoque")
  public List<ProductResponseDTO> getAvailable() {
    return productService.getAvailable().stream()
        .map(productService::toResponseDTO)
        .collect(Collectors.toList());
  }

  /**
   * Método abstrato para permitir endpoints específicos do domínio.
   * Subclasses podem implementar endpoints adicionais específicos.
   */
  protected abstract String getDomainName();
}
