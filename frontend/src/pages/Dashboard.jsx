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
  getSpendingSummary, 
} from "../api";
import { calculateSpentByBudget, generateRandomColor } from "../helpers";

// components
import Intro from "../components/Intro";
import AddBudgetForm from "../components/AddBudgetForm";
import AddExpenseForm from "../components/AddExpenseForm";
import BudgetItem from "../components/BudgetItem";
import Table from "../components/Table";
import CategoryChart from '../components/CategoryChart'; // --- NEW --- Import the chart

// ✅ Loader (no changes)
export async function dashboardLoader() {
  const userName = JSON.parse(localStorage.getItem("userName"));

  if (!userName) {
    return { userName: null, budgets: [], expenses: [], summaryData: [] }; 
  }

  try {
    const [budgetsRes, expensesRes, summaryRes] = await Promise.all([
      getBudgets(),
      getExpenses(),
      getSpendingSummary(), 
    ]);

    const budgets = budgetsRes.data || [];
    const expenses = expensesRes.data || [];
    const summaryData = summaryRes.data || []; 

    const budgetsWithSpent = budgets.map((budget) => {
      const spent = calculateSpentByBudget(budget.id, expenses);
      return { ...budget, spent, color: generateRandomColor() }; 
    });

    return { userName, budgets: budgetsWithSpent, expenses, summaryData };
  } catch (error) {
    console.error("Dashboard loader error:", error);
    return { userName, budgets: [], expenses: [], summaryData: [] }; 
  }
}

// ✅ Action (no changes)
export async function dashboardAction({ request }) {
  const data = await request.formData();
  const { _action, ...values } = Object.fromEntries(data);

  if (_action === "createBudget") {
    try {
      const newBudget = {
        name: values.newBudget,
        amount: parseFloat(values.newBudgetAmount),
      };
      await createBudget(newBudget); 
      toast.success(`Budget "${newBudget.name}" created!`);
    } catch (e) {
      toast.error("There was a problem creating your budget.");
    }
  }

  if (_action === "createExpense") {
    try {
      const newExpense = {
        name: values.newExpense,
        amount: parseFloat(values.newExpenseAmount),
        budgetId: values.newExpenseBudget,
        category: values.newExpenseCategory,
      };

      // --- START VALIDATION ---
      const budgetRes = await getBudget(newExpense.budgetId);
      const budgetAmount = budgetRes.data.amount;
      const expensesRes = await getExpensesForBudget(newExpense.budgetId);
      const spent = calculateSpentByBudget(
        newExpense.budgetId,
        expensesRes.data
      );
      if (newExpense.amount + spent > budgetAmount) {
        return toast.warn(
          `Adding this expense will exceed your "${budgetRes.data.name}" budget!`
        );
      }
      // --- END VALIDATION ---

      await createExpense(newExpense);
      toast.success(`Expense "${newExpense.name}" created!`);
    } catch (e) {
      toast.error("There was a problem creating your expense.");
    }
  }

  if (_action === "deleteExpense") {
    try {
      await deleteExpense(values.expenseId); 
      return toast.success("Expense deleted!");
    } catch (e) {
      throw new Error("There was a problem deleting your expense.");
    }
  }

  return null; 
}


const Dashboard = () => {
  const { userName, budgets, expenses, summaryData } = useLoaderData();

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
                    <BudgetItem key={budget.id} budget={budget} />
                  ))}
                </div>
                
                {/* --- UPDATED BLOCK --- */}
                <div className="grid-lg"> 
                  {summaryData && summaryData.length > 0 && (
                    <div className="grid-md">
                      <h2>Spending by Category</h2>
                      {/* We replaced the old progress bars with our new chart! */}
                      <CategoryChart summaryData={summaryData} />
                    </div>
                  )}

                  {expenses && expenses.length > 0 && (
                    <div className="grid-md">
                      <h2>Recent Expenses</h2>
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
                {/* --- END UPDATED BLOCK --- */}

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
