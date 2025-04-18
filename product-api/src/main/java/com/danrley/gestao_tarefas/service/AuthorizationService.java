package com.danrley.gestao_tarefas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.danrley.gestao_tarefas.model.user.User;
import com.danrley.gestao_tarefas.repository.UserRepository;
import com.danrley.gestao_tarefas.security.auth.UserDetailsImpl;

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
