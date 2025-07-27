package com.example.Queries;

import com.example.Connection.UsingConfig.DataBaseConnector2;
import com.example.Connection.UsingDotenv.DataBaseConnector1;
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
                        Select a.application_id , c.candidate_id ,u1.name , u1.gender , c.resume_link_path , c.experience
                        from candidate as c
                        join applications as a
                        on c.candidate_id = a.candidate_id and a.job_id = ?
                        join application_stage as a1
                        on a.current_stage_id = a1.stage_id and a1.title = 'interview'
                        join user as u1
                        on u1.user_id = c.user_id
                        """;
        try (Connection connection = DataBaseConnector1.getConnection()) {
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
                select
                i.application_id,
                u.name,
                j.title,
                i.scheduled_at,
                c.resume_link_path
                from interviews as i
                join applications as a on a.application_id = i.application_id
                join job as j on j.job_id = a.job_id
                join candidate as c on c.candidate_id = a.candidate_id
                join user as u on u.user_id = c.user_id
                where i.interviewer_id = ? and i.status = 'scheduled'
                """;
        try (Connection connection = DataBaseConnector2.getConnection()) {
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
                select job_id , title , total_applications , status , posted_by , description
                from job
                where total_applications > ?;
                """;
        try (Connection connection = DataBaseConnector1.getConnection()) {
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
                select
                cd.company_dept_id,
                cd.dept_id,
                cd.company_id,
                c.name as companyName,
                d.name as departmentName,
                round(cd.total_accepted_offers / cd.total_offers ,2) as acceptance_rate
                from company_department as cd
                join company as c
                on c.company_id = cd.company_id
                join department as d
                on d.dept_id = cd.dept_id;
                """;
        try (Connection connection = DataBaseConnector1.getConnection()) {
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
                select
                a.application_id,
                j.job_id,
                a1.title as status,
                j.title,
                a.applied_at
                from applications as a
                join application_stage as a1
                on a1.stage_id = a.current_stage_id
                join job as j
                on j.job_id = a.job_id
                where a.candidate_id = ?
                """;

        try (Connection connection = DataBaseConnector1.getConnection()) {
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
