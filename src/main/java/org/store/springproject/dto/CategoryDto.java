package org.store.springproject.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private String name;
    private List<ShoeDto> shoes;
}