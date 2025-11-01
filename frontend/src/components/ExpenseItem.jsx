// src/components/ExpenseItem.jsx
import { Link, useFetcher } from "react-router-dom";
import { TrashIcon } from "@heroicons/react/24/solid";

// ✅ Import only formatting helpers
import {
  formatCurrency,
  formatDateToLocaleString,
} from "../helpers";

// ✅ Component now accepts 'budgets' array as a prop
const ExpenseItem = ({ expense, showBudget, budgets }) => {
  const fetcher = useFetcher();

  // ✅ Find the budget name from the 'budgets' prop array
  // A fallback is added in case budgets are not passed
  const budget = budgets?.find((b) => b.id === expense.budgetId) || {
    id: "#",
    name: "N/A",
    color: "var(--dark)",
  };

  // ❌ const budget = getAllMatchingItems(...) // <-- No longer needed!

  return (
    <>
      <td>{expense.name}</td>
      <td>{formatCurrency(expense.amount)}</td>
      <td>{formatDateToLocaleString(expense.createdAt)}</td>
      {showBudget && (
        <td>
          <Link
            to={`/budget/${budget.id}`}
            style={{
              "--accent": budget.color,
            }}
          >
            {budget.name}
          </Link>
        </td>
      )}
      <td>
        <fetcher.Form method="post">
          <input type="hidden" name="_action" value="deleteExpense" />
          <input type="hidden" name="expenseId" value={expense.id} />
          <button
            type="submit"
            className="btn btn--warning"
            aria-label={`Delete ${expense.name} expense`}
          >
            <TrashIcon width={20} />
          </button>
        </fetcher.Form>
      </td>
    </>
  );
};
export default ExpenseItem;