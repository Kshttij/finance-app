import { Form } from "react-router-dom"
import { Link } from "react-router-dom";
import illustration from "../assets/illustration.jpg";
// library
import { UserPlusIcon } from "@heroicons/react/24/solid";

const Intro = () => {
  return (
    <div className="intro">
      <div>
        <h1>
          Take Control of <span className="accent">Your Money</span>
        </h1>
        <p>Personal budgeting is the secret to financial freedom. Start your journey today.</p>

        <Link to="/login" className="btn btn--dark">
          <span>Get Started</span>
        </Link>
      </div>
      <img src={illustration} alt="Person with money" width={600} />
    </div>
  );
};
export default Intro