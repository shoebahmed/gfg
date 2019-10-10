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
  
  /**
   * API exposed from this URI can be used to get products from GFG μS.
   * It supports Pagination and Sorting.
   * 
   * @param searchString is used to provide case insensitive search. Title and
   *        description fields support free text search.
   * @param pageNo is used to pass required page number base on search / fetch 
   *        criteria. pageNo uses zero based index.
   * @param pageSize denotes number of product to be returned in one page.
   * @param sortBy is used to provide sorting instruction.
   * @return List<ProductDTOV1>, returns list of Product.
   */
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
  
  /**
   * Read Product API, used to get Product by its id. Id is passed is simple string,
   * which is converted to ObjectId by spring framework.
   * 
   * @param id - Unique Id of product.
   * @return Product identified by id.
   */
  @GetMapping("/{id}")
  @ApiOperation(value = "Get a single product by it's unique id.", response = ProductDTOV1.class)
  public ResponseEntity<ProductDTOV1> getProduct(@PathVariable("id") ObjectId id);
  
  /**
   * Create Product API, used to create Product in GFG μS.
   * 
   * @param productDTO all fields are passed in ProductDTOV1
   * @return Its creates the product and returns back the product.
   */
  @PostMapping
  @ApiOperation(value = "Create a product.", response = ProductDTOV1.class)
  public ResponseEntity<ProductDTOV1> createProduct(@RequestBody ProductDTOV1 productDTO);
  
  /**
   * Create Product batch. This API can be used to create one or more than one Product. It returns
   * recently created product.
   * 
   * @param productDTOList
   * @return List<ProductDTO1>, returns list of created Product.
   */
  @PostMapping("/batch")
  @ApiOperation(
      value = "Create products, batch upload. This API can be used to create one or more product.",
      response = ProductDTOV1.class)
  public ResponseEntity<List<ProductDTOV1>> createProductBatch(
      @RequestBody List<ProductDTOV1> productDTOList);
  
  /**
   * API to update product details. This API will update product is exists or will create one 
   * if Product does not exist. It returns status code to identify whether Product is updated
   * or created.
   * 
   * @param id - Unique id of product.
   * @param productDTO - Product to be updated.
   * @return Updated product.
   */
  @PutMapping("/{id}")
  @ApiOperation(value = "Update a product.", response = ProductDTOV1.class)
  public ResponseEntity<ProductDTOV1> updateProduct(@PathVariable("id") ObjectId id,
      @RequestBody ProductDTOV1 productDTO);
  
  /**
   * API to delete Product from database. Product is deleted only when product is present.
   * If product is not present, than not found http status is sent back.
   * @param id
   * @return
   */
  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete a product by it's unique id.", response = ProductDTOV1.class)
  public ResponseEntity<Object> deleteProduct(@PathVariable("id") ObjectId id);
}
