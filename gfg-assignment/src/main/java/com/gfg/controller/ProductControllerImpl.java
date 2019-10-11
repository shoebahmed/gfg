package com.gfg.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.gfg.model.domain.Product;
import com.gfg.model.dto.ProductDTOV1;
import com.gfg.repository.ProductRepository;
import com.gfg.repository.util.PageSortRequest;
import com.gfg.service.DTOService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {

  private final ProductRepository productRepository;

  private final DTOService dtoService;
  
  @Override
  public ResponseEntity<List<ProductDTOV1>> getAllProduct(String searchString, Integer pageNo,
      Integer pageSize, String sortBy) {

    Page<Product> productPage = productRepository.findByTitleOrDescription(searchString,
        PageSortRequest.of(pageNo, pageSize, sortBy));

    HttpHeaders headers = new HttpHeaders();
    headers.add("total-pages", String.valueOf(productPage.getTotalPages()));
    headers.add("page-number", String.valueOf(productPage.getNumber()));

    return new ResponseEntity<>(productPage.getContent().stream()
        .map(dtoService::convertProductToProductDTO).collect(Collectors.toList()), headers,
        HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ProductDTOV1> getProduct(ObjectId id) {

    return productRepository.findById(id).map(product -> {
      return new ResponseEntity<>(dtoService.convertProductToProductDTO(product), HttpStatus.OK);
    }).orElseGet(() -> {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    });
  }

  @Override
  public ResponseEntity<ProductDTOV1> createProduct(ProductDTOV1 productDTO) {

    Product product = productRepository.save(dtoService.convertProductDTOToProduct(productDTO));
    return new ResponseEntity<>(dtoService.convertProductToProductDTO(product), HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<List<ProductDTOV1>> createProductBatch(List<ProductDTOV1> productDTOList) {

    List<Product> productList = productDTOList.stream().map(dtoService::convertProductDTOToProduct)
        .collect(Collectors.toList());

    List<ProductDTOV1> productDTOV1List = new ArrayList<>();

    productRepository.saveAll(productList).forEach(product -> {
      productDTOV1List.add(dtoService.convertProductToProductDTO(product));
    });
    return new ResponseEntity<>(productDTOV1List, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<ProductDTOV1> updateProduct(ObjectId id, ProductDTOV1 productDTO) {
    return productRepository.findById(id).map(product -> {

      Product productToUpdate = dtoService.convertProductDTOToProduct(productDTO);
      BeanUtils.copyProperties(productToUpdate, product, "id");

      return new ResponseEntity<>(
          dtoService.convertProductToProductDTO(productRepository.save(product)), HttpStatus.OK);
    }).orElseGet(() -> {
      return createProduct(productDTO);
    });
  }

  @Override
  public ResponseEntity<Object> deleteProduct(ObjectId id) {
    
    return productRepository.findById(id).map(product -> {
      productRepository.delete(product);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }).orElseGet(() -> {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    });
  }
}
