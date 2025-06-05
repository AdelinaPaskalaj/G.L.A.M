
# Law Office Management System

A desktop-based JavaFX application developed to manage law office operations with role-based access for **Admin**, **Lawyer**, and **Client**. The system provides modules for legal case management, appointment scheduling, invoicing, and secure user authentication.

---

## Overview

This system is structured to provide clear separation of access and responsibility across three main user roles:

###  Client
- Self-register and log in
- Register new legal cases
- View active and historical cases
- Track appointments with assigned lawyers
- View and update personal profile

### Lawyer
- View cases assigned by admin
- Update case statuses and descriptions
- Track hearing appointments by day/time
- Manage personal profile

###  Admin
- Manage all users (lawyers and clients)
- Assign lawyers to pending client cases
- Create and manage invoices
- View and manage appointments
- Broadcast system-wide notifications
- Generate reports for users, cases, invoices, and appointments

---

## Project Structure

```
lawoffice/
├── controller/          # JavaFX controllers for all views
├── dao/                 # Database access objects
├── model/               # JavaFX models using Property bindings
├── service/             # Business logic layer
├── session/             # Session handling for logged-in users
├── util/                # Database utility and common helpers
├── view/                # FXML view files
├── css/                 # Application-wide styles
└── Main.java            # Entry point of the application
```

---

## Database Setup (MySQL)

1. Run the provided SQL script in MySQL to create the schema and seed initial data.

```sql
DROP DATABASE IF EXISTS law_office_db;
CREATE DATABASE law_office_db;
USE law_office_db;

-- Tables: users, admins, lawyers, clients, cases, appointments, invoices, etc.
-- Triggers and foreign keys included
-- (Full script is included in `law_office_db.sql`)
```

2. Update your credentials in `DBUtil.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/law_office_db";
private static final String USER = "root";
private static final String PASSWORD = "root";
```

---

## Initial Login Accounts

### Admin Account
| Role  | Name        | Email               | Password |
|-------|-------------|---------------------|----------|
| Admin | Lida Filipi | lidafilipi@gmail.com| abc      |

### Lawyer Accounts
| Name             | Email                     | Password |
|------------------|---------------------------|----------|
| Mikaela Bala     | mikaelabala@gmail.com     | 123      |
| Adelina Paskalaj | adelinapaskalaj@gmail.com | 123      |
| Gledi Tace       | gleditace@gmail.com       | 123      |

### Client Accounts
| Name  | Email             | Password |
|-------|-------------------|----------|
| Noel  | noel@gmail.com    | 111      |
| Reina | reina@gmail.com   | 111      |
| Dani  | dani@gmail.com    | 111      |
| Nela  | nela@gmail.com    | 111      |
| Flavio| flavio@gmail.com  | 111      |

> Clients can also self-register using the registration form.

---

## How to Run

1. Ensure **Java 17+** and **JavaFX 21+** are configured.
2. Set up the MySQL database using the provided script.
3. Open the project in an IDE (e.g., IntelliJ or Eclipse).
4. Launch `Main.java` to start the application.

---

##  Developed By

- Lida Filipi  
- Adelina Paskalaj  
- Mikaela Bala  
- Gledi Taçe  

University Project – 2025  

