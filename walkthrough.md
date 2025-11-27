# Bookmark Project Walkthrough

## Accomplished
- **Project Structure**: Created Spring Boot 3.2.0 project with Web, Data JPA, MySQL, Lombok.
- **Domain Layer**: Implemented `Bookmark` Entity.
- **Repository Layer**: Implemented `BookmarkRepository`.
- **Service Layer**: Implemented `BookmarkService`.
- **Web Layer**: Implemented `BookmarkController`.
- **Learning**: Implemented `HelloController` with text and JSON endpoints.
- **Database Configuration**:
  - Created `bookmark` database.
  - Configured `application.properties` with MySQL credentials.
  - Fixed connection issues by using `127.0.0.1`, adding `allowPublicKeyRetrieval=true`, and explicitly setting `hibernate.dialect`.
- **Verification**:
  - Build successful (`mvn clean install`).
  - Application startup successful (`mvn spring-boot:run`).
  - Database connection verified (Tables created automatically).
  - Hello World endpoints verified.
  - **Bookmark API Verified**:
    - POST `/api/bookmarks`: Successfully created a bookmark.
    - GET `/api/bookmarks`: Successfully retrieved the created bookmark.

## How to Run
1. Ensure MySQL is running.
2. Run the application:
   ```bash
   mvn spring-boot:run
   ```
3. Access the API:
   - **Hello World (Text)**: [http://localhost:8080/hello](http://localhost:8080/hello)
   - **Hello World (JSON)**: [http://localhost:8080/api/hello](http://localhost:8080/api/hello)
   - **Bookmarks**:
     - GET `http://localhost:8080/api/bookmarks`
     - POST `http://localhost:8080/api/bookmarks`

## API Testing Examples
### Create a Bookmark (POST)
```bash
curl -X POST http://localhost:8080/api/bookmarks \
    -H "Content-Type: application/json" \
    -d '{"title": "Google", "url": "https://www.google.com", "description": "Search Engine"}'
```

### Get All Bookmarks (GET)
```bash
curl http://localhost:8080/api/bookmarks
```

## Troubleshooting
- If you see `Unable to determine Dialect`, ensure `spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect` is present in `application.properties`.
