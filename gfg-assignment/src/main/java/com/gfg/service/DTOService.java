package com.gfg.service;

import com.gfg.model.domain.Product;
import com.gfg.model.dto.ProductDTOV1;
import com.gfg.model.dto.UserDTOV1;
import com.gfg.security.domain.User;

public interface DTOService {

  User convertUserDTOToUser(UserDTOV1 userDTO);

  Product convertProductDTOToProduct(ProductDTOV1 productDTO);

  ProductDTOV1 convertProductToProductDTO(Product product);
}
