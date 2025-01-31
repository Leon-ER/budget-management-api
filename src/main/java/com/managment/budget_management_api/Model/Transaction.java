package com.managment.budget_management_api.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction model sets restrictions and used Jakarta Validator to validate if certain fields are not blank
 */
@Entity
@Table(name = "Transactions")
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    @ManyToOne
    @NotBlank(message = "userID is required")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @NotBlank(message = "budget id is required")
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    @NotBlank(message = "Transaction type is required")
    @Column(nullable = false)
    private String transactionType;

    @NotBlank(message = "Amount is required")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @NotBlank(message = "Description is required")
    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    public Transaction(){
        this.transactionDate = LocalDateTime.now();
    }

}
