import { useState, useEffect } from "react";
import { getCategoriesByType, createTransaction } from "../../services/api";

export default function TransactionForm({ userId, onTransactionCreated }) {
  const [type, setType] = useState("INCOME");
  const [categoryId, setCategoryId] = useState("");
  const [categories, setCategories] = useState([]);
  const [amount, setAmount] = useState("");
  const [description, setDescription] = useState("");
  const [date, setDate] = useState(new Date().toISOString().slice(0, 16));

  useEffect(() => {
    const fetchCategories = async () => {
      const data = await getCategoriesByType(type);
      setCategories(data);
      setCategoryId(data.length > 0 ? data[0].id : "");
    };
    fetchCategories();
  }, [type]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const success = await createTransaction(userId, {
      type,
      categoryId,
      amount: parseFloat(amount),
      description,
      date: new Date(date).toISOString()
    });

    if (success) {
      setAmount("");
      setDescription("");
      setDate(new Date().toISOString().slice(0, 16));

      if (onTransactionCreated) onTransactionCreated();
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginBottom: "20px" }}>
      <select value={type} onChange={(e) => setType(e.target.value)}>
        <option value="INCOME">Income</option>
        <option value="EXPENSE">Expense</option>
      </select>

      <select value={categoryId} onChange={(e) => setCategoryId(e.target.value)}>
        {categories.map(cat => (
          <option key={cat.id} value={cat.id}>{cat.name}</option>
        ))}
      </select>

      <input
        type="number"
        placeholder="Amount"
        value={amount}
        onChange={(e) => setAmount(e.target.value)}
        required
      />

      <input
        type="text"
        placeholder="Description"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
      />

      <input
        type="datetime-local"
        value={date}
        onChange={(e) => setDate(e.target.value)}
      />

      <button type="submit">Add Transaction</button>
    </form>
  );
}
