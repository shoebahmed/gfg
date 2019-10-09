package com.gfg.test.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfg.controller.LoginControllerImpl;
import com.gfg.model.dto.UserDTOV1;
import com.gfg.security.domain.User;
import com.gfg.security.util.JwtTokenUtility;
import com.gfg.service.DTOService;
import com.gfg.service.UserService;
import com.gfg.test.BaseTest;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LoginControllerTest extends BaseTest {

  @InjectMocks
  private LoginControllerImpl loginController;

  @Mock
  private UserService userService;

  @Mock
  private JwtTokenUtility jwtTokenUtility;

  @Mock
  private DTOService dtoService;

  private MockMvc mvc;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    initializeMVC();

    this.objectMapper = new ObjectMapper();
  }

  private void initializeMVC() {
    mvc = standaloneSetup(loginController).build();
  }

  @Test
  void login_UserPresent_Ok() throws Exception {

    UserDTOV1 userDTOV1 = new UserDTOV1();
    userDTOV1.setUsername("gfgadmin");
    userDTOV1.setPassword("password");

    Mockito.when(userService.isValid(any(), any())).thenReturn(true);
    Mockito.when(dtoService.convertUserDTOToUser(any())).thenReturn(new User());
    Mockito.when(jwtTokenUtility.generateToken(any())).thenReturn("newToken");

    MockHttpServletResponse response = mvc.perform(post(LOGINAPIURL)
        .accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTOV1))
        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  void login_UserNotPresent_UnAuthorized() throws Exception {

    UserDTOV1 userDTOV1 = new UserDTOV1();
    userDTOV1.setUsername("someuser");
    userDTOV1.setPassword("doesnotMatter");

    Mockito.when(userService.isValid(any(), any())).thenReturn(false);

    MockHttpServletResponse response = mvc.perform(post(LOGINAPIURL)
        .accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDTOV1))
        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
  }
}
