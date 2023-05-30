package com.example.itemservice.Models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
    @Size(min = 2, max = 70, message = "Full name has to be between 2 and 70 characters")
    @Pattern(regexp = "^[a-zA-ZåÅäÄöÖ]+([ -][a-zA-ZåÅäÄöÖ]+)+$", message = "Full name must contain at least a first and last name, with optional middle names")
    private String fullName;
    @Column (unique = true)
    @NotBlank(message = "Must not be blank")
    @Size(min = 9, max = 13, message = "Ssn must be unique and between 9 and 13 characters")
    private String ssn;
    @Embedded
    @Valid
    @NotNull(message = "Address must not be null")
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



