
let listLi = document.querySelectorAll("li");
for(let el of listLi){
    el.addEventListener("contextmenu", handleMenu);
}
console.log("V3");

function handleMenu(event){
    event.stopPropagation();
    event.preventDefault();

    console.log("LOL");

    document.body.innerHTML += `<div class="glasspane">
            <div class="glasspane-text" style="margin: ${event.pageY}px auto auto ${event.pageX}px;"};">Delete</div>
       </div>`;

}