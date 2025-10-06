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
    @Min(1)
    private Integer quantity;
    
    @NotNull
    private LocalDateTime shipDate;
    
    @NotNull
    private OrderStatus status;
    
    @NotNull
    private Boolean complete;
}