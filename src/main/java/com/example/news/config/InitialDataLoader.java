package com.example.news.config;

import com.example.news.model.Role;
import com.example.news.model.User;
import com.example.news.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class InitialDataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .name("admin")
                    .password(passwordEncoder.encode("password"))
                    .roles(Set.of(Role.ROLE_ADMIN))
                    .build();
            userRepository.save(admin);

            User testuser = User.builder()
                    .name("testuser")
                    .password(passwordEncoder.encode("password"))
                    .roles(Set.of(Role.ROLE_USER))
                    .build();
            userRepository.save(testuser);
        }
    }
}
