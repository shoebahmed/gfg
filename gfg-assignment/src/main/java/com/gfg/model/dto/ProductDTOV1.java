package com.gfg.model.dto;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDTOV1 {

  @ApiModelProperty(value = "Product id, a unique identifier of product.", required = true)
  private String id;

  @ApiModelProperty(value = "Product title.")
  private String title;

  @ApiModelProperty(value = "Description of product.")
  private String description;

  @ApiModelProperty(value = "Brand of product. Pass brand name in lower case with no spaces.",
      required = true)
  @NotEmpty
  private String brand;

  @ApiModelProperty(value = "Product price.", required = true)
  @NotEmpty
  private Double price;

  @ApiModelProperty(value = "Color of product. Pass color name in lower case with no spaces.",
      required = true)
  @NotEmpty
  private String color;
}
