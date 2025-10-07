package com.caceis.petstore.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pet_id", nullable = false, unique = true)
    private Pet pet;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "reserved", nullable = false)
    private Integer reserved;

    public Integer getAvailableQuantity() {
        return quantity - reserved;
    }

    public boolean hasAvailableStock(Integer requestedQuantity) {
        return getAvailableQuantity() >= requestedQuantity;
    }

    public void reserveStock(Integer amount) {
        if (!hasAvailableStock(amount)) {
            throw new IllegalStateException("Insufficient stock available");
        }
        this.reserved += amount;
    }

    public void releaseReservedStock(Integer amount) {
        if (this.reserved < amount) {
            throw new IllegalStateException("Cannot release more than reserved");
        }
        this.reserved -= amount;
    }

    public void confirmReservedStock(Integer amount) {
        if (this.reserved < amount) {
            throw new IllegalStateException("Cannot confirm more than reserved");
        }
        this.reserved -= amount;
        this.quantity -= amount;
    }

    public void addStock(Integer amount) {
        this.quantity += amount;
    }
}
