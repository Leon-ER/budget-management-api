package com.managment.budget_management_api.Controller;

import com.managment.budget_management_api.Exceptions.UserNotFoundException;
import com.managment.budget_management_api.Model.User;
import com.managment.budget_management_api.Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Integer userId, @RequestBody User user){
        return ResponseEntity.ok(userService.update(userId,user));
    }
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Integer userId){
        return ResponseEntity.ok(userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id, %s not found", userId))));
    }
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId){
        userService.deleteById(userId);
        return ResponseEntity.ok(String.format("User with ID: %s deleted successfully", userId));
    }

    @PostMapping("/saveAdmin")
    public ResponseEntity<User> saveAdmin(@RequestBody User user){
        return ResponseEntity.ok(userService.addAdminUser(user));
    }
}
