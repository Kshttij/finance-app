// src/actions/deleteBudget.js
import { redirect } from "react-router-dom";
import { toast } from "react-toastify";

// ✅ Import API
import { deleteBudget as apiDeleteBudget } from "../api";

export async function deleteBudget({ params }) {
  try {
    await apiDeleteBudget(params.id); // ✅ Use API

    // ❌ No need to delete associated expenses from frontend.
    // The backend should handle this (e.g., cascading delete).
    
    toast.success("Budget deleted successfully!");
  } catch (e) {
    throw new Error("There was a problem deleting your budget.");
  }
  return redirect("/"); // Redirect to dashboard
}