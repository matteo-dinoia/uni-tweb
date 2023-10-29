package tweb.rpgpc;

import tweb.rpgpc.rpg.PartyManager;

public abstract class TemplatesBase {

    protected final String contextPath, stylePath;
    protected final String headerContent, topbar;
    protected final String[] commonStyles;
    protected final PartyManager partyManager;

    public TemplatesBase(String contextPath, String[] commonStyles) {
        this.contextPath = contextPath;
        this.stylePath = contextPath + "/style";
        this.commonStyles = commonStyles;

        partyManager = PartyManager.getManager();
        headerContent = createHeader();
        topbar = createTopBar();
    }

    private String createHeader() {
        String meta = "<meta charset=\"UTF-8\">";
        String title = "<title>RPG Party Creator</title>";
        return meta + title + headerCSS(commonStyles);
    }

    protected String headerCSS(String[] css){
        if(css == null || css.length == 0)
            return "";

        StringBuilder addstylesheets = new StringBuilder();
        for (String style : css)
            addstylesheets.append("<link rel=\"stylesheet\" href=\"%s//%s\">".formatted(stylePath, style));

        return addstylesheets.toString();
    }

    private String createTopBar() {
        String h1 = "<h1>RPG Party Creator</h1>";
        String classname = "topbar";
        return "<section class=\"%s\">%s</section>".formatted(classname, h1);
    }

    protected String getPage(String head, String body){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                    headerContent +
                    head +
                "<body>\n" +
                    topbar +
                    body +
                "</body>\n";
    }
}
