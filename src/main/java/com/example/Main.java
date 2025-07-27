package com.example;


import com.example.Queries.Queries;
import com.example.ResponseEntity.*;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {

        // Query-1 List all candidates in the “interview” stage for a given job.
        int jobId = 101;
        List<CandidateResponse> candidateResponseList = Queries.listCandidatesInInterviewStage(jobId);
        for (CandidateResponse candidateResponse : candidateResponseList) {
            System.out.println("application_id : " + candidateResponse.applicationId());
            System.out.println("candidate_id : " + candidateResponse.candidateId());
            System.out.println("name : " + candidateResponse.name());
            System.out.println("gender : " + candidateResponse.gender());
            System.out.println("resume_link_path : " + candidateResponse.resumeLinkPath());
            System.out.println("experience : " + candidateResponse.experience());
        }

        // Query-2 Retrieve interview schedules for an interviewer
        int interviewId = 201;
        List<InterviewSchedules> interviewSchedulesList = Queries.getInterviewSchedulesForInterviewer(interviewId);
        for (InterviewSchedules interviewSchedules : interviewSchedulesList) {
            System.out.println("application_id: " + interviewSchedules.applicationId());
            System.out.println("name: " + interviewSchedules.name());
            System.out.println("jobTitle: " + interviewSchedules.jobTitle());
            System.out.println("scheduledAt: " + interviewSchedules.scheduledAt());
            System.out.println("resume_link_path: " + interviewSchedules.resumeLinkPath());
        }
        // Frequent Query- 3
        // Find jobs with more than 50 applications.
        List<JobResponse> jobResponseList = Queries.JobsWithMoreThanFiftyApplication();
        for (JobResponse jobResponse : jobResponseList) {
            System.out.println("job_id : " + jobResponse.job_id());
            System.out.println("title : " + jobResponse.title());
            System.out.println("total_applications : " + jobResponse.total_applications());
            System.out.println("posted_by : " + jobResponse.posted_by());
            System.out.println("description : " + jobResponse.description());
        }

        // Frequent Query- 4
        // Show offer acceptance rate per department.
        List<AcceptanceRateResponse> acceptanceRateResponseList = Queries.findAcceptanceRatePerDepartment();
        for (AcceptanceRateResponse acceptanceRateResponse : acceptanceRateResponseList) {
            System.out.println("company_dept_id: " + acceptanceRateResponse.company_dept_id());
            System.out.println("dept_id: " + acceptanceRateResponse.dept_id());
            System.out.println("company_id: " + acceptanceRateResponse.company_id());
            System.out.println("companyName: " + acceptanceRateResponse.companyName());
            System.out.println("departmentName: " + acceptanceRateResponse.departmentName());
        }

        // Frequent Query-5
        // Show status of all applications of a candidate.
        int candidateId = 101;
        List<ApplicationStatusOfCandidate> applicationStatusOfCandidateList = Queries.findStatusOfApplicationsOfCandidate(candidateId);
        for (ApplicationStatusOfCandidate applicationStatusOfCandidate : applicationStatusOfCandidateList) {
            System.out.println("application_id: " + applicationStatusOfCandidate.applicationId());
            System.out.println("jobId: " + applicationStatusOfCandidate.jobId());
            System.out.println("jobTitle: " + applicationStatusOfCandidate.jobTitle());
            System.out.println("status: " + applicationStatusOfCandidate.status());
            System.out.println("appliedAt: " + applicationStatusOfCandidate.appliedAt());
        }
    }
}

