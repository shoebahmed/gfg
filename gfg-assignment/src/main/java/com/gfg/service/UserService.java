package com.gfg.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  public boolean isValid(String userName, String password);
}
