import { loadParty } from "./generate-data.js";
const doAssociation = (event, listName, callback) => {
    const list = document.getElementsByClassName(listName)[0];
    const liElems = list.getElementsByTagName("li");
    for (let li of liElems) {
        li.addEventListener(event, callback);
    }
};
export function setupSelectionHighlight() {
    const highlight = (event) => {
        const clickedElem = event.target;
        if (clickedElem == null)
            return;
        const parentUL = clickedElem.closest("ul");
        if (parentUL == null)
            return;
        const liElems = parentUL.children;
        for (let li of liElems) {
            if (li === clickedElem) {
                li.classList.add("selected");
                if (li.role != null)
                    loadParty(li.role);
            }
            else {
                li.classList.remove("selected");
            }
        }
    };
    doAssociation("mousedown", "partylist", highlight);
    doAssociation("mousedown", "partymembers", highlight);
}
let popupTriggerLiElem = null;
export function setupRemovePopup() {
    const glass = document.getElementsByClassName("glasspane")[0];
    const showPopup = (event) => {
        if (event.button === 2) {
            popupTriggerLiElem = event.target;
            if (popupTriggerLiElem == null)
                return;
            const popup = document.getElementsByClassName("popup")[0];
            glass.classList.remove("hide");
            popup.style.left = `${event.clientX}px`;
            popup.style.top = `${event.clientY}px`;
        }
    };
    document.addEventListener("contextmenu", (event) => {
        event.preventDefault();
    });
    glass.addEventListener("mousedown", () => {
        glass.classList.add("hide");
    });
    const removeItem = document.getElementById("remove");
    if (removeItem != null) {
        removeItem.addEventListener("mousedown", () => {
            if (popupTriggerLiElem !== null)
                popupTriggerLiElem.remove();
        });
    }
    doAssociation("mousedown", "partylist", showPopup);
    doAssociation("mousedown", "partymembers", showPopup);
}
