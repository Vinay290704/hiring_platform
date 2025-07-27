# Recruitment & Applicant Tracking System (JDBC)

This project is a Java-based application that implements a backend system for a recruitment and applicant tracking
platform. It uses JDBC for direct database communication to manage job postings, candidate applications, interview
scheduling, and hiring decisions.

## Overview

The system provides a foundational structure for a hiring platform where recruiters can manage the entire recruitment
lifecycle. It handles everything from posting a new job to tracking an applicant through various stages like screening,
interviewing, and offering a position. The project focuses on clean database design and direct data access using Java's
JDBC API.

## Instructions for Running the `.jar` File

This section is for users who have received the final `.jar` file and want to run the application directly.

### Prerequisites

- **Java Runtime Environment (JRE):** Version 17 or higher must be installed on your system. You can verify this by running `java -version` in your terminal.
- **MySQL Server:** You must have a running instance of MySQL Server. The application will handle the rest.


## Note

- The `Entities` folder is to give the basic idea of the Tables in Database , the `ResponseEntity` table is the format in
  which we will recieve data from running queries.
- The `Enums` folder contains the enums used in Entities and ResponseEntity Folder
- The `Schema` folder contains the requirement , schema and raw data script that will help in database setup in your sql
  workbench

---

## Setup for Developers

This section is for developers who want to build the project from the source code.

### Prerequisites

To build and run this project, you will need the following installed on your system:

- **Java Development Kit (JDK):** Version 17 or higher.
- **Apache Maven:** To manage project dependencies and build the application.
- **MySQL Server:** A running instance of MySQL database (version 8.0 or higher).
- **A Code Editor:** An IDE like Visual Studio Code or IntelliJ IDEA.

### Build the Project

1.  **Open a terminal** in the root directory of the project (where the `pom.xml` file is located).
2.  **Run Maven Package:** This command will download all required dependencies and create a runnable `.jar` file with all dependencies included.
    ```bash
    mvn clean package
    ```
    This will generate a file like `jdbc-learning-1.0-SNAPSHOT-jar-with-dependencies.jar` in the `target` directory.

---
### How to Run

1.  **Place the JAR file:** Put the `.jar` file (e.g., `hiring_platform.jar`) into any folder on your computer.

2.  **Open a Terminal:** Open a command prompt or terminal and navigate to the folder where you placed the `.jar` file.

3.  **Execute the Application:** Run the following command:
    ```bash
    java -jar hiring_platform.jar
    ```
    *(Note: You must replace the file name with the exact name of the JAR file you have.)*

4.  **Automatic Database Setup:**
    - The application will start and prompt you for your MySQL `username` and `password`.
    - After you provide the credentials, the application will automatically connect to your MySQL server, create the `recruitment_db` database, create all the necessary tables, and fill them with sample data.

5.  **Use the Application:** Once the setup is complete, the main interactive menu will appear, and you can begin using the platform.
