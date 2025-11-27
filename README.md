# Spring Boot Bookmark Application

This is a simple Spring Boot application for managing bookmarks. It provides basic CRUD (Create, Read, Update, Delete) operations for bookmarks and demonstrates the implementation of idempotency for bookmark creation.

## Features

-   **Bookmark Management**: Create, view, edit, and delete bookmarks.
-   **Idempotent Bookmark Creation**: Ensures that a bookmark creation request, when sent multiple times with the same `Idempotency-Key`, is processed only once.
-   **Thymeleaf UI**: A server-side rendered UI for managing bookmarks.
-   **REST API**: A RESTful API for interacting with bookmark data.

## Technologies Used

-   Spring Boot 3.2.0
-   Spring Data JPA
-   Thymeleaf
-   MySQL Connector/J
-   Lombok
-   AspectJ (for AOP)
-   Jackson (for JSON processing)

## Setup and Run

### Prerequisites

-   Java 17 or higher
-   Maven 3.x
-   MySQL Database

### Database Configuration

1.  **Create a MySQL database**: For example, `bookmark_db`.
2.  **Update `application.properties`**: Located in `src/main/resources/application.properties`.

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/bookmark_db?useSSL=false&serverTimezone=UTC
    spring.datasource.username=your_mysql_username
    spring.datasource.password=your_mysql_password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    ```

    *Replace `your_mysql_username` and `your_mysql_password` with your actual MySQL credentials.* 
    *`spring.jpa.hibernate.ddl-auto=update` will automatically create/update tables. For production, consider `validate` or `none` and use migration tools like Flyway/Liquibase.*

### Build and Run the Application

1.  **Navigate to the project root directory**:

    ```bash
    cd /Users/yhbyun/work/study/spring-boot/spring-boot-bookmark
    ```

2.  **Build the project using Maven**:

    ```bash
    mvn clean install
    ```

3.  **Run the Spring Boot application**:

    ```bash
    mvn spring-boot:run
    ```

    The application will start on `http://localhost:8080`.

## Usage

### Web UI

-   Access the bookmark list page: `http://localhost:8080/bookmarks`
-   From this page, you can add new bookmarks, and edit or delete existing ones.

### REST API

-   **Get all bookmarks**:
    `GET http://localhost:8080/api/bookmarks`

-   **Create a new bookmark**:
    `POST http://localhost:8080/api/bookmarks`
    Content-Type: `application/json`
    Body:
    ```json
    {
        "title": "Example Site",
        "url": "https://example.com",
        "description": "A sample description"
    }
    ```

-   **Create a new bookmark with Idempotency (Recommended)**:
    `POST http://localhost:8080/api/bookmarks`
    Content-Type: `application/json`
    `Idempotency-Key: your-unique-key-here` (e.g., a UUID)
    Body:
    ```json
    {
        "title": "Idempotent Example",
        "url": "https://idempotent.com",
        "description": "This will only be created once"
    }
    ```
    Sending this request multiple times with the same `Idempotency-Key` will result in the same response being returned, and the bookmark will only be created once in the database.

-   **Update an existing bookmark**:
    `PUT http://localhost:8080/api/bookmarks/{id}`
    Content-Type: `application/json`
    Body:
    ```json
    {
        "title": "Updated Site Title",
        "url": "https://updated.example.com",
        "description": "Updated description"
    }
    ```

-   **Delete a bookmark**:
    `DELETE http://localhost:8080/api/bookmarks/{id}`

## Idempotency Implementation Details

Refer to `IDEMPOTENCY_IMPLEMENTATION_PLAN.md` for a detailed breakdown of how idempotency was implemented using Spring AOP, a custom `@Idempotent` annotation, and a dedicated `IdempotencyKey` entity to store request keys and their responses.
