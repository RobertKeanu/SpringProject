package org.store.springproject.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    private String username;
    private List<Long> shoeIds;
    private Long paymentMethodId;
}
