package com.caceis.petstore.dto;

import com.caceis.petstore.common.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderDTO {
    @NotNull
    private Long petId;
    
    @NotNull
    @Min(value = 1, message = "Minimum order quantity must be at least 1")
    private Integer quantity;

    private LocalDateTime shipDate;
    
    private OrderStatus status;
    
    private Boolean complete;
}