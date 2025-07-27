package com.example.Queries;

import com.example.Connection.ConnectionManager;
import com.example.ResponseEntity.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Queries {

    // Frequent Query -1
    // List all candidates in the “interview” stage for a given job.
    public static List<CandidateResponse> listCandidatesInInterviewStage(int jobId) {
        List<CandidateResponse> candidates = new ArrayList<>();
        String statement =
                """
                        SELECT a.application_id , c.candidate_id ,u1.name , u1.gender , c.resume_link_path , c.experience
                        FROM candidate AS c
                        JOIN applications AS a
                        ON c.candidate_id = a.candidate_id AND a.job_id = ?
                        JOIN application_stage AS a1
                        ON a.current_stage_id = a1.stage_id AND a1.title = 'interview'
                        JOIN user AS u1
                        ON u1.user_id = c.user_id
                        """;
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, jobId); // setting jobId in place of question mark
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    CandidateResponse candidateResponse = new CandidateResponse(
                            resultSet.getInt("application_id"),
                            resultSet.getInt("candidate_id"),
                            resultSet.getString("name"),
                            resultSet.getString("gender") != null ?
                                    com.example.Enums.Gender.valueOf(resultSet.getString("gender").toUpperCase())
                                    : null,
                            resultSet.getString("resume_link_path"),
                            resultSet.getInt("experience")
                    );
                    candidates.add(candidateResponse);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return candidates;
    }

    // Frequent Query -2
    // Retrieve interview schedules for an interviewer.
    public static List<InterviewSchedules> getInterviewSchedulesForInterviewer(int interviewerId) {
        List<InterviewSchedules> interviewSchedulesList = new ArrayList<>();
        String statement = """
                SELECT
                i.application_id,
                u.name,
                j.title,
                i.scheduled_at,
                c.resume_link_path
                FROM interviews AS i
                JOIN applications AS a ON a.application_id = i.application_id
                JOIN job AS j ON j.job_id = a.job_id
                JOIN candidate AS c ON c.candidate_id = a.candidate_id
                JOIN user AS u ON u.user_id = c.user_id
                WHERE i.interviewer_id = ? AND i.status = 'scheduled'
                """;
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, interviewerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    InterviewSchedules interviewSchedules = new InterviewSchedules(
                            resultSet.getInt("application_id"),
                            resultSet.getString("name"),
                            resultSet.getString("title"),
                            resultSet.getString("scheduled_at"),
                            resultSet.getString("resume_link_path")
                    );
                    interviewSchedulesList.add(interviewSchedules);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return interviewSchedulesList;
    }

    // Frequent Query -3
    // Find jobs with more than 50 applications.
    public static List<JobResponse> JobsWithMoreThanFiftyApplication() {
        return findJobsWithEqualOrGreaterThanApplication(50);
    }

    private static List<JobResponse> findJobsWithEqualOrGreaterThanApplication(int count) {
        List<JobResponse> jobResponseList = new ArrayList<>();
        String statement = """
                SELECT job_id , title , total_applications , status , posted_by , description
                FROM job
                WHERE total_applications > ?;
                """;
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, count);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    resultSet.getString("status");
                    JobResponse jobResponse = new JobResponse(
                            resultSet.getInt("job_id"),
                            resultSet.getString("title"),
                            resultSet.getInt("total_applications"),
                            resultSet.getString("status") != null
                                    ? com.example.Enums.JobStatus.valueOf(resultSet.getString("status"))
                                    : null,
                            resultSet.getInt("posted_by"),
                            resultSet.getString("description")

                    );
                    jobResponseList.add(jobResponse);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return jobResponseList;
    }

    // Query-4 Show offer acceptance rate per department.
    public static List<AcceptanceRateResponse> findAcceptanceRatePerDepartment() throws SQLException {
        List<AcceptanceRateResponse> acceptanceRateResponseList = new ArrayList<>();
        String statement = """
                SELECT
                cd.company_dept_id,
                cd.dept_id,
                cd.company_id,
                c.name AS companyName,
                d.name AS departmentName,
                ROUND(cd.total_accepted_offers * 100 / cd.total_offers ,2) AS acceptance_rate
                FROM company_department AS cd
                JOIN company AS c
                ON c.company_id = cd.company_id
                JOIN department AS d
                ON d.dept_id = cd.dept_id;
                """;
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    AcceptanceRateResponse acceptanceRateResponse = new AcceptanceRateResponse(
                            resultSet.getInt("company_dept_id"),
                            resultSet.getInt("dept_id"),
                            resultSet.getInt("company_id"),
                            resultSet.getString("companyName"),
                            resultSet.getString("departmentName"),
                            resultSet.getDouble("acceptance_rate")
                    );
                    acceptanceRateResponseList.add(acceptanceRateResponse);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return acceptanceRateResponseList;
    }

    // Query-5
    // Show status of all applications of a candidate.
    public static List<ApplicationStatusOfCandidate> findStatusOfApplicationsOfCandidate(int candidateId) {
        List<ApplicationStatusOfCandidate> applicationStatusOfCandidateList = new ArrayList<>();
        String statement = """
                SELECT
                a.application_id,
                j.job_id,
                a1.title AS status,
                j.title,
                a.applied_at
                FROM applications AS a
                JOIN application_stage AS a1
                ON a1.stage_id = a.current_stage_id
                JOIN job AS j
                ON j.job_id = a.job_id
                WHERE a.candidate_id = ?
                """;
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, candidateId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ApplicationStatusOfCandidate applicationStatusOfCandidate = new ApplicationStatusOfCandidate(
                            resultSet.getInt("application_id"),
                            resultSet.getInt("job_id"),
                            resultSet.getString("status"),
                            resultSet.getString("title"),
                            resultSet.getString("applied_at")
                    );
                    applicationStatusOfCandidateList.add(applicationStatusOfCandidate);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return applicationStatusOfCandidateList;
    }
}