package tweb.rpgpc;

import tweb.rpgpc.rpg.*;
import tweb.rpgpc.rpg.Character;

import java.util.ArrayList;

public class PartyTemplates {

    private final String contextPath;
    private final String servletPath;
    private final String stylePath;

    private final String[] commonStyles;
    private String header;
    private String topbar;

    private final PartyManager partyManager;

    public PartyTemplates(String contextPath, String servletPath, String[] baseStyles) {
        this.contextPath = contextPath;
        this.servletPath = servletPath;
        this.stylePath = contextPath + "/style";
        this.commonStyles = baseStyles;

        partyManager = PartyManager.getManager();
        initHeader();
        initTopBar();
    }

    private void initHeader() {
        String meta = "<meta charset=\"UTF-8\">";
        String title = "<title>RPG Party Creator</title>";
        StringBuilder stylsheets = new StringBuilder();
        for (String style : commonStyles) {
            stylsheets.append("<link rel=\"stylesheet\" href=\"%s//%s\">".formatted(stylePath, style));
        }
        String head = meta + title + stylsheets;
        header = "<head>" + head + "%s</head>";
    }

    private void initTopBar() {
        String h1 = "<h1>RPG Party Creator</h1>";
        String classname = "topbar";
        topbar = "<section class=\"%s\">%s</section>".formatted(classname, h1);
    }

    public String getDummyPartyPage() {
        String mainclass = "viewparty";

        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                header.formatted("") +
                "<body>\n" +
                topbar +
                "<div class=\"%s\">".formatted(mainclass) +
                "<h2>Route disponibili:</h2>\n" +
                "<ul>\n" +
                "<li>%s%s</li> mostra questa pagina</li>\n".formatted(contextPath, servletPath) +
                "<li>%s%s/[nome-party]</li> mostra il party specificato</li>".formatted(contextPath, servletPath) +
                ("<li>%s%s/[nome-party]?sel=[id]</li> mostra il party specificato con selezionato il personaggio" +
                        "corrispondente all\'id indicato</li>\n").formatted(contextPath, servletPath) +
                "</ul>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }

    public String getViewPartyPage(String[] css, String partyName, int selectedId) {
        StringBuilder addstylesheets = new StringBuilder();
        for (String style : css) {
            addstylesheets.append("<link rel=\"stylesheet\" href=\"%s//%s\">".formatted(stylePath, style));
        }
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">" +
                header.formatted(addstylesheets.toString()) +
                "<body>\n" +
                topbar +
                getViewPartyComponent(partyName, selectedId) +
                "</body>\n" +
                "</html>";
    }

    private String getViewPartyComponent(String partyName, int selectedId) {
        String mainclass = "viewparty";
        String titleclass = "partyname";
        String membersclass = "partymembers";
        String desclass = "chardesc";

        Party prt = partyManager.getParty(partyName);


        return "<div class=\"%s\">\n".formatted(mainclass) +
                "<h1 class=\"%s\">%s</h1>\n".formatted(titleclass, prt.getName()) +
                "<div class=\"%s\">\n".formatted(membersclass) +
                "<h2>Party members:</h2>\n" +
                getMembersList(partyName, selectedId) +
                "</div>\n" +
                (prt.hasCharacter(selectedId) ?
                "<div class=\"%s\">\n".formatted(desclass) +
                        getCharacterDescription(selectedId) +
                        "</div>\n" : "") +
                "</div>\n";
    }

    private String getCharacterDescription(int charId) {
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

    private String getMembersList(String partyname, int selectedId) {
        ArrayList<Character> chars = partyManager.getPartyCharacterList(partyname);
        StringBuilder charli = new StringBuilder();
        for (Character c : chars) {
            if (c.getId() != selectedId) {
                String hRef = contextPath + servletPath + "/%s?sel=%s".formatted(partyname, c.getId());
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


    public String getAddCharacterToPartyPage(String[] css, String partyName) {
        StringBuilder addstylesheets = new StringBuilder();
        for (String style : css) {
            addstylesheets.append("<link rel=\"stylesheet\" href=\"%s//%s\">".formatted(stylePath, style));
        }
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">" +
                header.formatted(addstylesheets.toString()) +
                "<body>\n" +
                topbar +
                getAddCharacterToPartyComponent(partyName) +
                "</body>\n" +
                "</html>";
    }

    private String getAddCharacterToPartyComponent(String partyName) {
        Party p = this.partyManager.getParty(partyName);
        if (p == null)
            return "<section class=\"addchar\">\n" +
                    ("<h2>Nonexistent Party \"%s\"- cannot add character</h2>\n").formatted(partyName) +
                    "</section>";

        return "<section class=\"addchar\">\n" +
                ("<h2>Add Character to %s</h2>\n").formatted(p.getName()) +
                ("<form action=\"/rpgpc/solved/party/addchar/%s\"\n").formatted(partyName) +
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
}
