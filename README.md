
Personal Finance Tracker (Full-Stack)


A full-stack personal finance application designed to track and analyze spending habits. This project
features a secure, stateless Spring Boot backend that provides RESTful APIs for managing budgets,
expenses, and user authentication, all consumed by a dynamic, pre-built React frontend.
🚀 Live Demo & Video Walkthrough
Video Walkthrough: [https://www.youtube.com/watch?v=iYWdQCXrRhI]


📦 How to Run Locally
Prerequisites
Java 17+ & Maven
Node.js & npm
A running PostgreSQL database
1. Backend (Spring Boot)
1. Clone the repo:
git clone [https://github.com/Kshttij/finance-app](https://github.com/Kshttij/finance-app)

3. Configure the Database:
Open src/main/resources/application.properties .
Update the spring.datasource.url , spring.datasource.username , and
spring.datasource.password to point to your local PostgreSQL database.
4. Run the app:
mvn spring-boot:run
The backend will be running on http://localhost:8080 .
2. Frontend (React)
1. Navigate to the frontend folder:
cd ../frontend
2. Install dependencies:
npm install
3. Run the app:
npm run dev
The frontend will be running on http://localhost:5173 and will automatically connect to your
backend at http://localhost:8080 .
