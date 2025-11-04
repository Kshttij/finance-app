Personal Finance Tracker (Full-Stack)

A full-stack personal finance application designed to track and analyze spending habits. This project features a secure, stateless Spring Boot backend that provides RESTful APIs for managing budgets, expenses, and user authentication, all consumed by a dynamic, pre-built React frontend.

🚀 Live Demo & Video Walkthrough

Video Walkthrough: [https://youtu.be/iYWdQCXrRhI]


🌟 Key Features

🔐 Secure Authentication: Full user registration and login flow built from scratch using Spring Security and stateless JWT (JSON Web Tokens).

🛡️ API Security: All data endpoints are protected.

📊 Budget & Expense Tracking: Full CRUD (Create, Read, Update, Delete) operations for budgets and expenses.

🔄 Full-Stack Integration: A decoupled, stateless backend API that provides all data and logic for the React frontend, demonstrating a modern, production-ready architecture.


📦 How to Run Locally

Prerequisites

Java 17+ & Maven

Node.js & npm

A running PostgreSQL database

1. Backend (Spring Boot)

Clone the repo:

git clone [https://github.com/Kshttij/finance-app)
cd finance-app/backend


Configure the Database:

Open src/main/resources/application.properties.

Update the spring.datasource.url, spring.datasource.username, and spring.datasource.password to point to your local PostgreSQL database.

Run the app:

mvn spring-boot:run


The backend will be running on http://localhost:8080.

2. Frontend (React)

Navigate to the frontend folder:

cd ../frontend


Install dependencies:

npm install


Run the app:

npm run dev


The frontend will be running on http://localhost:5173 and will automatically connect to your backend at http://localhost:8080.
