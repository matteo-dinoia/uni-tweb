import { parties, characters } from "./data.js";

export function setupLoad() {
    let partyList = document.querySelector(".partylist");
    partyList.innerHTML += getPartyHTML();
    
}


function getPartyHTML(){
    let res = "";
    let c = 0;
    for(let p of parties){
        res += `<li role="${c}" ${c==0 ? "class=\"selected\"" : ""}>${p.shortname}</li>`
        c++;
    }
    return res;
}

export function loadParty(index){
    let p = parties[index];
    console.log(p);
    if(p === undefined)
        return;

    document.querySelector(".partyname").innerText = p.name;

    let members = "";
    for(let iChar of p.characters){
        members += `<li role="${iChar}">${characters[iChar - 1].name}</li>`
    }
    document.querySelector("#partymembersul").innerHTML = members;

}