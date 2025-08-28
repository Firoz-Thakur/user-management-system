package com.usermanagement.service;

import com.usermanagement.dto.CreateUserRequest;
import com.usermanagement.dto.UserDto;
import com.usermanagement.entity.User;
import com.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * UserService contains business logic for User operations
 * 
 * Service Layer responsibilities:
 * 1. Business logic and validation
 * 2. Transaction management
 * 3. Coordinate between different repositories
 * 4. Convert between DTOs and Entities
 * 5. Security operations (password encoding)
 * 
 * @Service - Marks this as a Spring service component
 * @Transactional - Provides transaction management
 */
@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Constructor injection (recommended over @Autowired field injection)
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Create a new user
     */
    public UserDto createUser(CreateUserRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        
        // Create new user entity
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole() != null ? request.getRole() : User.Role.USER);
        user.setStatus(User.UserStatus.ACTIVE);
        
        // Save user to database
        User savedUser = userRepository.save(user);
        
        // Convert to DTO and return
        return UserDto.fromEntity(savedUser);
    }
    
    /**
     * Get all users
     */
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Get users with pagination
     */
    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserDto::fromEntity);
    }
    
    /**
     * Get user by ID
     */
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserDto::fromEntity);
    }
    
    /**
     * Get user by username
     */
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserDto::fromEntity);
    }
    
    /**
     * Get user by email
     */
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserDto::fromEntity);
    }
    
    /**
     * Update an existing user
     */
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // Check if username is being changed and if it already exists
        if (!existingUser.getUsername().equals(userDto.getUsername()) && 
            userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Username already exists: " + userDto.getUsername());
        }
        
        // Check if email is being changed and if it already exists
        if (!existingUser.getEmail().equals(userDto.getEmail()) && 
            userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already exists: " + userDto.getEmail());
        }
        
        // Update fields
        existingUser.setUsername(userDto.getUsername());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setPhoneNumber(userDto.getPhoneNumber());
        
        if (userDto.getRole() != null) {
            existingUser.setRole(userDto.getRole());
        }
        
        if (userDto.getStatus() != null) {
            existingUser.setStatus(userDto.getStatus());
        }
        
        // Save updated user
        User updatedUser = userRepository.save(existingUser);
        
        return UserDto.fromEntity(updatedUser);
    }
    
    /**
     * Delete a user
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
    
    /**
     * Get users by role
     */
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role)
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Get users by status
     */
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByStatus(User.UserStatus status) {
        return userRepository.findByStatus(status)
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Search users by name
     */
    @Transactional(readOnly = true)
    public List<UserDto> searchUsersByName(String name) {
        return userRepository.findByFullNameContaining(name)
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Activate user
     */
    public UserDto activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setStatus(User.UserStatus.ACTIVE);
        User updatedUser = userRepository.save(user);
        
        return UserDto.fromEntity(updatedUser);
    }
    
    /**
     * Deactivate user
     */
    public UserDto deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setStatus(User.UserStatus.INACTIVE);
        User updatedUser = userRepository.save(user);
        
        return UserDto.fromEntity(updatedUser);
    }
    
    /**
     * Suspend user
     */
    public UserDto suspendUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setStatus(User.UserStatus.SUSPENDED);
        User updatedUser = userRepository.save(user);
        
        return UserDto.fromEntity(updatedUser);
    }
    
    /**
     * Update user role
     */
    public UserDto updateUserRole(Long id, User.Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setRole(role);
        User updatedUser = userRepository.save(user);
        
        return UserDto.fromEntity(updatedUser);
    }
    
    /**
     * Update last login time
     */
    public void updateLastLogin(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        }
    }
    
    /**
     * Get user statistics
     */
    @Transactional(readOnly = true)
    public UserStatistics getUserStatistics() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByStatus(User.UserStatus.ACTIVE);
        long inactiveUsers = userRepository.countByStatus(User.UserStatus.INACTIVE);
        long suspendedUsers = userRepository.countByStatus(User.UserStatus.SUSPENDED);
        long adminUsers = userRepository.countByRole(User.Role.ADMIN);
        long managerUsers = userRepository.countByRole(User.Role.MANAGER);
        long regularUsers = userRepository.countByRole(User.Role.USER);
        
        return new UserStatistics(totalUsers, activeUsers, inactiveUsers, suspendedUsers, 
                                adminUsers, managerUsers, regularUsers);
    }
    
    /**
     * Inner class for user statistics
     */
    public static class UserStatistics {
        private final long totalUsers;
        private final long activeUsers;
        private final long inactiveUsers;
        private final long suspendedUsers;
        private final long adminUsers;
        private final long managerUsers;
        private final long regularUsers;
        
        public UserStatistics(long totalUsers, long activeUsers, long inactiveUsers, long suspendedUsers,
                            long adminUsers, long managerUsers, long regularUsers) {
            this.totalUsers = totalUsers;
            this.activeUsers = activeUsers;
            this.inactiveUsers = inactiveUsers;
            this.suspendedUsers = suspendedUsers;
            this.adminUsers = adminUsers;
            this.managerUsers = managerUsers;
            this.regularUsers = regularUsers;
        }
        
        // Getters
        public long getTotalUsers() { return totalUsers; }
        public long getActiveUsers() { return activeUsers; }
        public long getInactiveUsers() { return inactiveUsers; }
        public long getSuspendedUsers() { return suspendedUsers; }
        public long getAdminUsers() { return adminUsers; }
        public long getManagerUsers() { return managerUsers; }
        public long getRegularUsers() { return regularUsers; }
        
        public double getActiveUserPercentage() {
            return totalUsers > 0 ? (double) activeUsers / totalUsers * 100 : 0;
        }
    }
}
