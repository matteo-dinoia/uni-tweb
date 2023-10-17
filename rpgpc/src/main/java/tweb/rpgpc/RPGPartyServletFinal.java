package tweb.rpgpc;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tweb.rpgpc.rpg.PartyManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

/* URL Patterns
    /solved/party --> per il momento mostra delle istruzioni per l'uso
    /solved/party/[nome-party] --> mostra il party [nome-party] senza nessun personaggio selezionato
    /solved/party/[nome-party]?charid=N --> mostra il party da visualizzare selezionando il personaggio con id = N

    se non esiste un party [nome-party], o se in esso non c'è un personaggio con id=N,
    deve venire generato un errore 404 not found
 */
@WebServlet(name = "RPGPC-Party-Servlet-Final", urlPatterns = {"/solved/party/view/*", "/solved/party/addchar/*"})
public class RPGPartyServletFinal extends HttpServlet {
    private PartyManager partyManager;
    private PartyTemplates partyTemplates;

    public void init() {
        this.partyManager = PartyManager.getManager();
        String[] styles = {"main-style.css", "viewparty-style.css"};
        this.partyTemplates = new PartyTemplates(this.getServletContext().getContextPath(),
                "/solved/party/view", styles);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");

        String servpath = request.getServletPath();
        String[] requestSegments = ((servpath == null) ? new String[0] : servpath.split("/"));
        String lastReqSegment = requestSegments[requestSegments.length-1];

        // lastReqSegment è per forza o "view" o "addchar"
        String pathinfo = request.getPathInfo();
        String[] pathSegments = ((pathinfo == null) ? new String[0] : pathinfo.split("/"));

        String baseurl = this.getServletContext().getContextPath();
        PrintWriter out = response.getWriter();

        if (lastReqSegment.equals("view")) {
            if (pathSegments.length == 0) {
                //party --> per il momento mostra delle istruzioni per l'uso
                out.println(this.partyTemplates.getDummyPartyPage());
            } else if (pathSegments.length == 2) {
                String[] selChar = request.getParameterValues("sel");
                int selectedId = -1;
                if (selChar != null && selChar.length > 0) {
                    String selValue = selChar[0];
                    try {
                        selectedId = Integer.parseInt(selValue);
                    } catch (NumberFormatException exc) {
                    }
                }
                String partyname = pathSegments[1];
                if (partyManager.getParty(partyname) != null) {
                    String[] css = {"viewparty-solo-style.css"};
                    out.println(partyTemplates.getViewPartyPage(css, partyname, selectedId));
                } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else if (lastReqSegment.equals("addchar")) {
            if (pathSegments.length == 2) {
                String partyname = pathSegments[1];
                if (partyManager.getParty(partyname) != null) {
                    String[] css = {"addchar-style.css"};
                    out.println(partyTemplates.getAddCharacterToPartyPage(css, partyname));
                } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String servpath = request.getServletPath();
        String[] requestSegments = ((servpath == null) ? new String[0] : servpath.split("/"));
        String lastReqSegment = requestSegments[requestSegments.length-1];

        // lastReqSegment è per forza o "view" o "addchar"
        if (lastReqSegment.equals("addchar")) {
            String pathinfo = request.getPathInfo();
            String[] pathSegments = ((pathinfo == null) ? new String[0] : pathinfo.split("/"));

            if (pathSegments.length == 2) {
                String partyname = pathSegments[1];
                if (partyManager.getParty(partyname) != null) {
                    PrintWriter out = response.getWriter();
                    String name = request.getParameter("character_name");
                    String rpgclass = request.getParameter("character_rpg_class");
                    int level = 0;
                    try {
                        level = Integer.parseInt(request.getParameter("character_lavel"));
                    } catch (NumberFormatException ex) {
                    }
                    String[] desc = request.getParameter("character_description").split("\n");
                    int charId = partyManager.createCharacter(name, level, rpgclass, desc);
                    boolean added = partyManager.addCharacterToParty(partyname, charId);
                    if (!added) response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    else {
                        response.sendRedirect(request.getContextPath() +
                                "/solved/party/view/%s?sel=%s".formatted(partyname, charId));
                    }
                } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    public void destroy() {
    }
}