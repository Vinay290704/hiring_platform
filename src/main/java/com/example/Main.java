package com.example;

import com.example.Connection.ConnectionManager;
import com.example.Queries.Queries;
import com.example.ResponseEntity.*;
import com.example.setup.DataBaseSetup;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            DataBaseSetup.setupDatabase(scanner);
            while (true) {
                printMenu();
                System.out.print("Enter your choice (0-5): ");
                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice == 0) {
                        System.out.println("Exiting application. Goodbye!");
                        break;
                    }
                    handleUserChoice(choice, scanner);
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 0 and 5.");
                    scanner.nextLine(); // Clear the invalid input from the buffer
                } catch (SQLException e) {
                    System.err.println("A database error occurred: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } finally {
            ConnectionManager.closeConnection();
        }
    }

    private static void printMenu() {
        System.out.println("\n###################################################");
        System.out.println("###   Recruitment Platform - Interactive Menu   ###");
        System.out.println("###################################################");
        System.out.println("1. List candidates in 'interview' stage for a job");
        System.out.println("2. Retrieve interview schedules for an interviewer");
        System.out.println("3. Find jobs with more than 50 applications");
        System.out.println("4. Show offer acceptance rate per department");
        System.out.println("5. Show status of all applications for a candidate");
        System.out.println("0. Exit");
    }

    private static void handleUserChoice(int choice, Scanner scanner) throws SQLException {
        switch (choice) {
            case 1:
                runQuery1(scanner);
                break;
            case 2:
                runQuery2(scanner);
                break;
            case 3:
                runQuery3();
                break;
            case 4:
                runQuery4();
                break;
            case 5:
                runQuery5(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please select an option from the menu.");
        }
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static void runQuery1(Scanner scanner) throws SQLException {
        System.out.print("Enter Job ID: ");
        int jobId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\n--- [Query 1] Listing candidates in 'interview' stage for Job ID: " + jobId + " ---");
        List<CandidateResponse> list = Queries.listCandidatesInInterviewStage(jobId);
        if (list.isEmpty()) {
            System.out.println("No candidates found.");
        } else {
            String format = "| %-15s | %-25s | %-10s | %-12s |%n";
            System.out.format("+-----------------+---------------------------+------------+--------------+%n");
            System.out.format(format, "Application ID", "Name", "Gender", "Experience");
            System.out.format("+-----------------+---------------------------+------------+--------------+%n");
            for (CandidateResponse candidate : list) {
                System.out.format(format, candidate.applicationId(), candidate.name(), candidate.gender(), candidate.experience());
                System.out.println("| Resume Path     : " + candidate.resumeLinkPath());
            }
            System.out.format("+-----------------+---------------------------+------------+--------------+%n");
        }
    }

    private static void runQuery2(Scanner scanner) throws SQLException {
        System.out.print("Enter Interviewer ID: ");
        int interviewerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("\n--- [Query 2] Retrieving interview schedules for Interviewer ID: " + interviewerId + " ---");
        List<InterviewSchedules> list = Queries.getInterviewSchedulesForInterviewer(interviewerId);
        if (list.isEmpty()) {
            System.out.println("No schedules found.");
        } else {
            String format = "| %-15s | %-25s | %-30s |%n";
            System.out.format("+-----------------+---------------------------+--------------------------------+%n");
            System.out.format(format, "Application ID", "Candidate Name", "Job Title");
            System.out.format("+-----------------+---------------------------+--------------------------------+%n");
            for (InterviewSchedules schedule : list) {
                System.out.format(format, schedule.applicationId(), schedule.name(), schedule.jobTitle());
                System.out.println("| Scheduled At    : " + schedule.scheduledAt());
                System.out.println("| Resume Path     : " + schedule.resumeLinkPath());
            }
            System.out.format("+-----------------+---------------------------+--------------------------------+%n");
        }
    }

    private static void runQuery3() throws SQLException {
        System.out.println("\n--- [Query 3] Finding jobs with more than 50 applications ---");
        List<JobResponse> list = Queries.JobsWithMoreThanFiftyApplication();
        if (list.isEmpty()) {
            System.out.println("No jobs found meeting the criteria.");
        } else {
            String format = "| %-10s | %-35s | %-20s |%n";
            System.out.format("+------------+-------------------------------------+----------------------+%n");
            System.out.format(format, "Job ID", "Title", "Total Applications");
            System.out.format("+------------+-------------------------------------+----------------------+%n");
            for (JobResponse job : list) {
                System.out.format(format, job.job_id(), job.title(), job.total_applications());
                System.out.println("| Description     : " + job.description());
            }
            System.out.format("+------------+-------------------------------------+----------------------+%n");
        }
    }

    private static void runQuery4() throws SQLException {
        System.out.println("\n--- [Query 4] Showing offer acceptance rate per department ---");
        List<AcceptanceRateResponse> list = Queries.findAcceptanceRatePerDepartment();
        if (list.isEmpty()) {
            System.out.println("No data available to calculate rates.");
        } else {
            String format = "| %-25s | %-25s | %-18s |%n";
            System.out.format("+---------------------------+---------------------------+--------------------+%n");
            System.out.format(format, "Department", "Company", "Acceptance Rate %");
            System.out.format("+---------------------------+---------------------------+--------------------+%n");
            for (AcceptanceRateResponse rate : list) {
                System.out.format(format, rate.departmentName(), rate.companyName(), rate.acceptance_rate());
            }
            System.out.format("+---------------------------+---------------------------+--------------------+%n");
        }
    }

    private static void runQuery5(Scanner scanner) throws SQLException {
        System.out.print("Enter Candidate ID: ");
        int candidateId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\n--- [Query 5] Showing status of all applications for Candidate ID: " + candidateId + " ---");
        List<ApplicationStatusOfCandidate> list = Queries.findStatusOfApplicationsOfCandidate(candidateId);
        if (list.isEmpty()) {
            System.out.println("No applications found for this candidate.");
        } else {
            String format = "| %-15s | %-35s | %-15s | %-25s |%n";
            System.out.format("+-----------------+-------------------------------------+-----------------+---------------------------+%n");
            System.out.format(format, "Application ID", "Job Title", "Status", "Applied At");
            System.out.format("+-----------------+-------------------------------------+-----------------+---------------------------+%n");
            for (ApplicationStatusOfCandidate app : list) {
                System.out.format(format, app.applicationId(), app.jobTitle(), app.status(), app.appliedAt());
            }
            System.out.format("+-----------------+-------------------------------------+-----------------+---------------------------+%n");
        }
    }
}
