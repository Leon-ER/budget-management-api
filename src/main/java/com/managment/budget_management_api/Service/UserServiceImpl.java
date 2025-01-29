package com.managment.budget_management_api.Service;

import com.managment.budget_management_api.Exceptions.UserNotFoundException;
import com.managment.budget_management_api.Model.User;
import com.managment.budget_management_api.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        logger.info("Adding user with ID: {}", user.getUserId());
        try {
            user.setRole("USER");
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Error while saving user", e);
            throw new RuntimeException("Unable to save user", e);
        }
    }

    @Override
    public User update(Integer userId, User userDetails) {
        logger.info("Attempting to update user with ID: {}", userId);
        if (userDetails == null) {
            throw new IllegalArgumentException("User details cannot be null");
        }
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
        if (userDetails.getUsername() != null) {
            existingUser.setUsername(userDetails.getUsername());
        }
        if (userDetails.getEmail() != null) {
            existingUser.setEmail(userDetails.getEmail());
        }
        logger.info("User with ID: {} updated successfully", userId);
        return userRepository.save(existingUser);
    }

    @Override
    public Optional<User> findById(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    @Transactional
    public void deleteById(Integer userId) {
        logger.info("Attempting to delete user with ID: {}", userId);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
        userRepository.delete(existingUser);
        logger.info("User with ID: {} deleted successfully", userId);
    }

    @Override
    public User addAdminUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        logger.info("Adding user with ID: {}", user.getUserId());
        try {
            user.setRole("ADMIN");
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Error while saving user", e);
            throw new RuntimeException("Unable to save user", e);
        }
    }
}
