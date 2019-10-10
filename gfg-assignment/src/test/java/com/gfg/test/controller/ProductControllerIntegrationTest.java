package com.gfg.test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfg.AssignmentApplication;
import com.gfg.model.domain.Product;
import com.gfg.model.dto.ProductDTOV1;
import com.gfg.repository.ProductRepository;
import com.gfg.security.util.JwtTokenUtility;
import com.gfg.service.DTOService;
import com.gfg.test.BaseTest;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AssignmentApplication.class)
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest extends BaseTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private JwtTokenUtility jwtTokenUtility;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private DTOService dtoService;

  @BeforeEach
  void setup() {

    initializeProduct();
    initializeProductDTO();
    initializeProductInDatabase();
  }

  private void initializeProductInDatabase() {

    productRepository.deleteAll();

    product.setTitle("T-Shirt");
    product.setDescription("New Arrival");
    product.setBrand("lucas");
    product.setPrice(59.89);
    product.setColor("white");

    Product productTwo = new Product();
    productTwo.setTitle("Nike Runner");
    productTwo.setDescription("Latest collection from Nike.");
    productTwo.setBrand("nike");
    productTwo.setPrice(56.89);
    productTwo.setColor("white");

    Product productThree = new Product();
    productThree.setTitle("Wedding Dress");
    productThree.setDescription("Designer wedding dress");
    productThree.setBrand("maya");
    productThree.setPrice(76.89);
    productThree.setColor("white");

    productRepository.save(product);
    productRepository.save(productTwo);
    productRepository.save(productThree);

  }

  private void initializeProductToVerifyPagination() {

    productRepository.deleteAll();

    for (int i = 0; i < 70; i++) {
      Product product = new Product();
      product.setTitle("Nike Runner");
      product.setDescription("Latest collection from Nike.");
      product.setBrand("nike");
      product.setPrice(86.89);
      product.setColor("white");

      productRepository.save(product);
    }
  }

  private void initializeProductToVerifySortDatabase() {

    Product product = new Product();
    product.setTitle("Nike Runner");
    product.setDescription("Latest collection from Nike. Available at discounted price.");
    product.setBrand("nike");
    product.setPrice(56.89);
    product.setColor("white");

    productRepository.save(product);
  }

  @Test
  public void getProducts_Authorized_List_Ok() throws Exception {

    initializeAuthorizedUserHeader(jwtTokenUtility);

    mockMvc
        .perform(
            get(APIURL).headers(headerForAuthorizedUser).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.*", hasSize(3)));
  }

  @Test
  public void getProducts_UnAuthorized_List_UnAuthorized() throws Exception {

    initializeUnAuthorizedUserHeader(jwtTokenUtility);

    mockMvc
        .perform(
            get(APIURL).headers(headerForUnAuthorizedUser).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void getProducts_Pagination_List_Ok() throws Exception {

    initializeAuthorizedUserHeader(jwtTokenUtility);
    initializeProductToVerifyPagination();

    mockMvc
        .perform(get(APIURL).param("pageSize", "20").headers(headerForAuthorizedUser)
            .contentType("application/json"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.*", hasSize(20)))
        .andExpect(header().string("total-pages", "4"))
        .andExpect(header().string("page-number", "0"));
  }

  @Test
  public void getProducts_Pagination_PageNumber_List_Ok() throws Exception {

    initializeAuthorizedUserHeader(jwtTokenUtility);
    initializeProductToVerifyPagination();

    mockMvc
        .perform(get(APIURL).param("pageSize", "20").param("pageNo", "3")
            .headers(headerForAuthorizedUser).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.*", hasSize(10)))
        .andExpect(header().string("total-pages", "4"))
        .andExpect(header().string("page-number", "3"));
  }

  @Test
  public void getProducts_Pagination_SortByPriceAsc_List_Ok() throws Exception {

    initializeAuthorizedUserHeader(jwtTokenUtility);
    initializeProductToVerifyPagination();
    initializeProductToVerifySortDatabase();

    mockMvc
        .perform(get(APIURL).param("pageSize", "20").param("sortBy", "price.asc")
            .headers(headerForAuthorizedUser).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.[0].price", is(56.89)))
        .andExpect(header().string("total-pages", "4"))
        .andExpect(header().string("page-number", "0"));
  }

  @Test
  public void getProducts_Pagination_SearchByPriceAsc_List_Ok() throws Exception {

    initializeAuthorizedUserHeader(jwtTokenUtility);
    initializeProductToVerifyPagination();
    initializeProductToVerifySortDatabase();

    mockMvc
        .perform(get(APIURL).param("pageSize", "20").param("searchString", "discounted")
            .headers(headerForAuthorizedUser).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.*", hasSize(1)))
        .andExpect(header().string("total-pages", "1"))
        .andExpect(header().string("page-number", "0"));
  }

  @Test
  void createProduct_Product_Ok() throws Exception {

    initializeAuthorizedUserHeader(jwtTokenUtility);

    mockMvc
        .perform(
            post(APIURL).headers(headerForAuthorizedUser).contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(productDTOV1)))
        .andExpect(status().isCreated()).andExpect(jsonPath("$.title", is(productDTOV1.getTitle())))
        .andExpect(jsonPath("$.brand", is(productDTOV1.getBrand())));
  }

  @Test
  void createProduct_Product_NotAllowed() throws Exception {

    initializeUnAuthorizedUserHeader(jwtTokenUtility);

    mockMvc
        .perform(
            post(APIURL).headers(headerForUnAuthorizedUser).contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(productDTOV1)))
        .andDo(print()).andExpect(status().isUnauthorized());
  }

  @Test
  void updateProduct_Product_Ok() throws Exception {

    ProductDTOV1 productDTO = dtoService.convertProductToProductDTO(product);
    productDTO.setTitle("New Title");

    initializeAuthorizedUserHeader(jwtTokenUtility);

    mockMvc
        .perform(put(APIURL.concat(String.valueOf(productDTO.getId())))
            .headers(headerForAuthorizedUser).contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(productDTO)))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id", is(productDTO.getId())))
        .andExpect(jsonPath("$.title", is(productDTO.getTitle())));
  }

  @Test
  void updateProduct_NotAllowed() throws Exception {

    ProductDTOV1 productDTO = dtoService.convertProductToProductDTO(product);
    productDTO.setTitle("New Title");

    initializeUnAuthorizedUserHeader(jwtTokenUtility);

    mockMvc
        .perform(put(APIURL.concat(String.valueOf(productDTO.getId())))
            .headers(headerForUnAuthorizedUser).contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(productDTO)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void deleteProduct_Ok() throws Exception {

    initializeAuthorizedUserHeader(jwtTokenUtility);

    mockMvc
        .perform(delete(APIURL.concat(String.valueOf(product.getId())))
            .headers(headerForAuthorizedUser).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteProduct_NotAllowed() throws Exception {

    initializeUnAuthorizedUserHeader(jwtTokenUtility);

    mockMvc
        .perform(delete(APIURL.concat(String.valueOf(product.getId())))
            .headers(headerForUnAuthorizedUser).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void createProductBatch_Product_Ok() throws Exception {

    initializeAuthorizedUserHeader(jwtTokenUtility);

    mockMvc
        .perform(post(APIURL.concat("batch")).headers(headerForAuthorizedUser)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(Collections.singletonList(productDTOV1))))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.[0].title", is(productDTOV1.getTitle())))
        .andExpect(jsonPath("$.[0].brand", is(productDTOV1.getBrand())));
  }
}
