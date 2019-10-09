package com.gfg.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTOV1 {

  @ApiModelProperty(value = "User name to login, to run this application please use 'gfgadmin'.",
      required = true, position = 1)
  private String username;

  @ApiModelProperty(value = "Password for user, to run this application please use 'password'",
      required = true, position = 2)
  private String password;
}
