import "./PartyEdit.css";
import {FC, useState} from "react";
import {CharacterI} from "../../data/rpgpc.ts";
import {AddChar} from "../../addchar/AddChar.tsx";

interface PartyEditPropsI {
    notYetMembers: CharacterI[]
    onAdd: (c: CharacterI) => void;
    onAddNew: (c: CharacterI) => void;
}

export  const PartyEdit:FC<PartyEditPropsI> = ({notYetMembers, onAdd, onAddNew}) => {
    const [selectedChar, setSelectedChar] = useState(-1);
    const [showNewCharForm, setShowNewCharForm] = useState(false);
    const optionList = [<option value={-1}>--- Select character ---</option>, ...notYetMembers.map((c) =>
        <option value={c.id}>{c.name}</option>)];
    return (<><div className="partyedit">
        <h2>Add member:</h2>
        <form className="addchar">
            <select name="add_character" value={selectedChar}
            onChange={(e) => setSelectedChar(parseInt(e.target.value))}>
                {optionList}
            </select>
            <div className="pulsante"
            onClick={() => {
                if (selectedChar >= 0) {
                    const c = notYetMembers.find(x => x.id === selectedChar);
                    if (c !== undefined) onAdd(c);
                }
            }}>Add</div>
        </form>
        <div className="membersactions">
            <div className="pulsante" onClick={() => setShowNewCharForm(true)}>Add new...</div>
        </div>
    </div>
        {showNewCharForm && <AddChar title={"Add new character to party"}
                                     actionText={"Create & Add"}
                                     onCancel={() => {setShowNewCharForm(false)}}
                                     onAction={(c) => {
                                         setShowNewCharForm(false);
                                         onAddNew(c);
                                     }}/>}
    </>);
}