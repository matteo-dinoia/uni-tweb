import { setupRemovePopup, setupSelectionHighlight } from "./functions.js";
import { loadParty, setupLoad } from "./generate-data.js";
setupLoad();
loadParty(0);
setupSelectionHighlight();
setupRemovePopup();
