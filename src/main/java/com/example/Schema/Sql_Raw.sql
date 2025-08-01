create schema recruitment_db;
use recruitment_db;
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



insert into user (name, email, gender, phoneNo, dateOfBirth, password) values
('Amit Kumar', 'amit.k@example.com', 'MALE', '9876543210', '1990-05-15', 'pass123'),
('Priya Sharma', 'priya.s@example.com', 'FEMALE', '9876543211', '1992-08-20', 'pass123'),
('Rajesh Verma', 'rajesh.v@example.com', 'MALE', '9876543212', '1988-11-25', 'pass123'),
('Sunita Singh', 'sunita.s@example.com', 'FEMALE', '9876543213', '1995-02-10', 'pass123'),
('Vikram Rathore', 'vikram.r@example.com', 'MALE', '9876543214', '1985-07-30', 'pass123'),
('Anjali Gupta', 'anjali.g@example.com', 'FEMALE', '9876543215', '1998-01-05', 'pass123'),
('Sandeep Das', 'sandeep.d@example.com', 'MALE', '9876543216', '1991-03-12', 'pass123'),
('Meera Iyer', 'meera.i@example.com', 'FEMALE', '9876543217', '1989-09-18', 'pass123');

insert into user (name, email, gender, phoneno, dateofbirth, password)
select
    concat('User', seq),
    concat('user', seq, '@example.com'),
    'MALE',
    '7984651234',
    '1998-06-09',
    'password'
from (
    select n1.n + n10.n * 10 + n100.n * 100 + 1 as seq
    from
        (select 0 as n union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as n1,
        (select 0 as n union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as n10,
        (select 0 as n union all select 1 union all select 2) as n100
) as numbers
where seq <= 300;

insert into company (name, email, address) values
('Innovate Solutions', 'contact@innovate.com', 'Bangalore'),
('Future Tech Inc.', 'info@futuretech.com', 'Pune'),
('Data Dynamics', 'hello@datadynamics.com', 'Hyderabad'),
('CloudNet Services', 'support@cloudnet.com', 'Chennai');

insert into department (name) values
('Engineering'),
('Product Management'),
('Human Resources'),
('Data Science'),
('Cloud Operations'),
('Marketing');

insert into company_department (company_id, dept_id, description) values
(1, 1, 'Core software development team'),
(1, 2, 'Product strategy and roadmap'),
(1, 6, 'Digital Marketing team'),
(2, 1, 'R&D and new technologies'),
(2, 3, 'Recruitment and employee relations'),
(3, 4, 'AI and Machine Learning division'),
(3, 1, 'Big Data Engineering team'),
(4, 5, 'Infrastructure and SRE team'),
(4, 6, 'Growth Marketing'),
(1, 4, 'Data Science R&D');

insert into candidate (user_id, resume_link_path, experience, education) values
(1, '/resumes/amit.pdf', 5, 'B.Tech in Computer Science'),
(2, '/resumes/priya.pdf', 3, 'MBA'),
(4, '/resumes/sunita.pdf', 1, 'B.Sc in IT'),
(6, '/resumes/anjali.pdf', 0, 'B.E. in Electronics'),
(7, '/resumes/sandeep.pdf', 8, 'M.Tech in AI'),
(8, '/resumes/meera.pdf', 6, 'M.Sc in Statistics');

insert into candidate (user_id, experience)
select user_id, floor(rand() * 15) from user where user_id > 8;

insert into recruitor (user_id, company_id) values (3, 1), (8, 3);
insert into interviewer (user_id, company_dept_id) values (5, 1), (7, 6);

insert into job (title, company_dept_id, description, posted_by, status) values
('Senior Java Developer', 1, 'Looking for a senior Java developer with 5+ years of experience.', 1, 'open'),
('Product Manager', 2, 'Seeking a product manager to lead our new mobile app.', 1, 'open'),
('Frontend Engineer', 4, 'React developer needed for our R&D team.', 1, 'open'),
('Data Scientist', 6, 'ML expert for our Data Dynamics team.', 2, 'open'),
('DevOps Engineer', 8, 'Cloud infrastructure specialist for AWS/GCP.', 2, 'open'),
('Digital Marketing Head', 9, 'Lead our growth marketing efforts.', 2, 'closed'),
('Lead AI Researcher', 6, 'PhD in Computer Science preferred.', 2, 'on_hold');

insert into applications (candidate_id, job_id, current_stage_id)
select candidate_id, 1, floor(1 + rand() * 2) from candidate where user_id between 9 and 70;

insert into applications (candidate_id, job_id, current_stage_id)
select candidate_id, 4, floor(1 + rand() * 2) from candidate where user_id between 71 and 140;

insert into applications (candidate_id, job_id, current_stage_id)
select candidate_id, floor(2 + rand() * 5), floor(1 + rand() * 4) from candidate where user_id between 141 and 250;

insert into applications (candidate_id, job_id, current_stage_id)
select user_id, 3, 3 from user where user_id between 251 and 260;

insert into applications (candidate_id, job_id, current_stage_id) values
(3, 1, 1), (3, 2, 2), (3, 3, 3), (3, 4, 4), (3, 5, 1),
(3, 6, 2), (3, 7, 3), (3, 1, 4), (3, 2, 1), (3, 3, 2);

insert into applications (candidate_id, job_id, current_stage_id) values
(1, 2, 3), (2, 2, 3), (4, 1, 1), (4, 3, 2), (6, 4, 3), (7, 5, 3);

insert into interviews (application_id, interviewer_id, scheduled_at, status, conducted_by)
select application_id, 1, now() + interval floor(1 + rand() * 14) day, 'scheduled', 1 from applications where current_stage_id = 3 limit 15;

insert into interviews (application_id, interviewer_id, scheduled_at, status, result, conducted_by)
select application_id, 2, now() - interval floor(1 + rand() * 14) day, 'completed', 'pass', 2 from applications where job_id = 4 limit 5;

-- Corrected offer generation to prevent duplicates
insert into offers (application_id, salary, valid_till, status)
select i.application_id, round(1000000 + rand() * 2000000, 2), now() + interval 15 day, 'pending'
from interviews i
join applications a on i.application_id = a.application_id
where i.result = 'pass'
  and a.application_id not in (
      (select application_id from applications where candidate_id = 1 and job_id = 2),
      (select application_id from applications where candidate_id = 71 and job_id = 4),
      (select application_id from applications where candidate_id = 72 and job_id = 4)
  )
limit 3;

insert into offers (application_id, salary, valid_till, status) values
((select application_id from applications where candidate_id = 1 and job_id = 2), 1800000.00, '2025-08-25 00:00:00', 'accepted');

insert into offers (application_id, salary, valid_till, status) values
((select application_id from applications where candidate_id = 71 and job_id = 4), 2500000.00, '2025-08-26 00:00:00', 'declined');

insert into offers (application_id, salary, valid_till, status) values
((select application_id from applications where candidate_id = 72 and job_id = 4), 2600000.00, '2025-08-27 00:00:00', 'accepted');

update job set total_applications = (select count(*) from applications where applications.job_id = job.job_id);

update company_department cd set
total_offers = (select count(*) from offers o join applications a on o.application_id = a.application_id where a.job_id in (select job_id from job where company_dept_id = cd.company_dept_id)),
total_accepted_offers = (select count(*) from offers o join applications a on o.application_id = a.application_id where o.status = 'accepted' and a.job_id in (select job_id from job where company_dept_id = cd.company_dept_id));

select 'Data inserted successfully.' as status;