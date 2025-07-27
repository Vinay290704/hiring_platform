# Database Schema: Recruitment & Applicant Tracking

This document contains the complete SQL schema for the recruitment platform, including table creation and initial data
seeding for application stages.

## SQL Schema Definition

```sql
create database recruitment_db

-- User Management
create table user(
   user_id int primary key auto_increment,
   name varchar(100) not null,
   email varchar(255) unique not null,
   gender enum("MALE", "FEMALE", "OTHERS"),
   phoneNo varchar(50),
   dateOfBirth date,
   password varchar(255) not null,
   created_at datetime(2) default current_timestamp(2)
);

-- Company information
create table company(
   company_id int primary key auto_increment,
   name varchar(255) not null,
   email varchar(255),
   phoneNo varchar(255),
   address varchar(255),
   created_at datetime default current_timestamp
);

-- Department
create table department(
  dept_id int primary key auto_increment,
  name varchar(50) not null unique
);

-- Company-Department relationship
create table company_department(
   company_dept_id int primary key auto_increment,
   dept_id int not null,
   company_id int not null,
   description text,
   -- Denormalized columns for query optimization
   total_offers int default 0,
   total_accepted_offers int default 0,
   foreign key (dept_id) references department(dept_id),
   foreign key (company_id) references company(company_id)
);

-- Candidate profile
create table candidate(
   user_id int primary key,
   candidate_id int unique not null auto_increment,
   resume_link_path varchar(255),
   experience int default 0,
   education text,
   skill text,
   foreign key (user_id) references user(user_id)
);

-- Recruiter information
create table recruitor(
   user_id int primary key,
   recruitor_id int unique not null auto_increment,
   company_id int not null,
   foreign key (user_id) references user(user_id),
   foreign key (company_id) references company(company_id)
);

-- Interviewer information
create table interviewer(
   user_id int primary key,
   interviewer_id int unique not null auto_increment,
   company_dept_id int not null,
   foreign key (user_id) references user(user_id),
   foreign key (company_dept_id) references company_department(company_dept_id)
);

-- Application stages
create table application_stage(
   stage_id int primary key,
   title varchar(100) not null
);

-- Job postings
create table job(
   job_id int primary key auto_increment,
   title varchar(255) not null,
   company_dept_id int not null,
   description text,
   -- Denormalized column for query optimization
   total_applications int default 0,
   posted_by int not null,
   status enum('open', 'closed', 'on_hold') default 'open',
   created_at datetime(2) default current_timestamp(2),
   foreign key (company_dept_id) references company_department(company_dept_id),
   foreign key (posted_by) references recruitor(recruitor_id)
);

-- Job applications
create table applications(
   application_id int primary key auto_increment,
   candidate_id int not null,
   job_id int not null,
   applied_at datetime(2) default current_timestamp(2),
   current_stage_id int,
   foreign key (candidate_id) references candidate(candidate_id),
   foreign key (job_id) references job(job_id),
   foreign key (current_stage_id) references application_stage(stage_id),
   index idx_applications_job_stage(job_id, current_stage_id),
   index idx_applications_candidate_time(candidate_id , applied_at desc)
);

-- Application stage history
create table application_stage_history(
  id int primary key auto_increment,
  application_id int not null,
  stage_id int not null,
  changed_by int not null,
  changed_at datetime(2) default current_timestamp(2),
  notes text,
  foreign key (application_id) references applications(application_id),
  foreign key (stage_id) references application_stage(stage_id),
  foreign key (changed_by) references recruitor(recruitor_id)
);

-- Interview scheduling
create table interviews(
   id int primary key auto_increment,
   application_id int not null,
   interviewer_id int not null,
   scheduled_at datetime not null,
   feedback text,
   conducted_by int not null,
   status enum('scheduled', 'cancelled', 'completed') default 'scheduled',
   result enum('pass', 'fail', 'pending') default 'pending',
   foreign key (conducted_by) references recruitor(recruitor_id),
   foreign key (application_id) references applications(application_id),
   foreign key (interviewer_id) references interviewer(interviewer_id),
   index idx_interviews_interviewer_status (interviewer_id, status)
);

-- Job offers
create table offers(
   offer_id int primary key auto_increment,
   application_id int not null unique,
   salary decimal(12,2) not null,
   issued_at datetime(2) default current_timestamp(2),
   updated_at datetime(2) default current_timestamp(2) on update current_timestamp(2),
   valid_till datetime(2) not null,
   status enum('pending', 'accepted', 'declined') default 'pending',
   description text,
   foreign key (application_id) references applications(application_id)
);

-- Recruiter activity tracking
create table recruitor_activities(
   activity_id int primary key auto_increment,
   recruitor_id int not null,
   application_id int,
   activity_date datetime(2) default current_timestamp(2),
   activity_type enum('interview_conducted', 'application_reviewed', 'offer_created', 'stage_changed') not null,
   foreign key (recruitor_id) references recruitor(recruitor_id),
   foreign key (application_id) references applications(application_id)
);

-- Audit trail for security
create table audit(
   id int primary key auto_increment,
   changed_by int not null,
   action_type enum('access', 'update', 'delete', 'create') not null,
   table_name varchar(100) not null,
   row_id int not null,
   old_value text,
   new_value text,
   changed_at datetime(2) default current_timestamp(2),
   foreign key (changed_by) references user(user_id)
);

-- Sample data for application_stage
insert into application_stage (stage_id, title) values
(-1, 'rejected'),
(1, 'applied'),
(2, 'screened'),
(3, 'interview'),
(4, 'offer');