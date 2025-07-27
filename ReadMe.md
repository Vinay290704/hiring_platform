# Recruitment & Applicant Tracking System (JDBC)

This project is a Java-based application that implements a backend system for a recruitment and applicant tracking
platform. It uses JDBC for direct database communication to manage job postings, candidate applications, interview
scheduling, and hiring decisions.

## Overview

The system provides a foundational structure for a hiring platform where recruiters can manage the entire recruitment
lifecycle. It handles everything from posting a new job to tracking an applicant through various stages like screening,
interviewing, and offering a position. The project focuses on clean database design and direct data access using Java's
JDBC API.

## Note

- The Entities folder is to give the basic idea of the Tables in Database , the ResponseEntity table is the format in
  which we will recieve data from running queries.
- The Enums folder contains the enums used in Entities and ResponseEntity Folder
- There are two types of Connection : one through env and another through config.properties
- The Schema folder contains the requirement , schema and raw data script that will help in database setup in your sql
  workbench

## Prerequisites

To build and run this project, you will need the following installed on your system:

- **Java Development Kit (JDK):** Version 17 or higher.
- **Apache Maven:** To manage project dependencies and build the application.
- **MySQL Server:** A running instance of MySQL database (version 8.0 or higher).
- **A Code Editor:** An IDE like Visual Studio Code or IntelliJ IDEA.

## Setup and Run

Follow these steps to get the project configured, built, and running on your local machine.

### 1. Prerequisites

Ensure you have the following installed:

- **Java Development Kit (JDK):** Version 17 or higher.
- **Apache Maven:** For building the project.
- **MySQL Server:** Version 8.0 or higher.

---

### 2. Database Setup

1. **Connect to MySQL:** Open your preferred MySQL client (e.g., MySQL Workbench).

2. **Create Database:** Create a new schema for the project.
   ```sql
   CREATE SCHEMA recruitment_db;
   ```
3. **Create Tables and Populate Data:** The SQL scripts are now located in the `src/main/resources/` folder. Execute
   them in the following order:
    * First, run the contents of `schema.sql` to create all the necessary tables.
    * Second, run the contents of `Sql_Raw.sql` to insert the sample data.

---

### 3. Application Configuration

You must configure the database credentials for the application to connect successfully. Choose one of the two options
below.

**Option 1: Using `config.properties` (Recommended)**

1. Navigate to `src/main/resources/`.
2. Edit the `config.properties` file with your personal MySQL credentials.
   ```properties
   # Database Configuration
   DB_HOST=localhost
   DB_PORT=3306
   DB_NAME=recruitment_db
   DB_USER=your_username
   DB_PASSWORD=your_password
   ```

**Option 2: Using a `.env` file**

1. Create a file named `.env` in the project's root directory.
2. Add your credentials to the `.env` file.
   ```properties
   # Database Configuration
   DB_HOST=localhost
   DB_PORT=3306
   DB_NAME=recruitment_db
   DB_USER=your_username
   DB_PASSWORD=your_password
   ```

> **Note:** Ensure your code is set to use the correct `DataBaseConnector` class corresponding to your chosen
configuration method.

---

### 4. Build and Run the Application

1. **Build the Project:** Open a terminal in the root directory and run the following Maven command. This will compile
   the code and create a runnable `.jar` file in the `target` directory.
   ```bash
   mvn clean package
   ```

2. **Run the Application:** Execute the generated JAR file from the terminal.
   ```bash
   java -jar target/recruitment-system-1.0.jar
   ```
   *(Note: The JAR file name may vary based on the `artifactId` and `version` in your `pom.xml`)*

The application will now connect to the database and execute the implemented features.