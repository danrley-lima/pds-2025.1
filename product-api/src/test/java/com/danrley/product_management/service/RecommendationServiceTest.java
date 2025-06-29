package com.danrley.product_management.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.danrley.product_management.dto.product.ProductResponseDTO;
import com.danrley.product_management.dto.recommendation.ProductNotFoundDTO;
import com.danrley.product_management.dto.recommendation.ProductOutDTO;
import com.danrley.product_management.dto.recommendation.RecommendationRequestDTO;
import com.danrley.product_management.dto.recommendation.RecommendationResponseDTO;
import com.danrley.product_management.enums.RequestCategory;
import com.danrley.product_management.exception.custom.RecommendationException;
import com.danrley.product_management.service.llm.AbstractLLMHandler;
import com.danrley.product_management.service.llm.LLMClassifierService;
import com.danrley.product_management.service.llm.ProductLLMHandler;
import com.danrley.product_management.service.llm.PromotionLLMHandler;
import com.danrley.product_management.service.llm.RecipeLLMHandler;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private LLMClassifierService classifierService;
    
    @Mock
    private RecipeLLMHandler recipeLLMHandler;
    
    @Mock
    private ProductLLMHandler productLLMHandler;
    
    @Mock
    private PromotionLLMHandler promotionLLMHandler;
    
    @Mock
    private ProductService productService;

    private RecommendationService recommendationService;

    @BeforeEach
    void setUp() {
        recommendationService = new RecommendationService(
            classifierService,
            recipeLLMHandler,
            productLLMHandler,
            promotionLLMHandler,
            productService
        );
    }

    @Test
    void testGetRecommendations_RecipeCategory() {
        // Arrange
        RecommendationRequestDTO request = new RecommendationRequestDTO();
        request.setCustomerMessage("Quero fazer um bolo de chocolate");
        request.setProducts(createMockProducts());

        when(classifierService.classifyMessage("Quero fazer um bolo de chocolate"))
            .thenReturn(RequestCategory.RECIPE);

        AbstractLLMHandler.SearchResult searchResult = createMockSearchResult();
        when(recipeLLMHandler.searchProducts(anyString(), anyList()))
            .thenReturn(searchResult);

        // Act
        RecommendationResponseDTO response = recommendationService.getRecommendations(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getProducts());
        assertNotNull(response.getNotFoundProducts());
        assertEquals(2, response.getProducts().size());
        assertEquals(1, response.getNotFoundProducts().size());

        verify(classifierService).classifyMessage("Quero fazer um bolo de chocolate");
        verify(recipeLLMHandler).searchProducts(eq("Quero fazer um bolo de chocolate"), eq(request.getProducts()));
        verify(productLLMHandler, never()).searchProducts(anyString(), anyList());
        verify(promotionLLMHandler, never()).searchProducts(anyString(), anyList());
    }

    @Test
    void testGetRecommendations_ProductSearchCategory() {
        // Arrange
        RecommendationRequestDTO request = new RecommendationRequestDTO();
        request.setCustomerMessage("Preciso de açúcar");
        request.setProducts(createMockProducts());

        when(classifierService.classifyMessage("Preciso de açúcar"))
            .thenReturn(RequestCategory.SEARCH_PRODUCT);

        AbstractLLMHandler.SearchResult searchResult = createMockSearchResult();
        when(productLLMHandler.searchProducts(anyString(), anyList()))
            .thenReturn(searchResult);

        // Act
        RecommendationResponseDTO response = recommendationService.getRecommendations(request);

        // Assert
        assertNotNull(response);
        verify(classifierService).classifyMessage("Preciso de açúcar");
        verify(productLLMHandler).searchProducts(eq("Preciso de açúcar"), eq(request.getProducts()));
        verify(recipeLLMHandler, never()).searchProducts(anyString(), anyList());
        verify(promotionLLMHandler, never()).searchProducts(anyString(), anyList());
    }

    @Test
    void testGetRecommendations_PromotionCategory() {
        // Arrange
        RecommendationRequestDTO request = new RecommendationRequestDTO();
        request.setCustomerMessage("Quais produtos estão em promoção?");
        request.setProducts(createMockProducts());

        when(classifierService.classifyMessage("Quais produtos estão em promoção?"))
            .thenReturn(RequestCategory.SEARCH_PROMOTION);

        AbstractLLMHandler.SearchResult searchResult = createMockSearchResult();
        when(promotionLLMHandler.searchProducts(anyString(), anyList()))
            .thenReturn(searchResult);

        // Act
        RecommendationResponseDTO response = recommendationService.getRecommendations(request);

        // Assert
        assertNotNull(response);
        verify(classifierService).classifyMessage("Quais produtos estão em promoção?");
        verify(promotionLLMHandler).searchProducts(eq("Quais produtos estão em promoção?"), eq(request.getProducts()));
        verify(recipeLLMHandler, never()).searchProducts(anyString(), anyList());
        verify(productLLMHandler, never()).searchProducts(anyString(), anyList());
    }

    @Test
    void testGetRecommendations_EmptyProductsUsesProductService() {
        // Arrange
        RecommendationRequestDTO request = new RecommendationRequestDTO();
        request.setCustomerMessage("Quero fazer um bolo");
        request.setProducts(null); // Produtos vazios

        List<ProductResponseDTO> allProducts = createMockProducts();
        when(productService.getAll()).thenReturn(allProducts);

        when(classifierService.classifyMessage("Quero fazer um bolo"))
            .thenReturn(RequestCategory.RECIPE);

        AbstractLLMHandler.SearchResult searchResult = createMockSearchResult();
        when(recipeLLMHandler.searchProducts(anyString(), anyList()))
            .thenReturn(searchResult);

        // Act
        RecommendationResponseDTO response = recommendationService.getRecommendations(request);

        // Assert
        assertNotNull(response);
        verify(productService).getAll();
        verify(recipeLLMHandler).searchProducts(eq("Quero fazer um bolo"), eq(allProducts));
    }

    @Test
    void testGetRecommendations_DefaultCategory() {
        // Arrange
        RecommendationRequestDTO request = new RecommendationRequestDTO();
        request.setCustomerMessage("Mensagem não classificada");
        request.setProducts(createMockProducts());

        when(classifierService.classifyMessage("Mensagem não classificada"))
            .thenReturn(RequestCategory.UNKNOWN);

        AbstractLLMHandler.SearchResult searchResult = createMockSearchResult();
        when(productLLMHandler.searchProducts(anyString(), anyList()))
            .thenReturn(searchResult);

        // Act
        RecommendationResponseDTO response = recommendationService.getRecommendations(request);

        // Assert
        assertNotNull(response);
        verify(productLLMHandler).searchProducts(eq("Mensagem não classificada"), eq(request.getProducts()));
    }

    @Test
    void testGetRecommendations_ExceptionHandling() {
        // Arrange
        RecommendationRequestDTO request = new RecommendationRequestDTO();
        request.setCustomerMessage("Teste erro");
        request.setProducts(createMockProducts());

        when(classifierService.classifyMessage("Teste erro"))
            .thenThrow(new RuntimeException("Erro no classificador"));

        // Act & Assert
        RecommendationException exception = assertThrows(
            RecommendationException.class,
            () -> recommendationService.getRecommendations(request)
        );

        assertTrue(exception.getMessage().contains("Erro ao processar recomendação"));
        assertNotNull(exception.getCause());
    }

    private List<ProductResponseDTO> createMockProducts() {
        ProductResponseDTO product1 = new ProductResponseDTO();
        product1.id = 1L;
        product1.name = "Farinha de Trigo";
        product1.brand = "Marca A";
        product1.unitPrice = 5.50;

        ProductResponseDTO product2 = new ProductResponseDTO();
        product2.id = 2L;
        product2.name = "Açúcar";
        product2.brand = "Marca B";
        product2.unitPrice = 3.20;

        return Arrays.asList(product1, product2);
    }

    private AbstractLLMHandler.SearchResult createMockSearchResult() {
        ProductOutDTO product1 = new ProductOutDTO();
        product1.setId("1");
        product1.setName("Farinha de Trigo");
        product1.setBrand("Marca A");
        product1.setUnitPrice("5.50");

        ProductOutDTO product2 = new ProductOutDTO();
        product2.setId("2");
        product2.setName("Açúcar");
        product2.setBrand("Marca B");
        product2.setUnitPrice("3.20");

        ProductNotFoundDTO notFound = new ProductNotFoundDTO();
        notFound.setName("Chocolate em Pó");
        notFound.setQuantity("200g");

        return new AbstractLLMHandler.SearchResult(
            Arrays.asList(product1, product2),
            Arrays.asList(notFound)
        );
    }
}
