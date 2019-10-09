package com.gfg.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.gfg.model.domain.Product;
import com.gfg.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class InitializeTestData {

  private final ProductRepository productRepository;

  @Bean
  @Profile("stagingData")
  InitializingBean setStagingData() {
    return () -> {
      
      productRepository.deleteAll();

      Product productOne = new Product();
      productOne.setTitle("T-Shirt");
      productOne.setDescription("New Arrival");
      productOne.setBrand("lucas");
      productOne.setPrice(59.89);
      productOne.setColor("white");

      Product productTwo = new Product();
      productTwo.setTitle("Nike Runner From Staging");
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

      productRepository.save(productOne);
      productRepository.save(productTwo);
      productRepository.save(productThree);
    };
  }
}
