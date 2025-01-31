package com.managment.budget_management_api.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User Budget sets restrictions and used Jakarta Validator to validate if certain fields are not blank
 */
@Entity
@Table(name = "Budgets")
@Getter
@Setter
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer budgetId;

    @ManyToOne
    @NotBlank(message = "user id is required")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Category name is required")
    @Column(nullable = false, length = 50)
    private String categoryName;

    @NotBlank(message = "Total budget is required")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalBudget;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Budget(){
        this.createdAt = LocalDateTime.now();
    }
}
