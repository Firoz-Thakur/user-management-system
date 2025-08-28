package com.usermanagement.repository;

import com.usermanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * UserRepository interface for database operations
 * 
 * Spring Data JPA automatically provides implementations for:
 * - save(entity) - Create/Update
 * - findById(id) - Read by ID
 * - findAll() - Read all
 * - deleteById(id) - Delete by ID
 * - count() - Count records
 * - existsById(id) - Check if exists
 * 
 * We can also define custom query methods using method naming conventions
 * or @Query annotations
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Query methods using Spring Data JPA naming conventions
    
    /**
     * Find user by username
     * Spring automatically generates: SELECT * FROM users WHERE username = ?
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find users by role
     */
    List<User> findByRole(User.Role role);
    
    /**
     * Find users by status
     */
    List<User> findByStatus(User.UserStatus status);
    
    /**
     * Find users by first name (case-insensitive)
     */
    List<User> findByFirstNameContainingIgnoreCase(String firstName);
    
    /**
     * Find users by last name (case-insensitive)
     */
    List<User> findByLastNameContainingIgnoreCase(String lastName);
    
    /**
     * Find users by first name or last name containing search term
     */
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    
    /**
     * Find users by role and status
     */
    List<User> findByRoleAndStatus(User.Role role, User.UserStatus status);
    
    /**
     * Find users created after a specific date
     */
    List<User> findByCreatedAtAfter(LocalDateTime date);
    
    /**
     * Find users created between two dates
     */
    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Count users by role
     */
    long countByRole(User.Role role);
    
    /**
     * Count users by status
     */
    long countByStatus(User.UserStatus status);
    
    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Custom query using JPQL (Java Persistence Query Language)
     * Find active users with a specific role
     */
    @Query("SELECT u FROM User u WHERE u.status = :status AND u.role = :role")
    List<User> findActiveUsersByRole(@Param("status") User.UserStatus status, @Param("role") User.Role role);
    
    /**
     * Custom query to find users by full name search
     */
    @Query("SELECT u FROM User u WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByFullNameContaining(@Param("name") String name);
    
    /**
     * Custom query to find users who haven't logged in for a specific period
     */
    @Query("SELECT u FROM User u WHERE u.lastLogin < :date OR u.lastLogin IS NULL")
    List<User> findUsersNotLoggedInSince(@Param("date") LocalDateTime date);
    
    /**
     * Custom native SQL query to find users with statistics
     */
    @Query(value = "SELECT * FROM users WHERE created_at >= :sinceDate ORDER BY created_at DESC", nativeQuery = true)
    List<User> findRecentUsers(@Param("sinceDate") LocalDateTime sinceDate);
    
    /**
     * Custom query to get user count by role
     */
    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
    List<Object[]> getUserCountByRole();
    
    /**
     * Custom query to get user count by status
     */
    @Query("SELECT u.status, COUNT(u) FROM User u GROUP BY u.status")
    List<Object[]> getUserCountByStatus();
}
