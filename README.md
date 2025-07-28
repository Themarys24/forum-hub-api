# 🏆 Forum Hub API

Complete REST API for discussion forum management developed with Spring Boot as part of the Oracle ONE Challenge.

## 🚀 Implemented Features

### 🔐 JWT Authentication
- **POST** `/auth/register` - User registration
- **POST** `/auth/login` - Login and JWT token generation

### 📝 Complete Topic CRUD
- **GET** `/topics` - List all topics (ordered by date)
- **GET** `/topics/{id}` - Get specific topic
- **GET** `/topics/{id}/details` - Complete details with responses
- **POST** `/topics` - Create new topic 🔒
- **PUT** `/topics/{id}` - Update topic 🔒
- **DELETE** `/topics/{id}` - Delete topic 🔒

### 🔍 Filters and Search
- **GET** `/topics/search?title=term` - Search by title
- **GET** `/topics/status/{status}` - Filter by status (OPEN/CLOSED/SOLVED)
- **GET** `/topics/course/{courseId}` - Filter by course
- **GET** `/topics?course=name&year=2024` - Search by course and year
- **GET** `/topics/top10` - Top 10 topics
- **GET** `/topics/paginated` - Paginated listing
- **GET** `/topics/my` - My topics 🔒

### ⚙️ Special Actions
- **PATCH** `/topics/{id}/close` - Close topic 🔒
- **PATCH** `/topics/{id}/solve` - Mark as solved 🔒

🔒 = Requires JWT authentication

## 🛠️ Technologies Used

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Security** + JWT Authentication
- **Spring Data JPA**
- **MySQL Database**
- **Bean Validation**
- **Flyway Migration** (configured)
- **Lombok**

## 🗃️ Database Model

```sql
users (id, name, email, password, role, active, created_at, updated_at)
courses (id, name, category, description, active, created_at, updated_at)  
topics (id, title, message, status, author_id, course_id, created_at, updated_at)
responses (id, message, topic_id, author_id, solution, created_at, updated_at)
⚙️ How to Run
1. Prerequisites
Java 17+
MySQL 8+
Maven
2. Setup
# Clone the repository
git clone https://github.com/your-username/forum-hub.git
cd forum-hub

# Configure MySQL database
CREATE DATABASE forumhub;
3. Environment Variables
Configure in IntelliJ or system:

DB_PASSWORD=your_mysql_password
JWT_SECRET=yourSuperSecretKey123!
4. Run
mvn spring-boot:run
The API will be available at: http://localhost:8080

🧪 Testing the API
1. Register user
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@email.com", 
  "password": "123456"
}
2. Login
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "john@email.com",
  "password": "123456"
}
3. Create topic (use token from login)
POST http://localhost:8080/topics
Authorization: Bearer YOUR_TOKEN_HERE
Content-Type: application/json

{
  "title": "How to configure Spring Security?",
  "message": "I'm having trouble configuring JWT authentication with Spring Security.",
  "courseId": 1
}
```

📋 Business Rules
✅ No duplicate topics allowed (same title + message)
✅ Only the author can edit/delete their own topics
✅ All required fields are validated
✅ JWT token expires in 24 hours
✅ Passwords are encrypted with BCrypt
✅ Soft delete for users and courses

🔒 Security
Stateless JWT authentication
BCrypt encrypted passwords
Authorization validation per endpoint
Global exception handling
Robust input validations

Authentication Endpoints
Method	Endpoint	Description	Auth Required
POST	/auth/register	Register new user	No
POST	/auth/login	Login and get JWT token	No
Topic Endpoints
Method	Endpoint	Description	Auth Required
GET	/topics	List all topics	No
GET	/topics/{id}	Get topic by ID	No
GET	/topics/{id}/details	Get topic with responses	No
POST	/topics	Create new topic	Yes
PUT	/topics/{id}	Update topic	Yes
DELETE	/topics/{id}	Delete topic	Yes
PATCH	/topics/{id}/close	Close topic	Yes
PATCH	/topics/{id}/solve	Mark as solved	Yes
Search & Filter Endpoints
Method	Endpoint	Description	Auth Required
GET	/topics/search?title=term	Search by title	No
GET	/topics/status/{status}	Filter by status	No
GET	/topics/course/{courseId}	Filter by course	No
GET	/topics/top10	Top 10 topics	No
GET	/topics/my	Current user's topics	Yes

🚀 Features Highlights
Complete CRUD Operations for topics
JWT Authentication with Spring Security
Advanced Search & Filtering capabilities
Pagination Support for large datasets
Input Validation with custom error messages
Global Exception Handling with structured responses
Role-based Authorization (User, Admin, Moderator)
Database Relationships properly mapped
RESTful API Design following best practices
