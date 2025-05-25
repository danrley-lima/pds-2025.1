package com.danrley.product_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.danrley.product_management.model.user.User;
import com.danrley.product_management.repository.UserRepository;
import com.danrley.product_management.security.auth.UserDetailsImpl;

@Service
public class AuthorizationService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    return new UserDetailsImpl(user);

  }

}
