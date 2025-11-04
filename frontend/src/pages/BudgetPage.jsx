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

// ✅ Loader (No changes)
export async function budgetLoader({ params }) {
  try {
    const [budgetRes, expensesRes] = await Promise.all([
      getBudget(params.id),
      getExpensesForBudget(params.id), 
    ]);

    if (!budgetRes.data) {
      throw new Error("The budget you’re trying to find doesn’t exist");
    }

    const budget = budgetRes.data;
    const expenses = expensesRes.data || [];

    const spent = calculateSpentByBudget(budget.id, expenses);
    const budgetWithSpent = {
      ...budget,
      spent,
      color: generateRandomColor(),
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
        // --- THIS IS THE FIX ---
        // We must also pass the category from the form,
        // just like on the dashboard.
        category: values.newExpenseCategory,
      };

      // --- START VALIDATION ---
      // (This logic is great, no changes)
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

      // Only create if the check passes
      await createExpense(newExpense);
      return toast.success(`Expense ${values.newExpense} created!`);
    } catch (e) {
      throw new Error("There was a problem creating your expense.");
    }
  }

  if (_action === "deleteExpense") {
    // (No changes here)
    try {
      await deleteExpense(values.expenseId); // ✅ Use API
      return toast.success("Expense deleted!");
    } catch (e) {
      throw new Error("There was a problem deleting your expense.");
    }
  }
  return null;
}

// Component (No changes)
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
        <BudgetItem budget={budget} showDelete={true} />
        <AddExpenseForm budgets={[budget]} />
      </div>
      {expenses && expenses.length > 0 && (
        <div className="grid-md">
          <h2>
            <span className="accent">{budget.name}</span> Expenses
          </h2>
          <Table expenses={expenses} showBudget={false} />
        </div>
      )}
    </div>
  );
};
export default BudgetPage;
