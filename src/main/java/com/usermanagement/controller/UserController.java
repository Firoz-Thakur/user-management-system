package com.usermanagement.controller;

import com.usermanagement.dto.CreateUserRequest;
import com.usermanagement.dto.UserDto;
import com.usermanagement.entity.User;
import com.usermanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * UserController handles HTTP requests for User operations
 * 
 * REST Controller Annotations:
 * @RestController - Combines @Controller + @ResponseBody (returns JSON by default)
 * @RequestMapping - Base URL path for all endpoints in this controller
 * @CrossOrigin - Enables CORS for frontend integration
 * @PreAuthorize - Spring Security authorization
 * 
 * HTTP Method Annotations:
 * @GetMapping - Handle GET requests
 * @PostMapping - Handle POST requests
 * @PutMapping - Handle PUT requests
 * @DeleteMapping - Handle DELETE requests
 * @PatchMapping - Handle PATCH requests
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Allow all origins for development
public class UserController {
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * GET /api/users - Get all users with pagination
     * Query Parameters: page, size, sort
     * Returns: Page of UserDto
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<UserDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }
    
    /**
     * GET /api/users/all - Get all users without pagination
     * Returns: List of UserDto
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsersNoPagination() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * GET /api/users/{id} - Get user by ID
     * Path Variable: id (Long)
     * Returns: UserDto or 404 Not Found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or (hasRole('USER') and #id == authentication.principal.id)")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<UserDto> user = userService.getUserById(id);
        
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * GET /api/users/username/{username} - Get user by username
     * Path Variable: username (String)
     * Returns: UserDto or 404 Not Found
     */
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        Optional<UserDto> user = userService.getUserByUsername(username);
        
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * GET /api/users/email/{email} - Get user by email
     * Path Variable: email (String)
     * Returns: UserDto or 404 Not Found
     */
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        Optional<UserDto> user = userService.getUserByEmail(email);
        
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * POST /api/users - Create a new user
     * Request Body: CreateUserRequest (JSON)
     * Returns: Created UserDto with HTTP 201 Created
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            UserDto createdUser = userService.createUser(request);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * PUT /api/users/{id} - Update an existing user
     * Path Variable: id (Long)
     * Request Body: UserDto (JSON)
     * Returns: Updated UserDto or 404 Not Found
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or (hasRole('USER') and #id == authentication.principal.id)")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, 
                                             @Valid @RequestBody UserDto userDto) {
        try {
            UserDto updatedUser = userService.updateUser(id, userDto);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * DELETE /api/users/{id} - Delete a user
     * Path Variable: id (Long)
     * Returns: HTTP 204 No Content or 404 Not Found
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * GET /api/users/role/{role} - Get users by role
     * Path Variable: role (String)
     * Returns: List of UserDto
     */
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable String role) {
        try {
            User.Role userRole = User.Role.valueOf(role.toUpperCase());
            List<UserDto> users = userService.getUsersByRole(userRole);
            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * GET /api/users/status/{status} - Get users by status
     * Path Variable: status (String)
     * Returns: List of UserDto
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<UserDto>> getUsersByStatus(@PathVariable String status) {
        try {
            User.UserStatus userStatus = User.UserStatus.valueOf(status.toUpperCase());
            List<UserDto> users = userService.getUsersByStatus(userStatus);
            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * GET /api/users/search?name={name} - Search users by name
     * Query Parameter: name (String)
     * Returns: List of UserDto
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String name) {
        List<UserDto> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }
    
    /**
     * PATCH /api/users/{id}/activate - Activate user
     * Path Variable: id (Long)
     * Returns: Updated UserDto or 404 Not Found
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<UserDto> activateUser(@PathVariable Long id) {
        try {
            UserDto activatedUser = userService.activateUser(id);
            return ResponseEntity.ok(activatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * PATCH /api/users/{id}/deactivate - Deactivate user
     * Path Variable: id (Long)
     * Returns: Updated UserDto or 404 Not Found
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<UserDto> deactivateUser(@PathVariable Long id) {
        try {
            UserDto deactivatedUser = userService.deactivateUser(id);
            return ResponseEntity.ok(deactivatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * PATCH /api/users/{id}/suspend - Suspend user
     * Path Variable: id (Long)
     * Returns: Updated UserDto or 404 Not Found
     */
    @PatchMapping("/{id}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> suspendUser(@PathVariable Long id) {
        try {
            UserDto suspendedUser = userService.suspendUser(id);
            return ResponseEntity.ok(suspendedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * PATCH /api/users/{id}/role/{role} - Update user role
     * Path Variables: id (Long), role (String)
     * Returns: Updated UserDto or 404 Not Found
     */
    @PatchMapping("/{id}/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUserRole(@PathVariable Long id, @PathVariable String role) {
        try {
            User.Role userRole = User.Role.valueOf(role.toUpperCase());
            UserDto updatedUser = userService.updateUserRole(id, userRole);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * GET /api/users/statistics - Get user statistics
     * Returns: UserStatistics object
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<UserService.UserStatistics> getUserStatistics() {
        UserService.UserStatistics stats = userService.getUserStatistics();
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Exception handler for validation errors
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse error = new ErrorResponse("Error: " + e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
    
    /**
     * Simple error response class
     */
    public static class ErrorResponse {
        private String message;
        private long timestamp;
        
        public ErrorResponse(String message) {
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }
        
        // Getters
        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }
        
        // Setters
        public void setMessage(String message) { this.message = message; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}
