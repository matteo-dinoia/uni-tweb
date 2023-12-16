import "./TopBar.css";
import menubars from "../assets/menubars.svg";
import user from "../assets/user.svg";
import {FC} from "react";

export const TopBar: FC = () => {
    return (<section className="topbar">
        <h1>RPG Party Creator</h1>
        <nav className="topmenu">
            <div className="actions"><img src={menubars}/></div>
            <div className="user"><img src={user}/></div>
        </nav>
    </section>);
}