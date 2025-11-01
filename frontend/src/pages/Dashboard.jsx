// src/pages/Dashboard.jsx
import { Link, useLoaderData } from "react-router-dom";
import { toast } from "react-toastify";

// ✅ Import API and helper
import {
  createBudget,
  createExpense,
  getBudgets,
  getExpenses,
  deleteExpense,
  getBudget,
  getExpensesForBudget,
} from "../api";
import { calculateSpentByBudget, generateRandomColor } from "../helpers";

// components
import Intro from "../components/Intro";
import AddBudgetForm from "../components/AddBudgetForm";
import AddExpenseForm from "../components/AddExpenseForm";
import BudgetItem from "../components/BudgetItem";
import Table from "../components/Table";

// ✅ Refactored Loader
export async function dashboardLoader() {
  const userName = JSON.parse(localStorage.getItem("userName"));

  // If no user, no need to fetch data
  if (!userName) {
    return { userName: null, budgets: [], expenses: [] };
  }

  try {
    // ✅ Fetch budgets and expenses from API in parallel
    const [budgetsRes, expensesRes] = await Promise.all([
      getBudgets(),
      getExpenses(),
    ]);

    const budgets = budgetsRes.data || [];
    const expenses = expensesRes.data || [];

    // ✅ PRE-CALCULATE the 'spent' amount for each budget
    const budgetsWithSpent = budgets.map((budget) => {
      const spent = calculateSpentByBudget(budget.id, expenses);
      // ✅ Attach 'spent' to the budget object
      return { ...budget, spent, color: generateRandomColor() }; // Also add color
    });

    return { userName, budgets: budgetsWithSpent, expenses };
  } catch (error) {
    console.error("Dashboard loader error:", error);
    // Return empty state on error so app doesn't crash
    return { userName, budgets: [], expenses: [] };
  }
}

// ✅ Refactored Action
export async function dashboardAction({ request }) {
  const data = await request.formData();
  const { _action, ...values } = Object.fromEntries(data);

  if (_action === "createBudget") {
    try {
      const newBudget = {
        name: values.newBudget,
        amount: parseFloat(values.newBudgetAmount),
      };
      await createBudget(newBudget); // ✅ Use API
      toast.success(`Budget "${newBudget.name}" created!`);
    } catch (e) {
      toast.error("There was a problem creating your budget.");
    }
  }

  // ✅ MODIFIED THIS BLOCK
  if (_action === "createExpense") {
    try {
      const newExpense = {
        name: values.newExpense,
        amount: parseFloat(values.newExpenseAmount),
        budgetId: values.newExpenseBudget,
      };

      // --- START VALIDATION ---
      // 1. Fetch the budget details to get its limit
      const budgetRes = await getBudget(newExpense.budgetId);
      const budgetAmount = budgetRes.data.amount;

      // 2. Fetch the *current* expenses for this budget
      const expensesRes = await getExpensesForBudget(newExpense.budgetId);

      // 3. Calculate how much has already been spent
      const spent = calculateSpentByBudget(
        newExpense.budgetId,
        expensesRes.data
      );

      // 4. This is the check!
      if (newExpense.amount + spent > budgetAmount) {
        return toast.warn(
          `Adding this expense will exceed your "${budgetRes.data.name}" budget!`
        );
      }
      // --- END VALIDATION ---

      // Only create if the check passes
      await createExpense(newExpense);
      toast.success(`Expense "${newExpense.name}" created!`);
    } catch (e) {
      toast.error("There was a problem creating your expense.");
    }
  }

  if (_action === "deleteExpense") {
    try {
      await deleteExpense(values.expenseId); // ✅ Use API
      return toast.success("Expense deleted!");
    } catch (e) {
      // We throw an error here to send the user to the Error page
      throw new Error("There was a problem deleting your expense.");
    }
  }

  return null; // Stay on the page
}

// ✅ Component (mostly unchanged, just gets 'userName' from loader)
const Dashboard = () => {
  const { userName, budgets, expenses } = useLoaderData();

  return (
    <>
      {userName ? (
        <div className="dashboard">
          <h1>
            Welcome back, <span className="accent">{userName}</span>
          </h1>
          <div className="grid-sm">
            {budgets && budgets.length > 0 ? (
              <div className="grid-lg">
                <div className="flex-lg">
                  <AddBudgetForm />
                  <AddExpenseForm budgets={budgets} />
                </div>
                <h2>Existing Budgets</h2>
                <div className="budgets">
                  {budgets.map((budget) => (
                    // ✅ BudgetItem now receives 'budget.spent'
                    <BudgetItem key={budget.id} budget={budget} />
                  ))}
                </div>
                {expenses && expenses.length > 0 && (
                  <div className="grid-md">
                    <h2>Recent Expenses</h2>
                    {/* ✅ Pass budgets to Table for name lookups */}
                    <Table
                      expenses={expenses
                        .sort((a, b) => b.createdAt - a.createdAt)
                        .slice(0, 8)}
                      budgets={budgets}
                    />
                    {expenses.length > 8 && (
                      <Link to="expenses" className="btn btn--dark">
                        View all expenses
                      </Link>
                    )}
                  </div>
                )}
              </div>
            ) : (
              <div className="grid-sm">
                <p>Personal budgeting is the secret to financial freedom.</p>
                <p>Create a budget to get started!</p>
                <AddBudgetForm />
              </div>
            )}
          </div>
        </div>
      ) : (
        <Intro />
      )}
    </>
  );
};
export default Dashboard;