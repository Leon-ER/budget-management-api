package com.managment.budget_management_api.Service;

import com.managment.budget_management_api.Model.User;

import java.util.Optional;

/**
 * Interface for User service implementation
 */
public interface IUserService {
    User save(User user);

    User update(Integer userId, User user);

    Optional<User> findById(Integer userId);

    void deleteById(Integer userId);

    User addAdminUser(User user);
}
