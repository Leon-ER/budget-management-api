package com.managment.budget_management_api.Repository;

import com.managment.budget_management_api.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String johnDoe);
}
