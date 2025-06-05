DROP DATABASE IF EXISTS law_office_db;
CREATE DATABASE law_office_db;
USE law_office_db;

-- USERS
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    personal_id VARCHAR(16) UNIQUE NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'Lawyer', 'Client') NOT NULL,
    status VARCHAR(20) DEFAULT 'Active'
);

-- ADMINS
CREATE TABLE admins (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(100) DEFAULT 'System Admin',
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- LAWYERS
CREATE TABLE lawyers (
    lawyer_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    specialization VARCHAR(100),
    title VARCHAR(100) DEFAULT 'Lawyer',
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- CLIENTS
CREATE TABLE clients (
    client_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    case_history TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- CASES
CREATE TABLE cases (
    case_id INT PRIMARY KEY AUTO_INCREMENT,
    client_id INT NOT NULL,
    lawyer_id INT DEFAULT NULL, 
    case_type VARCHAR(100),
    start_date DATE,
    appointment_time TIME, 
    status VARCHAR(50),
    details TEXT,
    estimated_fee DECIMAL(10,2) DEFAULT 0.00,
    is_self_registered BOOLEAN DEFAULT FALSE,
    request_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES clients(client_id),
    FOREIGN KEY (lawyer_id) REFERENCES lawyers(lawyer_id)
);

-- ACTIVITIES
CREATE TABLE activities (
    activity_id INT PRIMARY KEY AUTO_INCREMENT,
    client_id INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    activity_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES clients(client_id)
);

-- CASE HISTORY
CREATE TABLE case_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    case_id INT,
    update_note TEXT,
    update_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (case_id) REFERENCES cases(case_id)
);

-- CASE DOCUMENTS
CREATE TABLE case_documents (
    document_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    case_id INT NOT NULL,
    document_name VARCHAR(255),
    document_type VARCHAR(20),
    document_data LONGBLOB,
    uploaded_date DATE,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (case_id) REFERENCES cases(case_id)
);

-- HEARINGS
CREATE TABLE hearings (
    hearing_id INT PRIMARY KEY AUTO_INCREMENT,
    case_id INT NOT NULL,
    lawyer_id INT NOT NULL,
    hearing_datetime DATETIME,
    status ENUM('assigned', 'pending', 'resolved') DEFAULT 'pending',
    notes TEXT,
    FOREIGN KEY (case_id) REFERENCES cases(case_id),
    FOREIGN KEY (lawyer_id) REFERENCES lawyers(lawyer_id)
);

-- LAWYER PERFORMANCE
CREATE TABLE lawyer_performance (
    performance_id INT PRIMARY KEY AUTO_INCREMENT,
    lawyer_id INT NOT NULL,
    cases_won INT DEFAULT 0,
    hearings_resolved INT DEFAULT 0,
    FOREIGN KEY (lawyer_id) REFERENCES lawyers(lawyer_id)
);

-- APPOINTMENTS
CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    lawyer_id INT NOT NULL,
    appointment_date DATETIME NOT NULL,
    location VARCHAR(255),
    case_id INT,
    notes TEXT,
    status VARCHAR(50) DEFAULT 'pending',
    FOREIGN KEY (client_id) REFERENCES clients(client_id),
    FOREIGN KEY (lawyer_id) REFERENCES lawyers(lawyer_id),
    FOREIGN KEY (case_id) REFERENCES cases(case_id)
);

-- INVOICES
CREATE TABLE invoices (
    invoice_id INT PRIMARY KEY AUTO_INCREMENT,
    client_id INT NOT NULL,
    case_id INT NOT NULL,
    amount DECIMAL(10,2),
    due_date DATE,
    status ENUM('paid', 'pending', 'overdue') DEFAULT 'pending',
    issued_date DATE,
    FOREIGN KEY (client_id) REFERENCES clients(client_id),
    FOREIGN KEY (case_id) REFERENCES cases(case_id)
);

-- CLIENT REQUESTS
CREATE TABLE client_requests (
    request_id INT PRIMARY KEY AUTO_INCREMENT,
    client_id INT NOT NULL,
    case_type VARCHAR(50) NOT NULL,
    preferred_date DATETIME NOT NULL,
    case_details TEXT,
    estimated_fee DECIMAL(10,2) NOT NULL,
    status ENUM('Pending', 'Approved', 'Rejected') DEFAULT 'Pending',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES clients(client_id)
);

-- LAWYER AVAILABILITY
CREATE TABLE lawyer_availability (
    availability_id INT PRIMARY KEY AUTO_INCREMENT,
    lawyer_id INT NOT NULL,
    day_of_week INT NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (lawyer_id) REFERENCES lawyers(lawyer_id)
);

-- TRIGGER
DELIMITER //
CREATE TRIGGER after_user_insert_client
AFTER INSERT ON users
FOR EACH ROW
BEGIN
    IF NEW.role = 'Client' THEN
        INSERT INTO clients (user_id) VALUES (NEW.user_id);
    END IF;
END;
//
DELIMITER ;

-- INSERT INITIAL USERS
INSERT INTO users (personal_id, full_name, email, phone, address, password, role) VALUES
('12345-1234567-1', 'Lida Filipi', 'lidafilipi@gmail.com', '0681234567', 'Tirane', 'abc', 'Admin'),
('23456-2345678-2', 'Mikaela Bala', 'mikaelabala@gmail.com', '0682345678', 'Tirane', '123', 'Lawyer'),
('34567-3456789-3', 'Adelina Paskalaj', 'adelinapaskalaj@gmail.com', '0683456789', 'Tirane', '123', 'Lawyer'),
('45678-4567891-4', 'Gledi Tace', 'gleditace@gmail.com', '0684567890', 'Tirane', '123', 'Lawyer');


SELECT COUNT(*) FROM cases WHERE status IN ('Assigned', 'Pending', 'In Progress');
SELECT COUNT(*) FROM appointments WHERE appointment_date >= CURDATE();

