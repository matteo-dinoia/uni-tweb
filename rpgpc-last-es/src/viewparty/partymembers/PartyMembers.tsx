import "./PartyMembers.css";
import remove from "../../assets/remove.svg"
import {FC} from "react";
import {CharacterI} from "../../data/rpgpc.ts";

interface MemberElemPropsI {
    name: string;
    rpgclass: string;
    level: number;
    selected: boolean;
    onSelect: () => void;
    onDelete: () => void;
}

const MemberElem: FC<MemberElemPropsI> = ({
                                              name,
                                              rpgclass,
                                              level,
                                              selected,
                                              onSelect,
                                              onDelete
                                          }) => {
    return (<li className={(selected ? "selected" : "")}
                onClick={onSelect}>
        <span>{name} ({rpgclass}, lv.{level})</span>
        {selected && <img className="deletemember"
                          src={remove}
                          onClick={(e) => {
                              e.stopPropagation();
                              onDelete();
                          }}/>}
    </li>);
};

interface PartyMembersPropsI {
    members: CharacterI[];
    selectedMember: number;
    onSelect: (memberId: number) => void;
    onDelete: (memberId: number) => void;
}

export const PartyMembers: FC<PartyMembersPropsI> = ({
                                                         members,
                                                         selectedMember,
                                                         onSelect,
                                                         onDelete
                                                     }) => {
    const memberListElems = members.map((m) =>
        <MemberElem level={m.level}
                    rpgclass={m.rpgclass}
                    name={m.name}
                    selected={selectedMember === m.id}
                    onSelect={() => onSelect(m.id)}
                    onDelete={() => onDelete(m.id)}/>)
    return (<div className="partymembers">
        <h2>Party members:</h2>
        <ul>
            {memberListElems}
        </ul>
    </div>);
}