package com.gfg.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.gfg.security.domain.User;
import com.gfg.service.UserService;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

  private Map<String, String> userMap;

  @PostConstruct
  public void intialializeUser() {
    userMap = new HashMap<>();
    userMap.put("gfgadmin", "password");
  }

  public boolean isValid(String userName, String password) {
    return userMap.containsKey(userName) && userMap.containsValue(password);
  }

  public User loadUserByUsername(String userName) {
    return userMap.containsKey(userName) ? new User(userName, userMap.get(userName)) : null;
  }
}
