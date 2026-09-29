#  Online Banking Backend

A secure and scalable **Online Banking REST API** built using **Java, Spring Boot, Spring Security, JWT Authentication, Spring Data JPA, Hibernate, and MySQL**.

This backend provides RESTful APIs for user authentication, bank account management, deposits, withdrawals, money transfers, transaction history, user dashboard, and administrator operations.

---

##  Project Overview

The Online Banking Backend is a backend application designed to simulate core banking operations through secure REST APIs.

The application implements:

- User registration and login
- JWT-based authentication
- Role-based authorization
- Bank account creation and management
- Deposit and withdrawal operations
- Account-to-account money transfers
- Transaction history
- User dashboard
- Admin user management
- User blocking and activation
- Request validation
- Global exception handling
- Swagger/OpenAPI API documentation

---

##  Technologies Used

### Backend
- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- Hibernate
- REST APIs
- Jakarta Validation

### Database
- MySQL

### Tools
- IntelliJ IDEA
- Maven
- Git
- GitHub
- Postman
- Swagger UI
- SQLyog

---

## Security Features

The application uses Spring Security and JWT authentication to protect secured APIs.

### Authentication
- User login using email and password
- Password encryption using BCrypt
- JWT token generation after successful login
- JWT token validation for protected APIs
- JWT expiration handling

### Authorization
- Role-based authorization
- `ROLE_USER`
- `ROLE_ADMIN`
- Admin-only endpoints
- Users can access only their own bank accounts and transaction data

### Account Protection
- Blocked users cannot log in
- Existing JWT access is also blocked for blocked users
- Unauthorized users cannot access another user's account information

---

##  Key Features

###  User Management

- Register new users
- Login users
- Get user information
- Update user information
- Delete users
- Duplicate email validation

###  Bank Account Management

- Create bank accounts
- Generate unique account numbers
- View user's accounts
- View account details
- Check account balance

###  Banking Operations

- Deposit money
- Withdraw money
- Insufficient balance validation
- Positive amount validation

###  Money Transfer

- Transfer money between bank accounts
- Validate sender account ownership
- Validate receiver account
- Prevent transfer to the same account
- Check sufficient balance
- Record transfer transactions

###  Transaction Management

- View transaction history for an account
- View transactions across user's accounts
- Record deposits
- Record withdrawals
- Record sent transfers
- Record received transfers

###  Dashboard

The dashboard provides:

- User name
- Total number of accounts
- Total balance
- Recent transactions

###  Admin Management

Administrators can:

- View all users
- View all bank accounts
- View all transactions
- Block users
- Activate users

---

## 📁 Project Structure

```text
src
└── main
    ├── java
    │   └── com.onlinebanking
    │       ├── config
    │       │   ├── AdminDataInitializer.java
    │       │   ├── OpenApiConfig.java
    │       │   └── SecurityConfig.java
    │       │
    │       ├── controller
    │       │   ├── AdminController.java
    │       │   ├── AuthController.java
    │       │   ├── BankAccountController.java
    │       │   ├── DashboardController.java
    │       │   ├── GlobalExceptionHandler.java
    │       │   ├── TestController.java
    │       │   ├── TransactionController.java
    │       │   ├── TransferController.java
    │       │   └── UserController.java
    │       │
    │       ├── dto
    │       │   ├── AccountResponse.java
    │       │   ├── AdminAccountResponse.java
    │       │   ├── AdminTransactionResponse.java
    │       │   ├── AdminUserResponse.java
    │       │   ├── DashboardResponse.java
    │       │   ├── DepositRequest.java
    │       │   ├── LoginRequest.java
    │       │   ├── TransactionResponse.java
    │       │   ├── TransferRequest.java
    │       │   ├── UserResponse.java
    │       │   └── WithdrawRequest.java
    │       │
    │       ├── entity
    │       │   ├── BankAccount.java
    │       │   ├── Transaction.java
    │       │   └── User.java
    │       │
    │       ├── repository
    │       │   ├── BankAccountRepository.java
    │       │   ├── TransactionRepository.java
    │       │   └── UserRepository.java
    │       │
    │       ├── security
    │       │   └── JwtAuthenticationFilter.java
    │       │
    │       └── service
    │           ├── BankAccountService.java
    │           ├── DashboardService.java
    │           ├── JwtService.java
    │           ├── TransactionService.java
    │           ├── TransferService.java
    │           └── UserService.java
    │
    └── resources
        └── application.properties
