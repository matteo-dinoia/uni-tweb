import "./CharDesc.css";
import {FC} from "react";

interface CharDescPropsI {
    name: string;
    description: string[]
}

export const CharDesc: FC<CharDescPropsI> = ({name, description}) => {
    const descPars = description.map((s) => <p>{s}</p>);
    return (<div className="chardesc">
        <h3>{name}</h3>
        {descPars}
    </div>);
}