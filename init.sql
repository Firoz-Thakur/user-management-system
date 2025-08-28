-- Database initialization script for User Management System
-- This script will be executed when PostgreSQL container starts for the first time

-- Create extensions (if needed)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- The tables will be created automatically by Hibernate JPA
-- This script is mainly for any initial data or custom setup

-- Insert initial admin user (password: admin123 - encoded with BCrypt)
-- Note: You should run the application first to create tables, then add initial data
-- Uncomment these after running the application once:

-- INSERT INTO users (username, email, password, first_name, last_name, role, status, created_at, updated_at) 
-- VALUES ('admin', 'admin@usermanagement.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'System', 'Admin', 'ADMIN', 'ACTIVE', NOW(), NOW());

-- INSERT INTO users (username, email, password, first_name, last_name, role, status, created_at, updated_at) 
-- VALUES ('manager', 'manager@usermanagement.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Test', 'Manager', 'MANAGER', 'ACTIVE', NOW(), NOW());

-- INSERT INTO users (username, email, password, first_name, last_name, role, status, created_at, updated_at) 
-- VALUES ('user', 'user@usermanagement.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Test', 'User', 'USER', 'ACTIVE', NOW(), NOW());

-- Create indexes for better performance (optional)
-- These will be created after Hibernate creates the tables
-- You can uncomment these after running the application once

-- CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
-- CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
-- CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);
-- CREATE INDEX IF NOT EXISTS idx_users_status ON users(status);
-- CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);
