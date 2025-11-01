// src/actions/logout.js
import { redirect } from "react-router-dom";
import { toast } from "react-toastify";

export async function logoutAction() {
  // ✅ Clear auth data from localStorage
  localStorage.removeItem("token");
  localStorage.removeItem("userName");
  
  // ❌ Don't delete budgets/expenses, that's just cache
  // deleteItem({ key: "budgets" })
  // deleteItem({ key: "expenses" })

  toast.success("You've been logged out!");
  return redirect("/"); // Redirect to public home page
}