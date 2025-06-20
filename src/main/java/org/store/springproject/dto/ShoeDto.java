package org.store.springproject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShoeDto {
    @NotBlank(message = "Numele pantofului este obligatoriu.")
    private String name;

    @Min(value = 1, message = "Mărimea minimă este 1.")
    private int size;

    @Min(value = 0, message = "Stocul nu poate fi negativ.")
    private int stock;

    @NotNull(message = "Prețul este obligatoriu.")
    @Min(value = 0, message = "Prețul trebuie să fie pozitiv.")
    private Double price;
}