package com.example.itemservice.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Full name missing")
    private String fullName;
    @Column(unique = true)
    @NotBlank(message = "Must not be blank")
    @Size(min = 12, max = 12, message = "Ssn must be unique and exactly 12 characters, no special characters")
    private String ssn;
    @Embedded
    @NotBlank(message = "Must not be blank")
    private Address address;
    @Email(message = "E-mail must be set properly")
    private String email;

    public Customer(String fullName, String ssn, Address address, String email) {
        this.fullName = fullName;
        this.ssn = ssn;
        this.address = address;
        this.email = email;
    }

}



