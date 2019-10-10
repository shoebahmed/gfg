package com.gfg.test.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Collections;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfg.controller.ProductControllerImpl;
import com.gfg.model.domain.Product;
import com.gfg.model.dto.ProductDTOV1;
import com.gfg.repository.ProductRepository;
import com.gfg.service.DTOService;
import com.gfg.test.BaseTest;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProductControllerTest extends BaseTest {

  @InjectMocks
  private ProductControllerImpl productController;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private DTOService dtoService;

  private MockMvc mvc;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    initializeMVC();
    initializeProduct();
    initializeProductDTO();

    this.objectMapper = new ObjectMapper();
  }

  private void initializeMVC() {
    mvc = standaloneSetup(productController).build();
  }

  @Test
  void getAllProduct_ProductList_Ok() throws Exception {

    Page<Product> pagedResponse = new PageImpl<>(Collections.singletonList(product));
    Mockito.when(productRepository.findByTitleOrDescription(any(String.class), any(Pageable.class)))
        .thenReturn(pagedResponse);
    Mockito.when(dtoService.convertProductToProductDTO(product)).thenReturn(productDTOV1);

    MockHttpServletResponse response =
        mvc.perform(get(APIURL).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).contains(productDTOV1.getId());
    assertThat(response.getContentAsString()).contains(productDTOV1.getTitle());
    assertThat(response.getContentAsString()).contains(productDTOV1.getDescription());
    assertThat(response.getContentAsString()).contains(productDTOV1.getBrand());
  }

  @Test
  void getProductById_Product_Ok() throws Exception {

    Mockito.when(productRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(product));
    Mockito.when(dtoService.convertProductToProductDTO(product)).thenReturn(productDTOV1);

    MockHttpServletResponse response =
        mvc.perform(get(APIURL.concat(productDTOV1.getId())).accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).contains(productDTOV1.getId());
    assertThat(response.getContentAsString()).contains(productDTOV1.getTitle());
    assertThat(response.getContentAsString()).contains(productDTOV1.getDescription());
    assertThat(response.getContentAsString()).contains(productDTOV1.getBrand());
  }

  @Test
  void getProductById_NotFound() throws Exception {

    Mockito.when(productRepository.findById(any(ObjectId.class))).thenReturn(Optional.empty());

    MockHttpServletResponse response =
        mvc.perform(get(APIURL.concat(productDTOV1.getId())).accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }

  @Test
  void createProduct_Product_Ok() throws Exception {

    Mockito.when(dtoService.convertProductDTOToProduct(any(ProductDTOV1.class)))
        .thenReturn(product);
    Mockito.when(productRepository.save(product)).thenReturn(product);
    Mockito.when(dtoService.convertProductToProductDTO(any(Product.class)))
        .thenReturn(productDTOV1);

    MockHttpServletResponse response = mvc.perform(post(APIURL).accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(productDTOV1))
        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    assertThat(response.getContentAsString()).contains(productDTOV1.getId());
    assertThat(response.getContentAsString()).contains(productDTOV1.getTitle());
    assertThat(response.getContentAsString()).contains(productDTOV1.getDescription());
    assertThat(response.getContentAsString()).contains(productDTOV1.getBrand());
  }

  @Test
  void updateProduct_Product_Ok() throws Exception {

    productDTOV1.setTitle("New Title");

    Mockito.when(productRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(product));
    Mockito.when(dtoService.convertProductDTOToProduct(any(ProductDTOV1.class)))
        .thenReturn(product);
    Mockito.when(productRepository.save(product)).thenReturn(product);
    Mockito.when(dtoService.convertProductToProductDTO(any(Product.class)))
        .thenReturn(productDTOV1);

    MockHttpServletResponse response = mvc.perform(put(APIURL.concat(productDTOV1.getId()))
        .accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(productDTOV1))
        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).contains(productDTOV1.getId());
    assertThat(response.getContentAsString()).contains(productDTOV1.getTitle());
  }

  @Test
  void updateProduct_Product_Created() throws Exception {

    productDTOV1.setTitle("New Title");

    Mockito.when(productRepository.findById(any(ObjectId.class))).thenReturn(Optional.empty());
    Mockito.when(dtoService.convertProductDTOToProduct(any(ProductDTOV1.class)))
        .thenReturn(product);
    Mockito.when(productRepository.save(product)).thenReturn(product);
    Mockito.when(dtoService.convertProductToProductDTO(any(Product.class)))
        .thenReturn(productDTOV1);

    MockHttpServletResponse response = mvc.perform(put(APIURL.concat(productDTOV1.getId()))
        .accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(productDTOV1))
        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    assertThat(response.getContentAsString()).contains(productDTOV1.getId());
    assertThat(response.getContentAsString()).contains(productDTOV1.getTitle());
  }

  @Test
  void deleteProduct_Present_NoContent() throws Exception {

    Mockito.when(productRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(product));

    MockHttpServletResponse response =
        mvc.perform(delete(APIURL.concat(productDTOV1.getId())).accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }

  @Test
  void deleteProduct_NotPresent_NotFound() throws Exception {

    Mockito.when(productRepository.findById(any(ObjectId.class))).thenReturn(Optional.empty());

    MockHttpServletResponse response =
        mvc.perform(delete(APIURL.concat(productDTOV1.getId())).accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }

  @Test
  void createProductBatch_ProductList_Ok() throws Exception {

    Mockito.when(dtoService.convertProductDTOToProduct(any(ProductDTOV1.class)))
        .thenReturn(product);
    Mockito.when(productRepository.saveAll(any())).thenReturn(Collections.singletonList(product));
    Mockito.when(dtoService.convertProductToProductDTO(any(Product.class)))
        .thenReturn(productDTOV1);

    MockHttpServletResponse response =
        mvc.perform(post(APIURL.concat("batch")).accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Collections.singletonList(productDTOV1)))
            .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    assertThat(response.getContentAsString()).contains(productDTOV1.getId());
    assertThat(response.getContentAsString()).contains(productDTOV1.getTitle());
    assertThat(response.getContentAsString()).contains(productDTOV1.getDescription());
    assertThat(response.getContentAsString()).contains(productDTOV1.getBrand());
  }
}
