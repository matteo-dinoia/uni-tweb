package tweb.rpgpc;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import tweb.rpgpc.rpgdb.PartyManagerDB;

/* URL Patterns
    /party/view --> per il momento mostra delle istruzioni per l'uso
    /party/view/[nome-party] --> mostra il party [nome-party] senza nessun personaggio selezionato
    /party/view/[nome-party]?sel=N --> mostra il party da visualizzare selezionando il personaggio con id = N
    /party/addchar/[nome-party] -->
        GET mostra il form per aggiungere un nuovo personaggio al party
        POST invia i dati per l'aggiunta e mostra il party con il nuovo personaggio selezionato
    se non esiste un party [nome-party], o se in esso non c'è un personaggio con id=N,
    deve venire generato un errore 404 not found
 */
@WebServlet(name = "RPGPC-Party-Servlet", urlPatterns = {"/party/view/*", "/party/addchar/*"})
public class RPGPartyServlet extends HttpServlet {

    private PartyTemplates templates;
    public void init() {
        String[] css = {"main-style.css", "viewparty-style.css"};
        templates = new PartyTemplates(getServletContext().getContextPath(), css);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");

        String[] servletPath = request.getServletPath().split("/");
        if (servletPath[2].equals("view")) {

            String path = request.getPathInfo();
            if (path == null) path = "/";
            String[] pathParts = path.split("/");
            PrintWriter out = response.getWriter();

            if (pathParts.length == 0) {
                String[] css = {"viewparty-solo-style.css"};
                out.println(templates.getDummyPartyPage(css));
            } else if (pathParts.length >= 2) {
                String[] values = request.getParameterValues("sel");
                String val = (values != null ? values[0] : "-1");
                int charSelected = -1;
                try {
                    charSelected = Integer.parseInt(val);
                } catch (NumberFormatException e) {
                }
                String partyname = pathParts[1];
                if (PartyManagerDB.getManager().getParty(partyname) != null) {
                    String[] css = {"viewparty-solo-style.css"};
                    out.println(
                            templates.getViewPartyPage(request.getServletPath(), css, partyname, charSelected)
                    );
                } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else if (servletPath[2].equals("addchar")) {
            String path = request.getPathInfo();
            if (path == null) path = "/";
            String[] pathParts = path.split("/");
            PrintWriter out = response.getWriter();
            String partyname = (pathParts.length >= 2 ?
                    pathParts[1] : "");
            if (PartyManagerDB.getManager().getParty(partyname) != null) {
                String[] css = {"addchar-style.css"};
                out.println(templates.getAddCharacterToPartyPage(request.getServletPath(), css, partyname));
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
                if (PartyManagerDB.getManager().getParty(partyname) != null) {
                    PrintWriter out = response.getWriter();
                    String name = request.getParameter("character_name");
                    String rpgclass = request.getParameter("character_rpg_class");
                    int level = 0;
                    try {
                        level = Integer.parseInt(request.getParameter("character_level"));
                    } catch (NumberFormatException ex) {
                    }
                    String[] desc = request.getParameter("character_description").split("\n");
                    int charId = PartyManagerDB.getManager().createCharacter(name, level, rpgclass, desc);
                    boolean added = PartyManagerDB.getManager().addCharacterToParty(partyname, charId);
                    if (!added) response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    else {
                        response.sendRedirect(request.getContextPath() +
                                "/party/view/%s?sel=%s".formatted(partyname, charId));
                    }
                } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    public void destroy() {
    }
}