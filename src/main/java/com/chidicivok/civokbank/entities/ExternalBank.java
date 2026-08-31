package com.chidicivok.civokbank.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "external_banks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "external_bank_id", nullable = false)
    private Long externalBankId;

    @Column(name = "bank_name", nullable = false, unique = true, length = 100)
    private String bankName;

    @Column(name = "bank_code", nullable = false, unique = true, length = 10)
    private String bankCode;
}