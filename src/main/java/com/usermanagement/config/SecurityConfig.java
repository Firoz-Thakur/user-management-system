package com.usermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Configuration for the User Management System
 * 
 * This configuration:
 * 1. Disables CSRF for API usage
 * 2. Configures stateless session management
 * 3. Sets up authorization rules
 * 4. Enables method-level security
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    /**
     * Configure security filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for REST APIs
            .csrf(csrf -> csrf.disable())
            
            // Configure session management (stateless for APIs)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Configure authorization rules
            .authorizeHttpRequests(authz -> authz
                // Public endpoints (no authentication required)
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                
                // Admin-only endpoints
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/users").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/api/users/all").hasRole("ADMIN")
                .requestMatchers("/api/users/*/suspend").hasRole("ADMIN")
                .requestMatchers("/api/users/*/role/**").hasRole("ADMIN")
                
                // Manager and Admin endpoints
                .requestMatchers(HttpMethod.GET, "/api/users").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/api/users/statistics").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/api/users/role/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/api/users/status/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/api/users/search").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/api/users/*/activate").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/api/users/*/deactivate").hasAnyRole("ADMIN", "MANAGER")
                
                // User can access their own data
                .requestMatchers(HttpMethod.GET, "/api/users/*").hasAnyRole("ADMIN", "MANAGER", "USER")
                .requestMatchers(HttpMethod.PUT, "/api/users/*").hasAnyRole("ADMIN", "MANAGER", "USER")
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            
            // Configure HTTP Basic authentication (for testing)
            .httpBasic(httpBasic -> {});
        
        return http.build();
    }
    
    /**
     * Password encoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
