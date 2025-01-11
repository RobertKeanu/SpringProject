package org.store.springproject.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.store.springproject.dto.UserDto;
import org.store.springproject.exception.UserAlreadyExistsException;
import org.store.springproject.exception.UserNotFoundException;
import org.store.springproject.model.User;
import org.store.springproject.service.UserService;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    @PostMapping("/users")
    public ResponseEntity<Void> createAccount(@RequestBody UserDto userDto) {
        log.info("Creating username with username {} and password {}" , userDto.getUsername(), userDto.getPassword());
        try{
            userService.createUser(userDto.getUsername(), userDto.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        catch(UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @RequestMapping(value = "/{username}/get-user-by-name", method = RequestMethod.GET)
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        try{
            return ResponseEntity.ok(user.get());
        }catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
