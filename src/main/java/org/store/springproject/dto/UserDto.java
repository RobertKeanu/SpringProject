package org.store.springproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotBlank(message = "Username-ul este obligatoriu.")
    private String username;

    @NotBlank(message = "Parola este obligatorie.")
    @Pattern(
            regexp = "^(?=.*[!@#$%^&*()_+\\-=`~{}\\[\\]:\";'<>?,./]).{8,}$",
            message = "Parola trebuie să aibă cel puțin 8 caractere și un caracter special."
    )
    private String password;
}
