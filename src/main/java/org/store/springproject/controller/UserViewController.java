package org.store.springproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.store.springproject.dto.UserDto;
import org.store.springproject.exception.UserAlreadyExistsException;
import org.store.springproject.model.User;
import org.store.springproject.service.UserService;

import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserViewController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register-user")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "user-form";
    }

    @PostMapping("/register-user")
    public String handleRegistration(
            @Valid @ModelAttribute("userDto") UserDto userDto,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("userDto", userDto);
            return "user-form";
        }

        try {
            userService.createUser(userDto.getUsername(), userDto.getPassword());
            model.addAttribute("successMessage", "Cont creat cu succes!");
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("errorMessage", "Utilizatorul există deja!");
        }

        return "user-form";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("userDto") UserDto userDto, Model model) {
        Optional<User> userOpt = userService.findByUsername(userDto.getUsername());

        if (userOpt.isPresent()) {
            log.info("Cerere de login pentru utilizator: {}", userDto.getUsername());
            User user = userOpt.get();
            if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
                model.addAttribute("successMessage", "Bine ai venit, " + user.getUsername() + "!");
                return "welcome";
            }
        }

        model.addAttribute("errorMessage", "Username sau parolă incorectă.");
        return "login";
    }

    @GetMapping("/welcome")
    public String showWelcomePage() {
        return "welcome";
    }

    @GetMapping("/users-list")
    public String listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "username") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model
    ) {
        Page<User> userPage = userService.getUsers(page, size, sort, dir);

        model.addAttribute("userPage", userPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("reverseDir", dir.equals("asc") ? "desc" : "asc");

        return "user-list";
    }

}
