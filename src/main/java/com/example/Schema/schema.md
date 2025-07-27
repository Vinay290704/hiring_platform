# Database Schema: Recruitment & Applicant Tracking

This document contains the complete SQL schema for the recruitment platform, including table creation and initial data seeding for application stages.

## SQL Schema Definition

```sql
-- User management
CREATE TABLE user(
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    gender ENUM("MALE", "FEMALE", "OTHERS"),
    phoneNo VARCHAR(50),
    dateOfBirth DATE,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME(2) DEFAULT CURRENT_TIMESTAMP(2)
);

-- Company information
CREATE TABLE company(
    company_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phoneNo VARCHAR(255),
    address VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Department master
CREATE TABLE department(
  dept_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL UNIQUE
);

-- Company-Department relationship
CREATE TABLE company_department(
    company_dept_id INT PRIMARY KEY AUTO_INCREMENT,
    dept_id INT NOT NULL,
    company_id INT NOT NULL,
    description TEXT,
    FOREIGN KEY (dept_id) REFERENCES department(dept_id),
    FOREIGN KEY (company_id) REFERENCES company(company_id)
);

-- Candidate profile
CREATE TABLE candidate(
    user_id INT PRIMARY KEY,
    candidate_id INT UNIQUE NOT NULL AUTO_INCREMENT,
    resume_link_path VARCHAR(255),
    experience INT DEFAULT 0,
    education TEXT,
    skill TEXT,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- Recruitor information
CREATE TABLE recruitor(
    user_id INT PRIMARY KEY,
    recruitor_id INT UNIQUE NOT NULL AUTO_INCREMENT,
    company_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (company_id) REFERENCES company(company_id)
);

-- Interviewer information
CREATE TABLE interviewer(
    user_id INT PRIMARY KEY,
    interviewer_id INT UNIQUE NOT NULL AUTO_INCREMENT,
    company_dept_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (company_dept_id) REFERENCES company_department(company_dept_id)
);

-- Application stages
CREATE TABLE application_stage(
    stage_id INT PRIMARY KEY,
    title VARCHAR(100) NOT NULL
);

-- Job postings
CREATE TABLE job(
    job_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    company_dept_id INT NOT NULL,
    description TEXT,
    posted_by INT NOT NULL,
    status ENUM('open', 'closed', 'on_hold') DEFAULT 'open',
    created_at DATETIME(2) DEFAULT CURRENT_TIMESTAMP(2),
    FOREIGN KEY (company_dept_id) REFERENCES company_department(company_dept_id),
    FOREIGN KEY (posted_by) REFERENCES recruitor(recruitor_id)
);

-- Job applications
CREATE TABLE applications(
    application_id INT PRIMARY KEY AUTO_INCREMENT,
    candidate_id INT NOT NULL,
    job_id INT NOT NULL,
    applied_at DATETIME(2) DEFAULT CURRENT_TIMESTAMP(2),
    current_stage_id INT,
    FOREIGN KEY (candidate_id) REFERENCES candidate(candidate_id),
    FOREIGN KEY (job_id) REFERENCES job(job_id),
    FOREIGN KEY (current_stage_id) REFERENCES application_stage(stage_id),
    INDEX idx_applications_job_stage(job_id, current_stage_id)
);

-- Interview scheduling
CREATE TABLE interviews(
    id INT PRIMARY KEY AUTO_INCREMENT,
    application_id INT NOT NULL,
    interviewer_id INT NOT NULL,
    scheduled_at DATETIME NOT NULL,
    feedback TEXT,
    status ENUM('scheduled', 'cancelled', 'completed') DEFAULT 'scheduled',
    result ENUM('pass', 'fail', 'pending') DEFAULT 'pending',
    FOREIGN KEY (application_id) REFERENCES applications(application_id),
    FOREIGN KEY (interviewer_id) REFERENCES interviewer(interviewer_id),
    INDEX idx_interviews_interviewer_status (interviewer_id, status)
);

-- Job offers
CREATE TABLE offers(
    offer_id INT PRIMARY KEY AUTO_INCREMENT,
    application_id INT NOT NULL UNIQUE,
    salary DECIMAL(12,2) NOT NULL,
    issued_at DATETIME(2) DEFAULT CURRENT_TIMESTAMP(2),
    status ENUM('pending', 'accepted', 'declined') DEFAULT 'pending',
    FOREIGN KEY (application_id) REFERENCES applications(application_id)
);


-- Initial data for application stages
INSERT INTO application_stage (stage_id, title) VALUES
(-1, 'rejected'),
(1, 'applied'),
(2, 'screened'),
(3, 'interview'),
(4, 'offer');