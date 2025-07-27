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
CREATE TABLE company(
   company_id INT PRIMARY KEY AUTO_INCREMENT,
   name VARCHAR(255) NOT NULL,
   email VARCHAR(255),
   phoneNo VARCHAR(255),
   address VARCHAR(255),
   created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE department(
  dept_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE company_department(
   company_dept_id INT PRIMARY KEY AUTO_INCREMENT,
   dept_id INT NOT NULL,
   company_id INT NOT NULL,
   description TEXT,
   total_offers INT DEFAULT 0,
   total_accepted_offers INT DEFAULT 0,
   FOREIGN KEY (dept_id) REFERENCES department(dept_id),
   FOREIGN KEY (company_id) REFERENCES company(company_id)
);
CREATE TABLE candidate(
   user_id INT PRIMARY KEY,
   candidate_id INT UNIQUE NOT NULL AUTO_INCREMENT,
   resume_link_path VARCHAR(255),
   experience INT DEFAULT 0,
   education TEXT,
   skill TEXT,
   FOREIGN KEY (user_id) REFERENCES user(user_id)
);
CREATE TABLE recruiter(
   user_id INT PRIMARY KEY,
   recruiter_id INT UNIQUE NOT NULL AUTO_INCREMENT,
   company_id INT NOT NULL,
   FOREIGN KEY (user_id) REFERENCES user(user_id),
   FOREIGN KEY (company_id) REFERENCES company(company_id)
);
CREATE TABLE interviewer(
   user_id INT PRIMARY KEY,
   interviewer_id INT UNIQUE NOT NULL AUTO_INCREMENT,
   company_dept_id INT NOT NULL,
   FOREIGN KEY (user_id) REFERENCES user(user_id),
   FOREIGN KEY (company_dept_id) REFERENCES company_department(company_dept_id)
);
CREATE TABLE application_stage(
   stage_id INT PRIMARY KEY,
   title VARCHAR(100) NOT NULL
);
CREATE TABLE job(
   job_id INT PRIMARY KEY AUTO_INCREMENT,
   title VARCHAR(255) NOT NULL,
   company_dept_id INT NOT NULL,
   description TEXT,
   total_applications INT DEFAULT 0,
   posted_by INT NOT NULL,
   status ENUM('open', 'closed', 'on_hold') DEFAULT 'open',
   created_at DATETIME(2) DEFAULT CURRENT_TIMESTAMP(2),
   FOREIGN KEY (company_dept_id) REFERENCES company_department(company_dept_id),
   FOREIGN KEY (posted_by) REFERENCES recruiter(recruiter_id)
);
CREATE TABLE applications(
   application_id INT PRIMARY KEY AUTO_INCREMENT,
   candidate_id INT NOT NULL,
   job_id INT NOT NULL,
   applied_at DATETIME(2) DEFAULT CURRENT_TIMESTAMP(2),
   current_stage_id INT,
   FOREIGN KEY (candidate_id) REFERENCES candidate(candidate_id),
   FOREIGN KEY (job_id) REFERENCES job(job_id),
   FOREIGN KEY (current_stage_id) REFERENCES application_stage(stage_id),
   INDEX idx_applications_job_stage (job_id, current_stage_id),
   INDEX idx_applications_candidate_time (candidate_id, applied_at DESC)
);
CREATE TABLE application_stage_history(
  id INT PRIMARY KEY AUTO_INCREMENT,
  application_id INT NOT NULL,
  stage_id INT NOT NULL,
  changed_by INT NOT NULL,
  changed_at DATETIME(2) DEFAULT CURRENT_TIMESTAMP(2),
  notes TEXT,
  FOREIGN KEY (application_id) REFERENCES applications(application_id),
  FOREIGN KEY (stage_id) REFERENCES application_stage(stage_id),
  FOREIGN KEY (changed_by) REFERENCES recruiter(recruiter_id)
);
CREATE TABLE interviews(
   id INT PRIMARY KEY AUTO_INCREMENT,
   application_id INT NOT NULL,
   interviewer_id INT NOT NULL,
   scheduled_at DATETIME NOT NULL,
   feedback TEXT,
   conducted_by INT NOT NULL,
   status ENUM('scheduled', 'cancelled', 'completed') DEFAULT 'scheduled',
   result ENUM('pass', 'fail', 'pending') DEFAULT 'pending',
   FOREIGN KEY (conducted_by) REFERENCES recruiter(recruiter_id),
   FOREIGN KEY (application_id) REFERENCES applications(application_id),
   FOREIGN KEY (interviewer_id) REFERENCES interviewer(interviewer_id),
   INDEX idx_interviews_interviewer_status (interviewer_id, status)
);
CREATE TABLE offers(
   offer_id INT PRIMARY KEY AUTO_INCREMENT,
   application_id INT NOT NULL UNIQUE,
   salary DECIMAL(12,2) NOT NULL,
   issued_at DATETIME(2) DEFAULT CURRENT_TIMESTAMP(2),
   updated_at DATETIME(2) DEFAULT CURRENT_TIMESTAMP(2) ON UPDATE CURRENT_TIMESTAMP(2),
   valid_till DATETIME(2) NOT NULL,
   status ENUM('pending', 'accepted', 'declined') DEFAULT 'pending',
   description TEXT,
   FOREIGN KEY (application_id) REFERENCES applications(application_id)
);
CREATE TABLE recruiter_activities(
   activity_id INT PRIMARY KEY AUTO_INCREMENT,
   recruiter_id INT NOT NULL,
   application_id INT,
   activity_date DATETIME(2) DEFAULT CURRENT_TIMESTAMP(2),
   activity_type ENUM('interview_conducted', 'application_reviewed', 'offer_created', 'stage_changed') NOT NULL,
   FOREIGN KEY (recruiter_id) REFERENCES recruiter(recruiter_id),
   FOREIGN KEY (application_id) REFERENCES applications(application_id)
);
CREATE TABLE audit(
   id INT PRIMARY KEY AUTO_INCREMENT,
   changed_by INT NOT NULL,
   action_type ENUM('access', 'update', 'delete', 'create') NOT NULL,
   table_name VARCHAR(100) NOT NULL,
   row_id INT NOT NULL,
   old_value TEXT,
   new_value TEXT,
   changed_at DATETIME(2) DEFAULT CURRENT_TIMESTAMP(2),
   FOREIGN KEY (changed_by) REFERENCES user(user_id)
);
INSERT INTO application_stage (stage_id, title) VALUES
(-1, 'rejected'),
(1, 'applied'),
(2, 'screened'),
(3, 'interview'),
(4, 'offer');