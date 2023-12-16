import "./AddParty.css";
import {FC, useState} from "react";
import {PartyI} from "../data/rpgpc.ts";

interface AddPartyPropsI {
    title: string;
    actionText: string;
    initParty?: PartyI;
    onCancel: () => void;
    onAction: (p: PartyI) => void;
}

export const AddParty: FC<AddPartyPropsI> = ({
                                                 title,
                                                 actionText,
                                                 onCancel,
                                                 onAction,
                                                 initParty
                                             }) => {
    const [value, setValue] =
        useState<{ name: string; shortname: string }>({
            name: (initParty === undefined ? "" : initParty.name),
            shortname: (initParty === undefined ? "" : initParty.shortname)
        })
    return (<div className="glasspane"
                 onClick={(e) => {
                     if (e.target === e.currentTarget) onCancel();
                 }}>
        <div className="modal addparty">
            <h2 className="modaltitle">{title}</h2>
            <form className="modalform">
                <div className="v_formgroup name">
                    <label htmlFor="name">Name</label>
                    <input type="text"
                           id="name"
                           name="party_name"
                           value={value.name}
                           onChange={(e) => {
                               setValue({
                                   ...value,
                                   name: e.target.value
                               })
                           }}/>
                </div>
                <div className="v_formgroup shortname">
                    <label htmlFor="shortname">Shortname: only lowcase [a-z], hyphen (-) and underscore (_)
                        allowed</label>
                    <input type="text"
                           id="shortname"
                           name="party_shortname"
                           value={value.shortname}
                           onChange={(e) => {
                               setValue({
                                   ...value,
                                   shortname: e.target.value
                               })
                           }}/>
                </div>
                <div className="formaction">
                    <div className="pulsante"
                         onClick={() => {
                             const p: PartyI = {
                                 ...value
                             }
                             onAction(p);
                         }}>{actionText}
                    </div>
                </div>
            </form>
        </div>
    </div>);
}