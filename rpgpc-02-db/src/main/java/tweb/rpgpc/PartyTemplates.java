package tweb.rpgpc;

import tweb.rpgpc.rpgdb.CharacterDB;
import tweb.rpgpc.rpgdb.PartyDB;
import tweb.rpgpc.rpgdb.PartyManagerDB;

import java.util.ArrayList;

public class PartyTemplates {

    private final String contextPath;
    private final String stylePath;

    private final String[] commonStyles;
    private String header;
    private String topbar;

    private final PartyManagerDB partyManager;

    public PartyTemplates(String contextPath, String[] commonStyles) {
        this.contextPath = contextPath;
        this.stylePath = contextPath + "/style";
        this.commonStyles = commonStyles;

        partyManager = PartyManagerDB.getManager();
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

    public String getDummyPartyPage(String[] css) {
        StringBuilder addstylesheets = new StringBuilder();

        for (String style : css) {
            addstylesheets.append("<link rel=\"stylesheet\" href=\"%s//%s\">".formatted(stylePath, style));
        }
        String mainclass = "viewparty";

        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                header.formatted(addstylesheets.toString()) +
                "<body>\n" +
                topbar +
                "<div class=\"%s\">".formatted(mainclass) +
                "<h3>Questa pagina dovrebbe, a regime, mostrare l'elenco dei party disponibili</h3>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }

    public String getViewPartyPage(String basePath, String[] css, String partyName, int selectedId) {
        StringBuilder addstylesheets = new StringBuilder();
        for (String style : css) {
            addstylesheets.append("<link rel=\"stylesheet\" href=\"%s//%s\">".formatted(stylePath, style));
        }
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">" +
                header.formatted(addstylesheets.toString()) +
                "<body>\n" +
                topbar +
                getViewPartyComponent(basePath, partyName, selectedId) +
                "</body>\n" +
                "</html>";
    }

    private String getViewPartyComponent(String basePath, String partyName, int selectedId) {
        String mainclass = "viewparty";
        String titleclass = "partyname";
        String membersclass = "partymembers";
        String desclass = "chardesc";

        PartyDB prt = partyManager.getParty(partyName);


        return "<div class=\"%s\">\n".formatted(mainclass) +
                "<h1 class=\"%s\">%s</h1>\n".formatted(titleclass, prt.getName()) +
                "<div class=\"%s\">\n".formatted(membersclass) +
                "<h2>Party members:</h2>\n" +
                getMembersList(basePath, partyName, selectedId) +
                "</div>\n" +
                (prt.hasCharacter(selectedId) ?
                "<div class=\"%s\">\n".formatted(desclass) +
                        getCharacterDescription(basePath, selectedId) +
                        "</div>\n" : "") +
                "</div>\n";
    }

    private String getCharacterDescription(String basePath, int charId) {
        CharacterDB c = partyManager.getCharacter(charId);
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
        ArrayList<CharacterDB> chars = partyManager.getPartyCharacterList(partyname);
        StringBuilder charli = new StringBuilder();
        for (CharacterDB c : chars) {
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
        StringBuilder addstylesheets = new StringBuilder();
        for (String style : css) {
            addstylesheets.append("<link rel=\"stylesheet\" href=\"%s//%s\">".formatted(stylePath, style));
        }
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">" +
                header.formatted(addstylesheets.toString()) +
                "<body>\n" +
                topbar +
                getAddCharacterToPartyComponent(basePath, partyName) +
                "</body>\n" +
                "</html>";
    }

    private String getAddCharacterToPartyComponent(String basePath, String partyName) {
        PartyDB p = this.partyManager.getParty(partyName);
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
}
