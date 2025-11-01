// src/pages/BudgetPage.jsx
import { useLoaderData } from "react-router-dom";
import { toast } from "react-toastify";

// ✅ Import the helper we need for validation
import {
  createExpense,
  deleteExpense,
  getBudget,
  getExpensesForBudget,
} from "../api";
import {
  calculateSpentByBudget, // <-- ADDED
  generateRandomColor,
} from "../helpers";

// components
import AddExpenseForm from "../components/AddExpenseForm";
import BudgetItem from "../components/BudgetItem";
import Table from "../components/Table";

// ✅ Refactored Loader
export async function budgetLoader({ params }) {
  try {
    // ✅ Fetch the specific budget and its expenses in parallel
    const [budgetRes, expensesRes] = await Promise.all([
      getBudget(params.id),
      getExpensesForBudget(params.id), // Assumes API: /api/expenses?budgetId=...
    ]);

    if (!budgetRes.data) {
      throw new Error("The budget you’re trying to find doesn’t exist");
    }

    const budget = budgetRes.data;
    const expenses = expensesRes.data || [];

    // ✅ PRE-CALCULATE spent amount
    const spent = calculateSpentByBudget(budget.id, expenses);
    const budgetWithSpent = {
      ...budget,
      spent,
      color: generateRandomColor(), // Add color
    };

    return { budget: budgetWithSpent, expenses };
  } catch (e) {
    throw new Error("There was a problem loading your budget.");
  }
}

// ✅ MODIFIED Action
export async function budgetAction({ request }) {
  const data = await request.formData();
  const { _action, ...values } = Object.fromEntries(data);

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
      return toast.success(`Expense ${values.newExpense} created!`);
    } catch (e) {
      throw new Error("There was a problem creating your expense.");
    }
  }

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

const BudgetPage = () => {
  const { budget, expenses } = useLoaderData();

  return (
    <div
      className="grid-lg"
      style={{
        "--accent": budget.color,
      }}
    >
      <h1 className="h2">
        <span className="accent">{budget.name}</span> Overview
      </h1>
      <div className="flex-lg">
        {/* ✅ BudgetItem receives 'budget.spent' */}
        <BudgetItem budget={budget} showDelete={true} />
        <AddExpenseForm budgets={[budget]} />
      </div>
      {expenses && expenses.length > 0 && (
        <div className="grid-md">
          <h2>
            <span className="accent">{budget.name}</span> Expenses
          </h2>
          {/* ✅ This is fine. showBudget=false means ExpenseItem won't need the 'budgets' prop */}
          <Table expenses={expenses} showBudget={false} />
        </div>
      )}
    </div>
  );
};
export default BudgetPage;