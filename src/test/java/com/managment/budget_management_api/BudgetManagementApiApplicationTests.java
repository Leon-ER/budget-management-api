package com.managment.budget_management_api;

import com.managment.budget_management_api.Model.Budget;
import com.managment.budget_management_api.Model.Transaction;
import com.managment.budget_management_api.Model.User;
import com.managment.budget_management_api.Repository.BudgetRepository;
import com.managment.budget_management_api.Repository.TransactionRepository;
import com.managment.budget_management_api.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BudgetManagementApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BudgetRepository budgetRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Test
	void testDataLoader() {
		// Verify a user was inserted
		User user = userRepository.findByUsername("john_doe");
		assertNotNull(user);

		// Verify a budget was inserted
		Budget budget = budgetRepository.findByCategoryName("Groceries");
		assertNotNull(budget);

		// Verify a transaction was inserted
		List<Transaction> transactions = transactionRepository.findByUser(user);
		assertFalse(transactions.isEmpty());
	}
}

