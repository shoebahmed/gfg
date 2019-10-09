package com.gfg.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gfg.model.dto.UserDTOV1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = {"/api/login", "/api/v1/login"})
@Api(value = "loginapi", description = "API Operations for User Login.", tags = {"Login API"})
public interface LoginController {

  @PostMapping
  @ApiOperation(value = "Login API to validate user and generate token.",
      response = UserDTOV1.class)
  public ResponseEntity<?> login(UserDTOV1 userDTO);
}
