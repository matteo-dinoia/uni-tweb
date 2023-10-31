package tweb.rpgpc;

import tweb.rpgpc.rpgdb.CharacterDB;
import tweb.rpgpc.rpgdb.PartyDB;
import tweb.rpgpc.rpgdb.PartyManagerDB;

import java.util.ArrayList;

public class HtmlGenerator {

    public static class AddCharacterForm {
        public static String CHAR_NAME = "character_name";
        public static String CHAR_LEVEL = "character_level";
        public static String CHAR_CLASS = "character_rpgclass";

        public static String CHAR_DESCRITPTION = "character_desc";
    }

    private final String contextPath;
    private final String stylePath;

    private final String[] commonStyles;
    private String header;
    private String topbar;

    public HtmlGenerator(String contextPath, String[] commonStyles) {
        this.contextPath = contextPath;
        this.stylePath = contextPath + "/style";
        this.commonStyles = commonStyles;
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

    private String preparePage(String[] css) {
        StringBuilder addstylesheets = new StringBuilder();
        for (String style : css) {
            addstylesheets.append("<link rel=\"stylesheet\" href=\"%s//%s\">".formatted(stylePath, style));
        }
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">" +
                header.formatted(addstylesheets.toString()) +
                "<body>\n" +
                topbar +
                "%s" +
                "</body>\n" +
                "</html>";
    }

    public String getCharactersPage(String[] css, ArrayList<CharacterDB> all) {
        StringBuilder body = new StringBuilder();
        body.append("<ul>");
        for (CharacterDB c: all) {
            body.append("<li>%s (level %s) - %s</li>".formatted(c.getName(), c.getLevel(), c.getRpgclass()));
        }
        body.append("</ul>");
        return preparePage(css).formatted(body.toString());
    }


    public String getCharacterPage(String[] css, CharacterDB c) {
        StringBuilder body = new StringBuilder();
        body.append("<div>");
        body.append("<h1>%s</h1>".formatted(c.getName()));
        body.append("<p><strong>Level:</strong> %s</p>".formatted(c.getLevel()));
        body.append("<p><strong>Class:</strong> %s</p>".formatted(c.getRpgclass()));
        for (String s: c.getDescription()) {
            body.append("<p>%s</p>".formatted(s));
        }
        body.append("</div>");
        return preparePage(css).formatted(body.toString());
    }

    public String getAddCharacterPage(String[] css, String action, String method) {
        String comp = "<section class=\"addchar\">\n" +
                "<h2>Create new character</h2>\n" +
                "<form action=\"%s%s\"\n".formatted(contextPath, action) +
                "      method=\"%s\"\n".formatted(method) +
                "      class=\"addchar\">\n" +
                "    <div class=\"h_formgroup name\">\n" +
                "        <label for=\"name\">Name</label>\n" +
                "        <input type=\"text\"\n" +
                "               id=\"name\"\n" +
                "               name=\"%s\">\n".formatted(AddCharacterForm.CHAR_NAME) +
                "    </div>\n" +
                "    <div class=\"h_formgroup rpgclass\">\n" +
                "        <label for=\"rpgclass\">Class</label>\n" +
                "        <input type=\"text\"\n" +
                "               id=\"rpgclass\"\n" +
                "               name=\"%s\">\n".formatted(AddCharacterForm.CHAR_CLASS) +
                "    </div>\n" +
                "    <div class=\"h_formgroup level\">\n" +
                "        <label for=\"level\">Level</label>\n" +
                "        <input type=\"number\"\n" +
                "               id=\"level\"\n" +
                "               name=\"%s\">\n".formatted(AddCharacterForm.CHAR_LEVEL) +
                "    </div>\n" +
                "    <div class=\"v_formgroup desc\">\n" +
                "        <label for=\"desc\">Description</label>\n" +
                "        <textarea id=\"desc\"\n" +
                "                  name=\"%s\"></textarea>\n".formatted(AddCharacterForm.CHAR_DESCRITPTION) +
                "    </div>\n" +
                "    <div class=\"formaction\">\n" +
                "        <button type=\"submit\">Create</button>\n" +
                "    </div>\n" +
                "</form>\n" +
                "</section>";
        return preparePage(css).formatted(comp);
    }
}
