package com.gfg.repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.gfg.model.domain.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, ObjectId> {

  default Page<Product> findByTitleOrDescription(String searchString, Pageable pageable) {
    return findByTitleLikeOrDescriptionLikeIgnoreCase(searchString, searchString, pageable);
  }

  Page<Product> findByTitleLikeOrDescriptionLikeIgnoreCase(String title, String description,
      Pageable pageable);
}
