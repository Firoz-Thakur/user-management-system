package com.usermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Application Class for User Management System
 * 
 * @SpringBootApplication annotation includes:
 * - @Configuration: Indicates this is a configuration class
 * - @EnableAutoConfiguration: Enables Spring Boot's auto-configuration
 * - @ComponentScan: Enables component scanning for this package and sub-packages
 */
@SpringBootApplication
public class UserManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementSystemApplication.class, args);
        System.out.println("User Management System API is running!");
        System.out.println("Access the API at: http://localhost:8080");
        System.out.println("API Documentation: http://localhost:8080/api/users");
        System.out.println("Health Check: http://localhost:8080/actuator/health");
    }
}
