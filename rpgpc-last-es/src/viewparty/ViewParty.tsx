import "./ViewParty.css";
import {FC, useEffect, useState} from "react";
import {PartyMembers} from "./partymembers/PartyMembers.tsx";
import {PartyEdit} from "./partyedit/PartyEdit.tsx";
import {CharDesc} from "./chardesc/CharDesc.tsx";
import {CharacterI, PartyI} from "../data/rpgpc.ts";

interface ViewPartyPropsI {
    selectedParty: string
    characters: CharacterI[]
    onNewCharacter: (c: CharacterI) => void;
}

export const ViewParty: FC<ViewPartyPropsI> = ({
                                                   selectedParty,
                                                   characters,
                                                   onNewCharacter
                                               }) => {
    const [theParty, setTheParty] = useState<PartyI | undefined>(undefined);
    useEffect(() => {
        let ignore = false;
        if (selectedParty === "") {
            setTheParty(undefined);
        } else {
            fetch("http://localhost:8080/rpgpc-gson-base/parties?shortname=" + selectedParty)
                .then(response => response.json())
                .then((data: PartyI) => {
                    if (!ignore) setTheParty(data)
                });
        }
        return () => {ignore = true};
    }, [selectedParty]);
    const members: CharacterI[] = [];
    const notMembers: CharacterI[] = [];
    if (theParty !== undefined && theParty.characters !== undefined) {
        for (const c of characters) {
            const index = theParty.characters.findIndex(m => m === c.id);
            if (index >= 0) members.push(c);
            else notMembers.push(c);
        }
    }

    const name = (theParty === undefined ? "" : theParty.name);
    const [selectedMemberId, setSelectedMemberId] = useState(-1);
    const selectedMember = members.find(c => c.id === selectedMemberId);

    return (<div className="viewparty contentbox">
        {theParty !== undefined && <h1 className="partyname">{name}</h1>}
        {theParty !== undefined && <PartyMembers members={members}
                                                 selectedMember={selectedMemberId}
                                                 onSelect={setSelectedMemberId}
                                                 onDelete={(memberId) => {
                                                     if (theParty !== undefined &&
                                                         theParty.characters !== undefined) {
                                                         const newp: PartyI = {
                                                             ...theParty,
                                                             characters: theParty.characters
                                                                 .filter(x => x !== memberId)
                                                         }
                                                         setTheParty(newp);
                                                     }
                                                 }}/>}
        {theParty !== undefined && <PartyEdit notYetMembers={notMembers}
                                              onAdd={(c) => {
                                                  if (theParty !== undefined &&
                                                      theParty.characters !== undefined) {
                                                      const newp: PartyI = {
                                                          ...theParty,
                                                          characters: [...theParty.characters, c.id]
                                                      }
                                                      setTheParty(newp)
                                                  }
                                              }}
                                              onAddNew={(c) => {
                                                  onNewCharacter(c);
                                                  if (theParty !== undefined &&
                                                      theParty.characters !== undefined) {
                                                      const newp: PartyI = {
                                                          ...theParty,
                                                          characters: [...theParty.characters, c.id]
                                                      }
                                                      setTheParty(newp)
                                                  }
                                              }}/>}
        {theParty !== undefined && selectedMember !== undefined &&
            <CharDesc name={selectedMember.name}
                      description={selectedMember.description}/>}
    </div>)
}