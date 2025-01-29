package com.managment.budget_management_api.Controller;

import com.managment.budget_management_api.Exceptions.UserNotFoundException;
import com.managment.budget_management_api.Model.User;
import com.managment.budget_management_api.Service.IUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    @Autowired
    private IUserService userService;

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@Min(1) @PathVariable Integer userId, @Valid @RequestBody User user) {
        try {
            User updatedUser = userService.update(userId, user);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating a user");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@Min(1) @PathVariable Integer userId) {
        return ResponseEntity.ok(userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id, %s not found", userId))));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@Min(1) @PathVariable Integer userId) {
        try {
            userService.deleteById(userId);
            return ResponseEntity.ok(String.format("User with ID: %s deleted successfully", userId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting a user");
        }
    }

    @PostMapping("/saveAdmin")
    public ResponseEntity<String> saveAdmin(@Min(1) @RequestBody User user) {
        try {
            userService.addAdminUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Admin added successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while adding the admin user.");
        }
    }
}
