import "./Parties.css";
import {FC, useEffect, useState} from "react";
import edit from "../assets/edit.svg";
import remove from "../assets/remove.svg";
import {PartyI} from "../data/rpgpc.ts";
import {AddParty} from "../addparty/AddParty.tsx";

interface PartyElemPropsI {
    name: string;
    selected: boolean;
    onSelected: () => void;
    onDeleted: () => void;
    onEdit: () => void;
}

const PartyElem: FC<PartyElemPropsI> = ({
                                            name,
                                            selected,
                                            onSelected,
                                            onDeleted,
                                            onEdit
                                        }) => {
    return (<li className={(selected ? "selected" : "")}
                onClick={() => onSelected()}>
        <span>{name}</span>
        {selected && <img className="editparty"
                          src={edit}
                          onClick={(e) => {
                              e.stopPropagation();
                              onEdit();
                          }}/>}
        {selected && <img className="deleteparty"
                          src={remove}
                          onClick={(e) => {
                              e.stopPropagation();
                              onDeleted();
                          }}/>}
    </li>);
}

interface PartiesPropsI {
    selectedParty: string
    onPartySelected: (shortname: string) => void;
}

export const Parties: FC<PartiesPropsI> = ({selectedParty, onPartySelected}) => {
    const [parties, setParties] = useState<PartyI[]>([]);
    useEffect(() => {
        let ignore = false;
        fetch("http://localhost:8080/rpgpc-gson-base/parties")
            .then(response => response.json())
            .then((data: PartyI[]) => {
                if (!ignore) setParties(data);
            });
        return () => { ignore = true }
    }, [])
    const [showNewPartyForm, setShowNewPartyForm]
        = useState(false);
    const [showEditPartyForm, setShowEditPartyForm]
        = useState(false);
    const partyListElems = parties.map((p) => <PartyElem
        onSelected={() => onPartySelected(p.shortname)}
        name={p.name}
        selected={(p.shortname === selectedParty)}
        onDeleted={() => {
            const upd = parties.filter(x => x.shortname !== p.shortname);
            setParties(upd);
            if (selectedParty === p.shortname) onPartySelected("");
        }}
        onEdit={() => {
            setShowEditPartyForm(true);
        }}/>);
    const selP = parties.find(p => p.shortname === selectedParty);

    return (<div className="parties sidebox">
        <h2>Your parties:</h2>
        <ul className="partylist">
            {partyListElems}
        </ul>
        <div className="partiesactions">
            <div className="pulsante"
                 onClick={() => setShowNewPartyForm(true)}>New party...
            </div>
        </div>
        {showNewPartyForm && <AddParty onCancel={() => {
            setShowNewPartyForm(false);
        }}
                                       onAction={(p: PartyI) => {
                                           setShowNewPartyForm(false);
                                           setParties([...parties, p]);
                                       }}
                                       title={"Create new party"}
                                       actionText={"Create"}/>}
        {showEditPartyForm && selP !== undefined &&
            <AddParty initParty={selP}
                      onCancel={() => {
                          setShowEditPartyForm(false);
                      }}
                      onAction={(x: PartyI) => {
                          setShowEditPartyForm(false);
                          const nuovo = parties.map(p => {
                              if (p.shortname === selectedParty) {
                                  p.name = x.name;
                                  p.shortname = x.shortname;
                                  return p;
                              }
                              return p;
                          });
                          setParties(nuovo);
                      }}
                      title={"Edit party"}
                      actionText={"Apply"}/>}
    </div>);
}