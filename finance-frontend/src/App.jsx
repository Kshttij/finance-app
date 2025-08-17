import { useState } from "react";
import Signup from "./components/Signup";
import Login from "./components/Login";
import Dashboard from "./components/dashboard/Dashboard";

export default function App() {
  const [userId, setUserId] = useState(null);

  return (
    <div style={{ fontFamily: "Arial, sans-serif", padding: "20px" }}>
      {!userId ? (
        <div style={{ maxWidth: "400px", margin: "0 auto" }}>
          <h1 style={{ textAlign: "center" }}>Finance App</h1>
          <Signup setUserId={setUserId} />
          <hr style={{ margin: "20px 0" }} />
          <Login setUserId={setUserId} />
        </div>
      ) : (
        <Dashboard userId={userId} />
      )}
    </div>
  );
}
