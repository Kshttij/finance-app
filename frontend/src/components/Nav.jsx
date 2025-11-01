// src/components/Nav.jsx
import { Form, NavLink } from "react-router-dom";
import { TrashIcon } from '@heroicons/react/24/solid';
import logomark from "../assets/logomark.svg";

const Nav = ({ userName }) => {
  return (
    <nav>
      <NavLink
        to="/"
        aria-label="Go to home"
      >
        <img src={logomark} alt="" height={30} />
        <span>HomeBudget</span>
      </NavLink>
      {
        userName && (
          <Form
            method="post"
            action="logout" // ✅ Points to our new logoutAction
            onSubmit={(event) => {
              // ✅ Changed confirmation text to be accurate
              if (!confirm("Are you sure you want to log out?")) {
                event.preventDefault()
              }
            }}
          >
            <button type="submit" className="btn btn--warning">
              {/* ✅ Changed button text */}
              <span>Logout</span>
              <TrashIcon width={20} />
            </button>
          </Form>
        )
      }
    </nav>
  )
}
export default Nav;