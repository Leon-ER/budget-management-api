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
import org.springframework.context.MessageSource;

import java.util.Locale;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Updates an existing user based on the provided user ID and request body.
     *
     * @param userId The ID of the user to be updated (must be greater than 0).
     * @param user The updated user details.
     * @param locale The locale for internationalized messages.
     * @return A response indicating success or failure of the update operation.
     */
    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(
            @Min(1) @PathVariable Integer userId,
            @Valid @RequestBody User user,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            userService.update(userId, user);
            return ResponseEntity.ok(
                    messageSource.getMessage("response.user.updated", new Object[]{userId}, locale)
            );
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    messageSource.getMessage("error.user.notfound", new Object[]{userId}, locale)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("error.user.serverUpdating", null, locale));
        }
    }
    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to retrieve (must be greater than 0).
     * @param locale The locale for internationalized messages.
     * @return The user details if found, or an error message if not.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(
            @Min(1) @PathVariable Integer userId,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            return ResponseEntity.ok(userService.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("error.user.notfound", new Object[]{userId}, locale));
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to delete (must be greater than 0).
     * @param locale The locale for internationalized messages.
     * @return A response indicating success or failure of the delete operation.
     */

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(
            @Min(1) @PathVariable Integer userId,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            userService.deleteById(userId);
            return ResponseEntity.ok(
                    messageSource.getMessage("response.user.deleted", new Object[]{userId}, locale)
            );
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    messageSource.getMessage("error.user.notfound", new Object[]{userId}, locale)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("error.user.serverDeleting", null, locale));
        }
    }

    /**
     * Creates a new admin user.
     *
     * @param user The user details to be added.
     * @param locale The locale for internationalized messages.
     * @return A response indicating success or failure of the creation operation.
     */
    @PostMapping("/saveAdmin")
    public ResponseEntity<String> saveAdmin(
            @RequestBody @Valid User user,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            userService.addAdminUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    messageSource.getMessage("response.user.created", null, locale)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("error.user.serverAdding", null, locale));
        }
    }
}
