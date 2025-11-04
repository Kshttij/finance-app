Personal Finance Tracker (Full-Stack)
A full-stack personal finance application designed to track and analyze spending habits. This project
features a secure, stateless Spring Boot backend that provides RESTful APIs for managing budgets,
expenses, and user authentication, all consumed by a dynamic, pre-built React frontend.
🚀 Live Demo & Video Walkthrough
Video Walkthrough: [Link to Your Video Demo Here]
Live Frontend (Vercel): [Link to Your Vercel Deployed App]
Live Backend (Render): [Link to Your Render Backend API]
🌟 Key Features
🔐 Secure Authentication: Full user registration and login flow built from scratch using Spring
Security and stateless JWT (JSON Web Tokens).
🛡️ API Security: All data endpoints are protected. The API validates the JWT on every request
using a custom JwtRequestFilter and ensures users can only access their own data (prevents
Insecure Direct Object Reference).
📊 Budget & Expense Tracking: Full CRUD (Create, Read, Update, Delete) operations for
budgets and expenses.
📈 Spending Insights: A dynamic backend GROUP BY query (using a JPQL Constructor
Expression) aggregates all user expenses by category, providing data for the frontend's
dashboard charts.
🔄 Full-Stack Integration: A decoupled, stateless backend API that provides all data and logic
for the React frontend, demonstrating a modern, production-ready architecture.
🏛️Architecture & Security Flow
This project is a decoupled, three-tier application. The backend is a "headless" API that knows
nothing about the frontend, and the frontend is a "thin client" that only knows how to make API calls.
How it Works: The JWT Authentication Flow
My primary focus was building a secure, stateless backend. Here is the flow for every user action:
1. Login: A user logs in via the React frontend. The POST /api/auth/login request hits the
AuthService .
2. Validation: The service manually checks the username and hashed password (using
BCryptPasswordEncoder.matches() ) against the PostgreSQL database.
3. Token Generation: If valid, the JwtUtil class generates a new, signed JWT containing the
user's username. This token is sent back to the user.
11/5/25, 2:26 AM Google Gemini
https://gemini.google.com/app/f2e898f771dc91ef?is_sa=1&is_sa=1&android-min-version=301356232&ios-min-version=322.0&campaign_id=bkws&ut… 1/4
4. Token Storage: The React frontend receives the JWT and stores it in localStorage .
5. Secured Request: The user now tries to fetch their budgets ( GET /api/budgets ). The
frontend's Axios Interceptor automatically attaches the JWT to the Authorization: Bearer
... header.
6. Backend "Bouncer": The request hits the Spring Boot backend and is immediately intercepted by
the JwtRequestFilter .
7. Token Validation: This "bouncer" filter:
Validates the token's signature and expiration date ( jwtUtil.getUsernameFromToken ).
Extracts the username from the token.
Checks the database (via MyUserDetailsService ) to ensure the user still exists.
8. Context Set: If all checks pass, the filter manually creates an AuthenticationToken and sets it
in the SecurityContextHolder . This tells Spring Security, "This user is authenticated for this
single request."
9. Controller: The request is finally passed to the BudgetController . It gets the authenticated
user from the Principal object, fetches their data, and returns it as JSON.
This entire flow happens on every secured API call, ensuring the backend is fast, secure, and fully
stateless.
🛠️Tech Stack
Area Technology
Backend Java 17, Spring Boot, Spring Security, Spring Data JPA, Hibernate, JWT (jjwt)
Frontend React, React Router, Chart.js, React-Chartjs-2, Axios, React Toastify
Database PostgreSQL
Deployment Render (Backend), Vercel (Frontend), Railway (Database)
📖API Endpoints
All data endpoints (except /auth ) are protected and require a valid JWT.
Method Endpoint Description Protecte
d
Auth
POST /api/auth/register Registers a new user. No
11/5/25, 2:26 AM Google Gemini
https://gemini.google.com/app/f2e898f771dc91ef?is_sa=1&is_sa=1&android-min-version=301356232&ios-min-version=322.0&campaign_id=bkws&ut… 2/4
POST /api/auth/login Logs in a user and returns a JWT. No
Budgets
GET /api/budgets Gets all budgets for the logged-in user. Yes
POST /api/budgets Creates a new budget for the logged-in user. Yes
GET /api/budgets/{id} Gets a single budget. (Also checks ownership). Yes
DELET
E
/api/budgets/{id} Deletes a single budget. (Also checks ownership). Yes
Expense
s
GET /api/expenses Gets all expenses for the user. Yes
GET /api/expenses?budgetId=
{id}
Gets all expenses for a specific budget. Yes
POST /api/expenses Creates a new expense. (Also checks budget
ownership).
Yes
DELET
E
/api/expenses/{id} Deletes a single expense. (Also checks
ownership).
Yes
Insights
GET /api/insights/summary Gets total spending grouped by category. Yes
📦 How to Run Locally
Prerequisites
Java 17+ & Maven
Node.js & npm
A running PostgreSQL database
1. Backend (Spring Boot)
1. Clone the repo:
git clone [https://github.com/your-username/your-repo-name.git](https://github.com/
cd your-repo-name/backend
11/5/25, 2:26 AM Google Gemini
https://gemini.google.com/app/f2e898f771dc91ef?is_sa=1&is_sa=1&android-min-version=301356232&ios-min-version=322.0&campaign_id=bkws&ut… 3/4
2. Configure the Database:
Open src/main/resources/application.properties .
Update the spring.datasource.url , spring.datasource.username , and
spring.datasource.password to point to your local PostgreSQL database.
3. Run the app:
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
