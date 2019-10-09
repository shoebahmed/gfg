package com.gfg.model.domain;

import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "product")
@Getter
@Setter
@ToString
public class Product {

  @Id
  private ObjectId id;

  private String title;

  private String description;

  @NotNull
  private String brand;

  @NotNull
  private Double price;

  @NotNull
  private String color;
}
