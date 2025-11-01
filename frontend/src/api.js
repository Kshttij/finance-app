// src/api.js
import axios from "axios";

// Create an axios instance with a base URL
const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

// ✅ Interceptor: Attach the JWT token to every request
API.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// --- Auth ---
export const login = (credentials) => API.post("/auth/login", credentials);
export const register = (credentials) => API.post("/auth/register", credentials);

// --- Budgets ---
export const getBudgets = () => API.get("/budgets");
export const getBudget = (id) => API.get(`/budgets/${id}`);
export const createBudget = (budget) => API.post("/budgets", budget);
export const deleteBudget = (id) => API.delete(`/budgets/${id}`);

// --- Expenses ---
export const getExpenses = () => API.get("/expenses");
export const getExpensesForBudget = (budgetId) => API.get(`/expenses?budgetId=${budgetId}`);
export const createExpense = (expense) => API.post("/expenses", expense);
export const deleteExpense = (id) => API.delete(`/expenses/${id}`);

// Export the instance if needed, but functions are cleaner
export default API;