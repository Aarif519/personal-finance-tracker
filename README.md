# FinTrack — Personal Finance Tracker

FinTrack is a personal finance management application built with Java and Spring Boot. It helps users manage transactions, organize expenses by category, and monitor their budgets.

## Features

* User registration and login
* Transaction management
* Expense categorization
* Budget management
* Financial summaries and reports
* Web interface built with Thymeleaf

## Tech Stack

* **Language:** Java
* **Backend:** Spring Boot
* **Database:** MySQL
* **ORM:** Spring Data JPA / Hibernate
* **Frontend:** HTML, CSS, Thymeleaf
* **Build Tool:** Maven

## Project Structure

```text
src/main/java/com/fintrack/fintrack/
├── config/
├── controllers/
├── dto/
├── entities/
├── repositories/
└── services/
```

## Getting Started

### Prerequisites

* Java Development Kit (JDK)
* MySQL Server
* Git

### Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/Aarif519/personal-finance-tracker.git
   ```

2. Open the project directory:

   ```bash
   cd personal-finance-tracker
   ```

3. Configure your local MySQL database and application properties.

4. Run the application using the Maven wrapper:

   **Windows PowerShell:**

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

5. Open the application in your browser using the local address configured by the project.

## Security

* Keep database credentials out of version control.
* Configure your own local database credentials before running the application.
* Never commit passwords, API keys, or other secrets.

## Future Improvements

* Transaction filtering and pagination
* Interactive financial charts
* CSV export
* Automated tests
* Improved authentication and authorization

## Author

**Aarif519**

GitHub: https://github.com/Aarif519
