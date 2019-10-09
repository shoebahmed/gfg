package com.gfg.service.impl;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.gfg.model.converter.CustomObjectIdToStringConverter;
import com.gfg.model.domain.Product;
import com.gfg.model.dto.ProductDTOV1;
import com.gfg.model.dto.UserDTOV1;
import com.gfg.security.domain.User;
import com.gfg.service.DTOService;

@Service(value = "dtoService")
public class DTOServiceImpl extends ModelMapper implements DTOService {

  @PostConstruct
  void init() {
    addConverter(new CustomObjectIdToStringConverter());
  }

  @Override
  public User convertUserDTOToUser(UserDTOV1 userDTO) {
    return map(userDTO, User.class);
  }

  @Override
  public Product convertProductDTOToProduct(ProductDTOV1 productDTO) {
    return map(productDTO, Product.class);
  }

  @Override
  public ProductDTOV1 convertProductToProductDTO(Product product) {
    return map(product, ProductDTOV1.class);
  }
}
