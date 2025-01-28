package com.managment.budget_management_api.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Budgets")
@Getter
@Setter
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer budgetId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String categoryName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalBudget;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Budget(){
        this.createdAt = LocalDateTime.now();
    }
}
