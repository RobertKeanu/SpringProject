package org.store.springproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.store.springproject.exception.UserAlreadyExistsException;
import org.store.springproject.model.User;
import org.store.springproject.repository.UserRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String username, String password) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(username).isPresent()) {
            log.warn("Userul '{}' există deja în baza de date", username);
            throw new UserAlreadyExistsException("Username already exists");
        }

        String encPassword = passwordEncoder.encode(password);
        log.info("Creăm un utilizator nou cu username: {}", username);
        User user = new User(username, encPassword);
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Page<User> getUsers(int page, int size, String sortField, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortField).descending() :
                Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);
    }
}
