# Recruitment & Applicant Tracking System (JDBC)

This project is a Java-based application that implements a backend system for a recruitment and applicant tracking platform. It uses JDBC for direct database communication to manage job postings, candidate applications, interview scheduling, and hiring decisions.

## Overview

The system provides a foundational structure for a hiring platform where recruiters can manage the entire recruitment lifecycle. It handles everything from posting a new job to tracking an applicant through various stages like screening, interviewing, and offering a position. The project focuses on clean database design and direct data access using Java's JDBC API.

## Prerequisites

To build and run this project, you will need the following installed on your system:

-   **Java Development Kit (JDK):** Version 17 or higher.
-   **Apache Maven:** To manage project dependencies and build the application.
-   **MySQL Server:** A running instance of MySQL database (version 8.0 or higher).
-   **A Code Editor:** An IDE like Visual Studio Code or IntelliJ IDEA.

## Setup and Installation

Follow these steps to get the project running on your local machine.

### 1. Database Setup

-   **Connect to MySQL:** Open your preferred MySQL client (like MySQL Workbench, DBeaver, or the command line).
-   **Create a Database:** Create a new database for the project. The default name is `recruitment_db` but you can change it in the config file.
    ```sql
    CREATE DATABASE recruitment_db;
    ```
-   **Run the Schema Script:** Execute the provided SQL schema script to create all the necessary tables and insert the initial `application_stage` data.

### 2. Application Configuration
There are two ways for configuration.

**Option 1: Using Dotenv**
1.  Create a `.env` file in the root project folder.
2.  Configure your credentials:
    ```properties
    # Database Configuration
    DB_HOST=localhost
    DB_PORT=3306
    DB_NAME=recruitment_db
    DB_USER=root
    DB_PASSWORD=your_secret_password
    ```
3.  Ensure your code uses `DataBaseConnector1` from `com.example/Connection/UsingDotenv/DataBaseConnector1`.

**Option 2: Using config.properties**
1.  **Create `resources` folder:** Inside `src/main/`, create a new folder named `resources`.
2.  **Create Configuration File:** Inside `src/main/resources/`, create a new file named `config.properties`.
3.  **Add Your DB Credentials:** Open `config.properties` and add your database connection details. **Use your own configuration.**
    ```properties
    # Database Configuration
    DB_HOST=localhost
    DB_PORT=3306
    DB_NAME=recruitment_db
    DB_USER=root
    DB_PASSWORD=your_secret_password
    ```

**Common Step: Populate Data**
- Run the script from `Schema/Raw_data` in SQL Workbench to get some sample data to practice with.

### 3. Build the Project

-   **Open a terminal** in the root directory of the project (where the `pom.xml` file is located).
-   **Run Maven Install:** This command will download all the required dependencies (like the MySQL JDBC driver) and compile your code.
    ```bash
    mvn clean install
    ```

## How to Run

After setting up the database and building the project, you can run the main application.

1.  **Navigate to the `Main` class:** Open the `Main.java` file in your IDE.
2.  **Run the file:** Execute the `main` method.

The application will now connect to the database using your chosen configuration method and print the query results to the console.