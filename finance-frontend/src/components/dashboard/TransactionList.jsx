import { useEffect, useState } from "react";
import { getTransactionsInRange, deleteTransaction } from "../../services/api";

export default function TransactionList({ userId, refreshFlag, onTransactionDeleted, dateRange }) {
  const [transactions, setTransactions] = useState([]);

  const fetchTransactions = async () => {
    if (!dateRange?.start || !dateRange?.end) {
      setTransactions([]);
      return;
    }

    const data = await getTransactionsInRange(
      userId,
      dateRange.start.toISOString(),
      dateRange.end.toISOString()
    );
    setTransactions(data || []);
  };

  useEffect(() => {
    fetchTransactions();
  }, [userId, refreshFlag, dateRange.start, dateRange.end]);

  const handleDelete = async (txId) => {
    const success = await deleteTransaction(userId, txId);
    if (success) {
      fetchTransactions();
      if (onTransactionDeleted) onTransactionDeleted();
    }
  };

  return (
    <div>
      <h2>Transactions</h2>
      <table border="1" width="100%" cellPadding="5">
        <thead>
          <tr>
            <th>Type</th>
            <th>Category</th>
            <th>Amount</th>
            <th>Description</th>
            <th>Date</th>
            <th>Delete</th>
          </tr>
        </thead>
        <tbody>
          {transactions.length > 0 ? (
            transactions.map(tx => (
              <tr key={tx.id}>
                <td>{tx.type}</td>
                <td>{tx.category?.name || "N/A"}</td>
                <td>{tx.amount}</td>
                <td>{tx.description}</td>
                <td>{new Date(tx.date || tx.timestamp).toLocaleString()}</td>
                <td>
                  <button onClick={() => handleDelete(tx.id)}>Delete</button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="6" style={{ textAlign: "center" }}>No transactions in this range</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
