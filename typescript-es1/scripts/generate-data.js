import { parties, characters } from "./data.js";
export function setupLoad() {
    let partyList = document.querySelector(".partylist");
    if (partyList == null)
        return;
    partyList.innerHTML += getPartyHTML();
}
function getPartyHTML() {
    let res = "";
    let c = 0;
    for (let p of parties) {
        res += `<li role="${c}" ${c == 0 ? "class=\"selected\"" : ""}>${p.shortname}</li>`;
        c++;
    }
    return res;
}
export function loadParty(index) {
    let p = parties[index];
    console.log(p);
    if (p === undefined)
        return;
    let partyName = document.querySelector(".partyname");
    if (partyName != null)
        partyName.innerText = p.name;
    let members = "";
    for (let iChar of p.characters)
        members += `<li role="${iChar}">${characters[iChar - 1].name}</li>`;
    let partyMemebersUl = document.querySelector("#partymembersul");
    if (partyMemebersUl != null)
        partyMemebersUl.innerText = members;
}
