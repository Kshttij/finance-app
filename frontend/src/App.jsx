import { createBrowserRouter, RouterProvider, Navigate } from "react-router-dom";
import AuthPage from "./pages/AuthPage";
import ProtectedRoute from "./components/ProtectedRoute";

// Library
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

// Layouts
import Main, { mainLoader } from "./layouts/Main";

// Actions
import { logoutAction } from "./actions/logout";
import { deleteBudget } from "./actions/deleteBudget";

// Routes
import Dashboard, { dashboardAction, dashboardLoader } from "./pages/Dashboard";
import Error from "./pages/Error";
import BudgetPage, { budgetAction, budgetLoader } from "./pages/BudgetPage";
import ExpensesPage, {
  expensesAction,
  expensesLoader,
} from "./pages/ExpensesPage";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Main />,
    loader: mainLoader,
    errorElement: <Error />,
    children: [
      // ✅ Dashboard (public; shows Intro if no user)
      {
        index: true,
        element: <Dashboard />,
        loader: dashboardLoader,
        action: dashboardAction,
        errorElement: <Error />,
      },

      // ✅ Protected Budget Page
      {
        path: "budget/:id",
        element: (
          <ProtectedRoute>
            <BudgetPage />
          </ProtectedRoute>
        ),
        loader: budgetLoader,
        action: budgetAction,
        errorElement: <Error />,
        children: [
          {
            path: "delete",
            action: deleteBudget,
          },
        ],
      },

      // ✅ Protected Expenses Page
      {
        path: "expenses",
        element: (
          <ProtectedRoute>
            <ExpensesPage />
          </ProtectedRoute>
        ),
        loader: expensesLoader,
        action: expensesAction,
        errorElement: <Error />,
      },

      // ✅ Login page (public)
      {
        path: "login",
        element: <AuthPage />,
      },

      // ✅ Logout
      {
        path: "logout",
        action: logoutAction,
      },

      // Redirect unknown routes
      {
        path: "*",
        element: <Navigate to="/" replace />,
      },
    ],
  },
]);

function App() {
  return (
    <div className="App">
      <RouterProvider router={router} />
      <ToastContainer position="top-center" autoClose={2000} />
    </div>
  );
}

export default App;
