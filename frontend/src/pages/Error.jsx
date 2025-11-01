// src/pages/Error.jsx
import { useRouteError, Link, useNavigate } from "react-router-dom"

// library imports
import { HomeIcon, ArrowUturnLeftIcon } from "@heroicons/react/24/solid"

const Error = () => {
  const error = useRouteError();
  const navigate = useNavigate();

  return (
    <div className="error">
      <h1>Uh oh! We’ve got a problem.</h1>
      <p>{error.message || error.statusText}</p>
      <div className="flex-md">
        <button
          className="btn btn--dark"
          onClick={() => navigate(-1)} // This still goes back one page
        >
          <ArrowUturnLeftIcon width={20} />
          <span>Go Back</span>
        </button>
        
        {/* ✅ This link now points to the login page */}
        <Link
          to="/login" 
          className="btn btn--dark"
        >
          <HomeIcon width={20} />
          <span>Go to Login</span>
        </Link>
      </div>
    </div>
  )
}
export default Error