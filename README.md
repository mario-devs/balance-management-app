# ProyectoChill - Student Management App

A JavaFX desktop application for managing students, expenses, and categories.

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven 3.6+

## Building the Application

The project uses Maven for dependency management and building. All necessary dependencies, including JavaFX and SQLite, are declared in the `pom.xml`.

To compile the application, run:
```bash
mvn clean compile
```

## Running the Application

To run the application locally:
```bash
mvn javafx:run
```

## Project Structure
- `src/`: Contains the Java source code and FXML view resources.
- `src/lib/`: Contains legacy or local JAR files included in the build via Maven system scopes.
- `data.db`: The local SQLite database used by the application to store student and expense data.

## Note for Employers
This project was originally an older Ant-based application (NetBeans) that relied on the bundled JavaFX found in Java 8. It has been audited and successfully migrated to **Maven**, ensuring it compiles and runs seamlessly on modern Java environments (JDK 11+) without requiring complicated local setups or deprecated IDEs.

## Database Note
The SQLite database `data.db` is included for testing purposes to quickly showcase the app without needing a database setup script. You may choose to uncomment the database line in `.gitignore` if you prefer to keep local testing data off GitHub.

