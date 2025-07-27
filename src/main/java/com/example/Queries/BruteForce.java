package com.example.Queries;

import com.example.Connection.UsingConfig.DataBaseConnector2;
import com.example.Connection.UsingDotenv.DataBaseConnector1;
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
            System.out.println(e.getMessage());
        }
        return candidates;
    }

    public static List<InterviewSchedules> getInterviewSchedulesForInterviewer(int interviewerId) {
        List<InterviewSchedules> interviewSchedulesList = new ArrayList<>();
        String statement = """
                Select *
                from interviews as i
                join applications as a
                on a.application_id = i.application_id
                join job as j
                on j.job_id = a.job_id
                join candidate as c
                on c.candidate_id = a.candidate_id
                join user as u
                on u.user_id = c.user_id
                where i.interviewer_id  = ? and i.status = 'scheduled'
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
            System.out.println(e.getMessage());
        }
        return interviewSchedulesList;
    }
}
