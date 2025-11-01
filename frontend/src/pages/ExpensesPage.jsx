// src/pages/ExpensesPage.jsx
import { useLoaderData } from "react-router-dom";
import { toast } from "react-toastify";

// ✅ Import API
import { deleteExpense, getBudgets, getExpenses } from "../api";

// components
import Table from "../components/Table";

// ✅ Refactored Loader
export async function expensesLoader() {
  try {
    // ✅ Fetch expenses AND budgets (for name lookups in the table)
    const [expensesRes, budgetsRes] = await Promise.all([
      getExpenses(),
      getBudgets()
    ]);
    
    const expenses = expensesRes.data || [];
    const budgets = budgetsRes.data || [];

    return { expenses, budgets };
  } catch (error) {
    console.error("Expenses loader error:", error);
    return { expenses: [], budgets: [] };
  }
}

// ✅ Refactored Action
export async function expensesAction({ request }) {
  const data = await request.formData();
  const { _action, ...values } = Object.fromEntries(data);

  if (_action === "deleteExpense") {
    try {
      await deleteExpense(values.expenseId); // ✅ Use API
      return toast.success("Expense deleted!");
    } catch (e) {
      throw new Error("There was a problem deleting your expense.");
    }
  }
  return null;
}

const ExpensesPage = () => {
  // ✅ Get both expenses and budgets from loader
  const { expenses, budgets } = useLoaderData();

  return (
    <div className="grid-lg">
      <h1>All Expenses</h1>
      {expenses && expenses.length > 0 ? (
        <div className="grid-md">
          <h2>
            Recent Expenses <small>({expenses.length} total)</small>
          </h2>
          {/* ✅ Pass budgets to the Table */}
          <Table expenses={expenses} budgets={budgets} />
        </div>
      ) : (
        <p>No Expenses to show</p>
      )}
    </div>
  );
};

export default ExpensesPage;