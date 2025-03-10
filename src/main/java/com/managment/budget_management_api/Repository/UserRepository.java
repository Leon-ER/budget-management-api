package com.managment.budget_management_api.Repository;

import com.managment.budget_management_api.Model.Transaction;
import com.managment.budget_management_api.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

}
