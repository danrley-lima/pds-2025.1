package com.danrley.gestao_tarefas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@Profile({ "dev", "test" })
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

  @Value("${app.database-seeder-enabled:false}")
  private boolean isSeederEnabled;

  // private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    if (!isSeederEnabled) {
      return;
    }
  }
}
