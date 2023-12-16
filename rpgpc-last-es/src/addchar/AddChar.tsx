import "./AddChar.css";
import {FC, useState} from "react";
import {CharacterI} from "../data/rpgpc.ts";

interface AddCharPropsI {
    title: string;
    actionText: string;
    initChar?: CharacterI;
    onCancel: () => void;
    onAction: (c: CharacterI) => void;
}

export const AddChar: FC<AddCharPropsI> = ({
                                               title,
                                               actionText,
                                               initChar,
                                               onCancel,
                                               onAction
                                           }) => {
    const [value, setValue] =
        useState<CharacterI>((initChar === undefined ? {
            id: -1,
            name: "",
            rpgclass: "",
            level: 0,
            description: []

        } : {...initChar}));
    return (<div className="glasspane"
                 onClick={(e) => {
                     if (e.target === e.currentTarget) onCancel();
                 }}>
        <div className="modal addchar">
            <h2 className="modaltitle">{title}</h2>
            <form className="modalform">
                <div className="h_formgroup name">
                    <label htmlFor="name">Name</label>
                    <input type="text"
                           id="name"
                           name="character_name"
                           value={value.name}
                           onChange={(e) => {
                               setValue({...value, name: e.target.value})
                           }}/>
                </div>
                <div className="h_formgroup rpgclass">
                    <label htmlFor="rpgclass">Class</label>
                    <input type="text"
                           id="rpgclass"
                           name="character_rpg_class"
                           value={value.rpgclass}
                           onChange={(e) => {
                               setValue({...value, rpgclass: e.target.value})
                           }}/>
                </div>
                <div className="h_formgroup level">
                    <label htmlFor="level">Level</label>
                    <input type="number"
                           id="level"
                           name="character_level"
                           value={value.level}
                           onChange={(e) => {
                               const level = parseInt(e.target.value);
                               setValue({...value, level: (isNaN(level)? -1 : level)})
                           }}/>
                </div>
                <div className="v_formgroup desc">
                    <label htmlFor="desc">Description</label>
                    <textarea id="desc"
                              name="character_description"
                              value={value.description.join("\n")}
                              onChange={(e) => {
                                  setValue({
                                      ...value,
                                      description: e.target.value.split("\n")
                                  })
                              }}></textarea>
                </div>
                <div className="formaction">
                    <div className={"pulsante"}
                         onClick={() => {
                             onAction(value);
                         }}>{actionText}</div>
                </div>
            </form>
        </div>
    </div>)
}