package tweb.rpgpc;

import tweb.rpgpc.rpg.*;
import tweb.rpgpc.rpg.Character;

import java.util.ArrayList;

public class PartyTemplates {

    private final String contextPath;
    private final String stylePath;

    private final String[] commonStyles;
    private String header;
    private String topbar;

    private final PartyManager partyManager;

    public PartyTemplates(String contextPath, String[] commonStyles) {
        this.contextPath = contextPath;
        this.stylePath = contextPath + "/style";
        this.commonStyles = commonStyles;

        partyManager = PartyManager.getManager();
        initHeader();
        initTopBar();
    }

    private void initHeader() {
        String meta = "<meta charset=\"UTF-8\">";
        String title = "<title>RPG Party Creator</title>";
        StringBuilder stylsheets = new StringBuilder();
        for (String style : commonStyles) {
            stylsheets.append("<link rel=\"stylesheet\" href=\"%s/%s\">".formatted(stylePath, style));
        }
        String head = meta + title + stylsheets;
        header = "<head>" + head + "%s</head>";
    }

    private void initTopBar() {
        String h1 = "<h1>RPG Party Creator</h1>";
        String classname = "topbar";
        topbar = "<section class=\"%s\">%s</section>".formatted(classname, h1);
    }

    private String getViewParties(String baseLink){
        String linkList = "";

        for(Party p : this.partyManager.getPartiesList()){
            linkList += "        <li><a href=\"" + contextPath + baseLink + "/" + p.getShortname() +"\">\n" +
                        "            " + p.getName() + "\n" +
                        "        </a></li>\n";
        }

        return "<div class=\"parties\">\n" +
                "    <h2>Your parties:</h2>\n" +
                "    <ul>\n" +
                linkList +
                "    </ul>\n" +
                "    <div class=\"partiesactions\">\n" +
                "        <a href=\"" + contextPath + "/party/new\">" +
                "           <div class=\"pulsante\">Add...</div>" +
                "        </a>\n" +
                "    </div>\n" +
                "</div>";
    }

    public String getDummyPartyPage(String[] css) {
        String mainclass = "viewparty";

        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                headerCSS(css) +
                "<body>\n" +
                topbar +
                "<div class=\"%s\">".formatted(mainclass) +
                getViewParties("/party/view") + //TODO
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }

    public String getViewPartyPage(String basePath, String[] css, String partyName, int selectedId) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">" +
                headerCSS(css) +
                "<body>\n" +
                topbar +
                getViewPartyComponent(basePath, partyName, selectedId) +
                "</body>\n" +
                "</html>";
    }

    private String getViewPartyComponent(String basePath, String partyName, int selectedId) {
        String mainclass = "viewparty";

        return "<div class=\"%s\">\n".formatted(mainclass) +
                getViewPartyComponentInner(basePath, partyName, selectedId) +
                "</div>\n";
    }

    private String getViewPartyComponentInner(String basePath, String partyName, int selectedId) {
        String titleclass = "partyname";
        String membersclass = "partymembers";
        String desclass = "chardesc";

        Party prt = partyManager.getParty(partyName);


        return  "<h1 class=\"%s\">%s</h1>\n".formatted(titleclass, prt.getName()) +
                "<div class=\"%s\">\n".formatted(membersclass) +
                "<h2>Party members:</h2>\n" +
                getMembersList(basePath, partyName, selectedId) +
                "</div>\n" +
                (prt.hasCharacter(selectedId) ?
                        "<div class=\"%s\">\n".formatted(desclass) +
                                getCharacterDescription(basePath, selectedId) +
                                "</div>\n" : "");
    }

    private String getCharacterDescription(String basePath, int charId) {
        Character c = partyManager.getCharacter(charId);
        if (c == null) return "";
        String title = "<h3>%s</h3>".formatted(c.getName());
        String[] descText = c.getDescription();
        StringBuilder paragraphs = new StringBuilder();
        for (String par : descText) {
            paragraphs.append("<p>%s</p>".formatted(par));
        }
        return (title + paragraphs);
    }

    private String getMembersList(String basePath, String partyname, int selectedId) {
        ArrayList<Character> chars = partyManager.getPartyCharacterList(partyname);
        StringBuilder charli = new StringBuilder();
        for (Character c : chars) {
            if (c.getId() != selectedId) {
                String hRef = contextPath + basePath + "/%s?sel=%s".formatted(partyname, c.getId());
                String seclass = (c.getId() == selectedId ? "class = \"selected\"" : "");
                charli.append("<li %s><a href=\"%s\">%s (%s, lv.%s)</a></li>"
                        .formatted(seclass, hRef, c.getName(), c.getRpgclass(), c.getLevel()));
            } else {
                String seclass = (c.getId() == selectedId ? "class = \"selected\"" : "");
                charli.append("<li %s>%s (%s, lv.%s)</li>"
                        .formatted(seclass, c.getName(), c.getRpgclass(), c.getLevel()));
            }
        }
        return "<ul>%s</ul>".formatted(charli.toString());
    }


    public String getAddCharacterToPartyPage(String basePath, String[] css, String partyName) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">" +
                headerCSS(css) +
                "<body>\n" +
                topbar +
                getAddCharacterToPartyComponent(basePath, partyName) +
                "</body>\n" +
                "</html>";
    }

    private String getAddCharacterToPartyComponent(String basePath, String partyName) {
        Party p = this.partyManager.getParty(partyName);
        if (p == null)
            return "<section class=\"addchar\">\n" +
                    ("<h2>Nonexistent Party \"%s\"- cannot add character</h2>\n").formatted(partyName) +
                    "</section>";

        String action = contextPath + basePath + "/" + partyName;
        return "<section class=\"addchar\">\n" +
                ("<h2>Add Character to %s</h2>\n").formatted(p.getName()) +
                ("<form action=\"%s\"\n").formatted(action) +
                "      method=\"post\"\n" +
                "      class=\"addchar\">\n" +
                "    <div class=\"h_formgroup name\">\n" +
                "        <label for=\"name\">Name</label>\n" +
                "        <input type=\"text\"\n" +
                "               id=\"name\"\n" +
                "               name=\"character_name\">\n" +
                "    </div>\n" +
                "    <div class=\"h_formgroup rpgclass\">\n" +
                "        <label for=\"rpgclass\">Class</label>\n" +
                "        <input type=\"text\"\n" +
                "               id=\"rpgclass\"\n" +
                "               name=\"character_rpg_class\">\n" +
                "    </div>\n" +
                "    <div class=\"h_formgroup level\">\n" +
                "        <label for=\"level\">Level</label>\n" +
                "        <input type=\"number\"\n" +
                "               id=\"level\"\n" +
                "               name=\"character_level\">\n" +
                "    </div>\n" +
                "    <div class=\"v_formgroup desc\">\n" +
                "        <label for=\"desc\">Description</label>\n" +
                "        <textarea id=\"desc\"\n" +
                "                  name=\"character_description\"></textarea>\n" +
                "    </div>\n" +
                "    <div class=\"formaction\">\n" +
                "        <button type=\"submit\">Add to Party</button>\n" +
                "    </div>\n" +
                "</form>\n" +
                "</section>";
    }

    private String getEditParty(String basePath, String partyName){
        String options = "";
        for(Character c : this.partyManager.getCharacterNotInPartyList()){
            options += "                    <option value=\"" + c.getId() + "\">" + c.getName() + "</option>\n";
        }

        String mainclass = "viewparty";

        return "<div class=\"%s\">\n".formatted(mainclass) +
                getViewPartyComponentInner(basePath, partyName, -1) +
                "   <div class=\"partyedit\">\n" +
                "            <h2>Add member:</h2>\n" +
                "            <form action=\"/rpgpc/party/edit/" + partyName + "\"\n" +
                "                  method=\"post\">\n" +
                "                <select name=\"add_character\">\n" +
                options +
                "                </select>\n" +
                "                <button type=\"submit\">Add</button>\n" +
                "            </form>\n" +
                "            <div class=\"membersactions\">\n" +
                "                <div class=\"pulsante\"><a href=\"/rpgpc/party/addchar/" + partyName + "\">Create new</a></div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>" +
                "</div>\n";
    }
    public String getEditPartyPage(String basePath, String[] css, String partyName) {
        Party p = this.partyManager.getParty(partyName);

        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">" +
                headerCSS(css) +
                "<body>\n" +
                topbar +
                getViewParties("/party/edit") +
                (p == null ? "" : getViewPartyComponent(basePath, partyName, 0) + getEditParty(basePath, partyName)) +
                "</body>\n" +
                "</html>";
    }

    public String getNewPartyPage(String servletPath, String[] css, boolean isError, String oldShortName, String oldName) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">" +
                headerCSS(css) +
                "<body>\n" +
                getNewPartyComponent(servletPath, isError, oldShortName, oldName) +
                "</body>\n" +
                "</html>";
    }

    public String getNewPartyComponent(String link, boolean isError, String oldShortName, String oldName){
        String error = "";
        if(isError)
            error = "<div class=\"error\">Errore input invalido</div>";

        return "<div>" +
                "   <h1> RPG Party Creator</h1>" +
                "   <h2>Create new party </h2>" +
                error +
                "   <form method=\"post\" action=\"" + contextPath + link + "\">" +
                "       <label for=\"fname\">Name</label>" +
                "       <input id=\"fname\" name=\"name\" type=\"text\" value=\"" + (oldName != null ? oldName : "") + "\">" +
                "       <label for=\"fshort\">Shortname: only lowercase [a-z], hyphen (-) and underscore (_) allowed</label>" +
                "       <input id=\"fshort\" name=\"shortname\" type=\"text\" value=\"" + (oldShortName != null ? oldShortName : "") + "\">" +
                "       <input class=\"btn\" type=\"submit\" value=\"Add party\"></input>" +
                "   </form>" +
                "</div>";
    }

    public String headerCSS(String[] css){
        if(css == null || css.length == 0)
            return "";

        StringBuilder addstylesheets = new StringBuilder();
        for (String style : css) {
            addstylesheets.append("<link rel=\"stylesheet\" href=\"%s//%s\">".formatted(stylePath, style));
        }

        return header.formatted(addstylesheets.toString());
    }
}

