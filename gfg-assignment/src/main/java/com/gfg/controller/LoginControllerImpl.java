package com.gfg.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.gfg.model.dto.UserDTOV1;
import com.gfg.security.util.JwtTokenUtility;
import com.gfg.service.DTOService;
import com.gfg.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoginControllerImpl implements LoginController {

  private final UserService userService;

  private final JwtTokenUtility jwtTokenUtility;

  private final DTOService dtoService;
  
  @Override
  public ResponseEntity<?> login(UserDTOV1 userDTO) {

    if (userService.isValid(userDTO.getUsername(), userDTO.getPassword())) {

      String jwtToken = jwtTokenUtility.generateToken(dtoService.convertUserDTOToUser(userDTO));

      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", "Bearer " + jwtToken);
      return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }
}
