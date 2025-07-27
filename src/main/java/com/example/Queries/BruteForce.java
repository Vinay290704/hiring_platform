package com.example.Queries;

import com.example.Connection.DataBaseConnector;
import com.example.ResponseEntity.CandidateResponse;
import com.example.ResponseEntity.InterviewSchedules;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BruteForce {
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
        try (Connection connection = DataBaseConnector.getConnection()) {
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
            System.out.println(e.getMessage());
        }
        return candidates;
    }

    public static List<InterviewSchedules> getInterviewSchedulesForInterviewer() {
        List<InterviewSchedules> interviewSchedulesList = new ArrayList<>();
        String statement = """
                Select *
                from interviews
                """;
        try (Connection connection = DataBaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {

                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return interviewSchedulesList;
    }
}
