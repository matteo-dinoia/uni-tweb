package tweb.rpgpc;

import java.io.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import tweb.rpgpc.rpg.PartyManager;

@WebServlet(name = "RPGPC-Party-Servlet", urlPatterns = {"/party/view/*", "/party/addchar/*", "/party/edit/*", "/party/new/*"})
public class RPGPartyServlet extends HttpServlet {

    private PartyTemplates templates;
    public void init() {
        String[] css = {"main-style.css", "viewparty-style.css"};
        templates = new PartyTemplates(getServletContext().getContextPath(), css);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] servletPath = request.getServletPath().split("/");

        String path = request.getPathInfo();
        if (path == null) path = "/";
        String[] pathParts = path.split("/");
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        if (servletPath[2].equals("view")) {
            if (pathParts.length == 0) {
                String[] css = {"parties-style.css"};
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
                if (PartyManager.getManager().getParty(partyname) != null) {
                    String[] css = {"viewparty-solo-style.css"};
                    out.println(
                            templates.getViewPartyPage(request.getServletPath(), css, partyname, charSelected)
                    );
                } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else if (servletPath[2].equals("addchar")) {
            String partyname = (pathParts.length >= 2 ?
                    pathParts[1] : "");
            if (PartyManager.getManager().getParty(partyname) != null) {
                String[] css = {"addchar-style.css"};
                out.println(templates.getAddCharacterToPartyPage(request.getServletPath(), css, partyname));
            } else response.sendError(HttpServletResponse.SC_NOT_FOUND);

        } else if (servletPath[2].equals("edit")) {
            String partyname = (pathParts.length >= 2 ?
                    pathParts[1] : "");

            if (PartyManager.getManager().getParty(partyname) != null || partyname.equals("")) {
                String[] css = {"party-editor-style.css", "parties-style.css"};
                out.println(templates.getEditPartyPage(request.getServletPath(), css, partyname));
            } else response.sendError(HttpServletResponse.SC_NOT_FOUND);

        } else if (servletPath[2].equals("new")){
            String[] css = {"new-party.css"};
            String[] paths = request.getRequestURL().toString().split("/");
            boolean isError = paths.length >= 7 && paths[6].equals("error");
            String name = request.getParameter("name");
            String sname = request.getParameter("shortname");
            out.println(templates.getNewPartyPage(request.getServletPath(), css, isError, sname, name));
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
                if (PartyManager.getManager().getParty(partyname) != null) {
                    PrintWriter out = response.getWriter();
                    String name = request.getParameter("character_name");
                    String rpgclass = request.getParameter("character_rpg_class");
                    int level = 0;
                    try {
                        level = Integer.parseInt(request.getParameter("character_level"));
                    } catch (NumberFormatException ex) {
                    }
                    String[] desc = request.getParameter("character_description").split("\n");
                    int charId = PartyManager.getManager().createCharacter(name, level, rpgclass, desc);
                    boolean added = PartyManager.getManager().addCharacterToParty(partyname, charId);
                    if (!added) response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    else {
                        response.sendRedirect(request.getContextPath() +
                                "/party/view/%s?sel=%s".formatted(partyname, charId));
                    }
                } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else if (lastReqSegment.equals("edit")) {
            String pathinfo = request.getPathInfo();
            String[] pathSegments = ((pathinfo == null) ? new String[0] : pathinfo.split("/"));

            if (pathSegments.length == 2) {
                String partyname = pathSegments[1];
                if (PartyManager.getManager().getParty(partyname) != null) {
                    PrintWriter out = response.getWriter();
                    int characterId = -1;

                    try {
                        characterId = Integer.parseInt(request.getParameter("add_character"));
                        PartyManager.getManager().addCharacterToParty(partyname, characterId);
                        response.sendRedirect(request.getContextPath() +
                                "/party/edit/" + partyname);
                    } catch (NumberFormatException ex) {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else if (lastReqSegment.equals("new")){
            PrintWriter out = response.getWriter();
            String name = request.getParameter("name");
            String sname = request.getParameter("shortname");

            if(PartyManager.getManager().addParty(sname, name)){
                response.sendRedirect(request.getContextPath()  + "/party/edit/" + sname);
            }else response.sendRedirect(request.getContextPath()  + "/party/new/error?name=" + name + "&shortname=" + sname);
        }else response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    public void destroy() {
    }
}