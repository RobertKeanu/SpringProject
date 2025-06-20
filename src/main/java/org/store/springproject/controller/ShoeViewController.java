package org.store.springproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.store.springproject.dto.ShoeDto;
import org.store.springproject.exception.ShoeAlreadyExistsException;
import org.store.springproject.exception.ShoeNotFoundException;
import org.store.springproject.service.ShoeService;

@Controller
@RequiredArgsConstructor
public class ShoeViewController {

    private final ShoeService shoeService;

    @GetMapping("/shoes")
    public String listShoes(Model model,
                            @ModelAttribute("successMessage") String successMessage,
                            @ModelAttribute("errorMessage") String errorMessage) {
        model.addAttribute("shoes", shoeService.findAllShoes());
        if (!successMessage.isEmpty()) model.addAttribute("successMessage", successMessage);
        if (!errorMessage.isEmpty()) model.addAttribute("errorMessage", errorMessage);
        return "shoes-list";
    }

    @GetMapping("/shoes/new")
    public String showCreateForm(Model model) {
        model.addAttribute("shoeDto", new ShoeDto());
        return "shoe-form";
    }

    @PostMapping("/shoes/create")
    public String createShoe(@Valid @ModelAttribute("shoeDto") ShoeDto shoeDto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            return "shoe-form";
        }

        try {
            shoeService.addShoe(
                    shoeDto.getName(),
                    shoeDto.getSize(),
                    shoeDto.getStock(),
                    shoeDto.getPrice()
            );
            redirectAttributes.addFlashAttribute("successMessage", "Pantof adăugat cu succes!");
            return "redirect:/shoes";
        } catch (ShoeAlreadyExistsException e) {
            model.addAttribute("errorMessage", "Pantoful există deja!");
            return "shoe-form";
        }
    }

    @GetMapping("/shoes/delete/{id}")
    public String deleteShoe(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        try {
            shoeService.deleteShoe(id);
            redirectAttributes.addFlashAttribute("successMessage", "Pantof șters cu succes.");
        } catch (ShoeNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Pantoful nu a fost găsit.");
        }
        return "redirect:/shoes";
    }
}
