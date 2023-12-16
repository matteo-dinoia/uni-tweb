import "./Characters.css";
import {FC, useState} from "react";
import edit from "../assets/edit.svg";
import remove from "../assets/remove.svg";
import {CharacterI} from "../data/rpgpc.ts";
import {AddChar} from "../addchar/AddChar.tsx";

interface CharElemPropsI {
    name: string;
    rpgclass: string;
    level: number;
    onRemove: () => void;
    onEdit: () => void;
}

const CharElem: FC<CharElemPropsI> = ({
                                          name,
                                          rpgclass,
                                          level,
                                          onRemove,
                                          onEdit
                                      }) => {
    return (<tr>
        <td>{name}</td>
        <td>{rpgclass}</td>
        <td>{level}</td>
        <td><img className="editchar"
                 src={edit}
                 onClick={() => onEdit()}/></td>
        <td><img className="deletechar"
                 src={remove}
                 onClick={() => onRemove()}/></td>
    </tr>);
}

interface CharactersPropI {
    characters: CharacterI[];
    onRemoveCharacter: (id: number) => void;
    onNewCharacter: (c: CharacterI) => void;
    onEditCharacter: (c: CharacterI) => void;
}

export const Characters: FC<CharactersPropI> = ({
                                                    characters,
                                                    onRemoveCharacter,
                                                    onEditCharacter,
                                                    onNewCharacter
                                                }) => {
    const [showNewCharForm, setShowNewCharForm] = useState(false);
    const [showEditCharForm, setShowEditCharForm] = useState(-1);
    const charRowElems = characters.map((c) =>
        <CharElem name={c.name}
                  level={c.level}
                  rpgclass={c.rpgclass}
                  onRemove={() => onRemoveCharacter(c.id)}
                  onEdit={() => setShowEditCharForm(c.id)}/>);
    return (<>
        <div className="characters sidebox">
            <h2>Your characters:</h2>
            <table className="chartable">
                <thead>
                <tr className="header">
                    <th>Name</th>
                    <th>Class</th>
                    <th>Level</th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                {charRowElems}
                </tbody>
            </table>
            <div className="charactions">
                <div className="pulsante"
                     onClick={() => setShowNewCharForm(true)}>New character...
                </div>
            </div>
        </div>
        {showNewCharForm && <AddChar title={"Create new character"}
                                     actionText={"Create"}
                                     onCancel={() => setShowNewCharForm(false)}
                                     onAction={(c) => {
                                         setShowNewCharForm(false);
                                         onNewCharacter(c);
                                     }}/>}
        {showEditCharForm >= 0 && <AddChar title={"Edit character"}
                                           actionText={"Apply"}
                                           onCancel={() => setShowEditCharForm(-1)}
                                           onAction={(c) => {
                                               setShowEditCharForm(-1);
                                               onEditCharacter(c);
                                           }}
                                           initChar={characters.find(c => c.id === showEditCharForm)}/>}
    </>);
}