package com.chidicivok.civokbank.entities;

import com.chidicivok.civokbank.enums.CustomerTier;
import com.chidicivok.civokbank.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "tier", nullable = false)
    private CustomerTier customerTier;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole userRole;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // tell jpa to perform this before running db record create
    @PrePersist
    public void prePersist() {
        // for default
        createdAt = LocalDateTime.now();

        // default customer sto standard
        if (customerTier == null) customerTier = CustomerTier.STANDARD;

        // default user role to customer
        if (userRole == null) userRole = UserRole.CUSTOMER;

    }

    // tell jpa to perform this before any update on this account
    @PreUpdate
    public void preUpdate() {
        // for updated_at
        updatedAt = LocalDateTime.now();
    }

}
