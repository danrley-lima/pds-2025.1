package com.danrley.product_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.danrley.product_management.dto.product.ProductRequestDTO;
import com.danrley.product_management.dto.product.ProductResponseDTO;
import com.danrley.product_management.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Produtos", description = "API para gerenciamento de produtos")
public class ProductController {

  @Autowired
  private ProductService productService;

  @PostMapping("/batch")
  @Operation(summary = "Buscar produtos por IDs", description = "Retorna uma lista de produtos com base nos IDs fornecidos")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produtos encontrados com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class)))),
      @ApiResponse(responseCode = "404", description = "Um ou mais produtos não encontrados"),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public List<ProductResponseDTO> getByIds(
      @Parameter(description = "Lista de IDs dos produtos a serem buscados") @RequestBody List<Long> ids) {
    return productService.getByIds(ids);
  }

  @GetMapping
  @Operation(summary = "Listar todos os produtos", description = "Retorna todos os produtos cadastrados no sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produtos listados com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class)))),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public List<ProductResponseDTO> getAll() {
    return productService.getAll();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar produto por ID", description = "Retorna um único produto com base no ID fornecido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso", content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public ProductResponseDTO getById(
      @Parameter(description = "ID do produto a ser buscado") @PathVariable Long id) {
    return productService.getById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Criar novo produto", description = "Cria um novo produto com base nos dados fornecidos")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Produto criado com sucesso", content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public ProductResponseDTO create(
      @Parameter(description = "Dados do produto a ser criado") @RequestBody ProductRequestDTO dto) {
    return productService.create(dto);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar produto", description = "Atualiza um produto existente com base no ID e dados fornecidos")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso", content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public ProductResponseDTO update(
      @Parameter(description = "ID do produto a ser atualizado") @PathVariable Long id,
      @Parameter(description = "Novos dados do produto") @RequestBody ProductRequestDTO dto) {
    return productService.update(id, dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(summary = "Excluir produto", description = "Remove um produto com base no ID fornecido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso"),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public void delete(
      @Parameter(description = "ID do produto a ser excluído") @PathVariable Long id) {
    productService.delete(id);
  }

  @GetMapping("/promotions")
  @Operation(summary = "Listar produtos em promoção", description = "Retorna todos os produtos que estão atualmente em promoção")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produtos em promoção listados com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class)))),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  public List<ProductResponseDTO> getProductsOnPromotion() {
    return productService.getProductsOnPromotion();
  }
}
