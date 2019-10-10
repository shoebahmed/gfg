package com.gfg.controller;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gfg.model.dto.ProductDTOV1;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = {"/api/product", "/api/v1/product"})
@Api(value = "productapi", description = "API Operations for Product.", tags = {"Product API"})
public interface ProductController {

  @GetMapping
  @ApiOperation(value = "Get all products, support for search and pagination.",
      response = ProductDTOV1.class)
  public ResponseEntity<List<ProductDTOV1>> getAllProduct(
      @ApiParam(name = "searchString", value = "Search string", defaultValue = "")
      @RequestParam(defaultValue = "") String searchString,
      @ApiParam(name = "pageNo", value = "Page number", defaultValue = "0")
      @RequestParam(defaultValue = "0") Integer pageNo,
      @ApiParam(name = "pageSize", value = "Page size", defaultValue = "50")
      @RequestParam(defaultValue = "50") Integer pageSize,
      @ApiParam(name = "sortBy", value = "Sort by", defaultValue = "price.asc")
      @RequestParam(defaultValue = "price.asc") String sortBy);

  @GetMapping("/{id}")
  @ApiOperation(value = "Get a single product by it's unique id.", response = ProductDTOV1.class)
  public ResponseEntity<ProductDTOV1> getProduct(@PathVariable("id") ObjectId id);

  @PostMapping
  @ApiOperation(value = "Create a product.", response = ProductDTOV1.class)
  public ResponseEntity<ProductDTOV1> createProduct(@RequestBody ProductDTOV1 productDTO);

  @PostMapping("/batch")
  @ApiOperation(
      value = "Create products, batch upload. This API can be used to create one or more product.",
      response = ProductDTOV1.class)
  public ResponseEntity<List<ProductDTOV1>> createProductBatch(
      @RequestBody List<ProductDTOV1> productDTOList);

  @PutMapping("/{id}")
  @ApiOperation(value = "Update a product.", response = ProductDTOV1.class)
  public ResponseEntity<ProductDTOV1> updateProduct(@PathVariable("id") ObjectId id,
      @RequestBody ProductDTOV1 productDTO);

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete a product by it's unique id.", response = ProductDTOV1.class)
  public ResponseEntity<Object> deleteProduct(@PathVariable("id") ObjectId id);
}
