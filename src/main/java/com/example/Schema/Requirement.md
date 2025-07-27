# Recruitment & Applicant Tracking

### Scenario Description
A hiring platform that manages job postings, candidate applications, interview rounds, and hiring decisions. Recruiters track candidates through multiple stages.

### Business Requirements
* Jobs are posted with titles, departments, descriptions, and statuses (open, closed, on_hold).
* Candidates register with personal details and upload resumes.
* Candidates apply to jobs; application statuses move through stages (applied, screened, interview, offer, rejected).
* Interviews are scheduled per application with interviewer and result.
* Offers are issued per application with salary details and statuses (pending, accepted, declined).
* Only one offer is allowed per application.
* Application timelines (dates of each stage) must be tracked.
* Recruiter activities (e.g., number of interviews conducted) are logged.
* Job-to-offer conversion rates and time-to-hire metrics are reported.
* Confidential candidate data must be stored securely.

### Frequent Queries
* List all candidates in the “interview” stage for a given job.
* Retrieve interview schedules for an interviewer.
* Find jobs with more than 50 applications.
* Show offer acceptance rate per department.
* Show status of all applications of a candidate.