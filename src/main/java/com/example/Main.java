package com.example;


import com.example.Queries.BruteForce;
import com.example.ResponseEntity.CandidateResponse;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {

        // Query-1 List all candidates in the “interview” stage for a given job.
        int jobId = 101;
        List<CandidateResponse> candidateResponseList = BruteForce.listCandidatesInInterviewStage(jobId);
        for (CandidateResponse candidateResponse : candidateResponseList) {
            System.out.println("application_id : " + candidateResponse.applicationId());
            System.out.println("candidate_id : " + candidateResponse.candidateId());
            System.out.println("name : " + candidateResponse.name());
            System.out.println("gender : " + candidateResponse.gender());
            System.out.println("resume_link_path : " + candidateResponse.resumeLinkPath());
            System.out.println("experience : " + candidateResponse.experience());
        }


    }
}

