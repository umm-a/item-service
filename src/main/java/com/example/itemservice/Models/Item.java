package com.example.itemservice.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "Must not be blank")
    @Size(min = 1 , max = 100, message = "Item name must be between 1 and 100 characters long")
    private String name;
    @Min(value = 1, message = "Item is not supposed to be given free!")
    private int price;
    private int stock;

    public Item(String name, int price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}
