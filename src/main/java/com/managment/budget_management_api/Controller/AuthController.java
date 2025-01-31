package com.managment.budget_management_api.Controller;

import com.managment.budget_management_api.Model.User;
import com.managment.budget_management_api.Service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.MessageSource;

import java.util.Locale;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private IUserService userService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Registers a new user in the system.
     *
     * @param user   The user details to be saved.
     * @param locale The locale for internationalized messages.
     * @return A response indicating success or failure of the registration operation.
     */
    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(
            @Valid @RequestBody User user,
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        try {
            userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(messageSource.getMessage("response.user.created", null, locale));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("error.user.serverAdding", null, locale));
        }
    }
}
