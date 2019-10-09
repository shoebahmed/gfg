package com.gfg.test;

import org.bson.types.ObjectId;
import org.springframework.http.HttpHeaders;

import com.gfg.model.domain.Product;
import com.gfg.model.dto.ProductDTOV1;
import com.gfg.security.domain.User;
import com.gfg.security.util.JwtTokenUtility;

public class BaseTest {

  protected static final String APIURL = "/api/v1/product/";

  protected static final String LOGINAPIURL = "/api/v1/login/";

  protected Product productOne;

  protected ProductDTOV1 productDTOV1;

  protected HttpHeaders headerForAuthorizedUser;

  protected HttpHeaders headerForUnAuthorizedUser;

  public void initializeProduct() {

    productOne = new Product();
    productOne.setId(new ObjectId());
    productOne.setTitle("T-Shirt");
    productOne.setDescription("New Arrival");
    productOne.setBrand("lucas");
    productOne.setPrice(59.89);
    productOne.setColor("white");
  }

  public void initializeProductDTO() {

    productDTOV1 = new ProductDTOV1();
    productDTOV1.setId(productOne.getId().toString());
    productDTOV1.setTitle(productOne.getTitle());
    productDTOV1.setDescription(productOne.getDescription());
    productDTOV1.setBrand(productOne.getBrand());
    productDTOV1.setPrice(productOne.getPrice());
    productDTOV1.setColor(productOne.getColor());
  }

  public void initializeAuthorizedUserHeader(JwtTokenUtility jwtTokenUtility) {

    User authorizedUser = new User();
    authorizedUser.setUsername("gfgadmin");
    authorizedUser.setPassword("password");

    String jwtToken = jwtTokenUtility.generateToken(authorizedUser);

    headerForAuthorizedUser = new HttpHeaders();
    headerForAuthorizedUser.add("Authorization", "Bearer " + jwtToken);
  }

  public void initializeUnAuthorizedUserHeader(JwtTokenUtility jwtTokenUtility) {

    User unAuthorizedUser = new User();
    unAuthorizedUser.setUsername("anyuser");
    unAuthorizedUser.setPassword("DoesNotMatter");

    String jwtToken = jwtTokenUtility.generateToken(unAuthorizedUser);

    headerForUnAuthorizedUser = new HttpHeaders();
    headerForUnAuthorizedUser.add("Authorization", "Bearer " + jwtToken);
  }
}
