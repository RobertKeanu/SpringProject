package org.store.springproject.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.store.springproject.exception.UserAlreadyExistsException;
import org.store.springproject.model.User;
import org.store.springproject.repository.UserRepository;
import org.store.springproject.dto.UserDto;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    public User createUser(String username, String password) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(username).isPresent()){
            throw new RuntimeException("Username already exists");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        final String encPassword = passwordEncoder.encode(password);
        User user = new User(username, encPassword);
        return userRepository.save(user);
    }
    public Optional<User>findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
