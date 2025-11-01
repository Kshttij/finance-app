// src/layouts/Main.jsx
import { Outlet, useLoaderData } from "react-router-dom";

// components
import Nav from "../components/Nav";

// assets
import wave from "../assets/wave.svg";

// ✅ Main loader to get username for the Nav
export function mainLoader() {
  // We still get userName from localStorage, as set by AuthPage
  const userName = JSON.parse(localStorage.getItem("userName"));
  return { userName };
}

const Main = () => {
  const { userName } = useLoaderData();

  return (
    <div className="layout">
      {/* ✅ Pass userName to the Nav */}
      <Nav userName={userName} />
      <main>
        <Outlet />
      </main>
      <img src={wave} alt="" />
    </div>
  );
};
export default Main;