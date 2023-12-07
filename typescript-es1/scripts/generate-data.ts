import { parties, characters } from "./data.js";

export function setupLoad() : void {
    let partyList : HTMLElement | null = document.querySelector(".partylist");
    if(partyList == null) return;
    partyList.innerHTML += getPartyHTML();
}


function getPartyHTML() : string{
    let res = "";
    let c = 0;
    for(let p of parties){
        res += `<li role="${c}" ${c==0 ? "class=\"selected\"" : ""}>${p.shortname}</li>`
        c++;
    }
    return res;
}

export function loadParty(index : number) : void{
    let p = parties[index];
    console.log(p);
    if(p === undefined)
        return;

    let partyName : HTMLElement | null = document.querySelector(".partyname");
    if(partyName != null)
        partyName.innerText = p.name;

    let members : string = "";
    for(let iChar of p.characters)
        members += `<li role="${iChar}">${characters[iChar - 1].name}</li>`
    
    let partyMemebersUl : HTMLElement | null = document.querySelector("#partymembersul");
    if(partyMemebersUl != null)
        partyMemebersUl.innerText = members;
}