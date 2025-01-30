package com.managment.budget_management_api.Model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionSummary {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal totalBalance;

    public TransactionSummary(BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal totalBalance){
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.totalBalance = totalBalance;
    }
}
