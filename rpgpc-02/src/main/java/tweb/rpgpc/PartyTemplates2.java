package tweb.rpgpc;

import tweb.rpgpc.rpg.Party;

public class PartyTemplates2 extends TemplatesBase{

    public PartyTemplates2(String contextPath, String[] commonStyles) {
        super(contextPath, commonStyles);
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

        return getPage(headerCSS(css),
                "<div class=\"%s\">".formatted(mainclass) +
                        getViewParties("/party/view") + //TODO
                        "</div>"
                );

    }
}
