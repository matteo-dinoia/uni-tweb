package tweb.rpgpc;

import java.io.*;
import java.util.Map;

import com.google.gson.Gson;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import tweb.rpgpc.rpgdb.CharacterDB;
import tweb.rpgpc.rpgdb.PartyManagerDB;

@WebServlet(name = "RPGPC-Character-Servlet", urlPatterns = {"/characters"})
public class RPGCharacterServlet extends HttpServlet {

    private Gson gson;

    public void init() {
        gson = new Gson();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");

        Map<String, String[]> pars = request.getParameterMap();
        int charId = -1;
        if (pars.containsKey("id")) {
            charId = Integer.parseInt(pars.get("id")[0]);
        }

        PrintWriter out = response.getWriter();
        if (charId > 0) {
            CharacterDB c = PartyManagerDB.getManager().getCharacter(charId);
            if (c != null) out.println(gson.toJson(c));
            else response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader in = request.getReader();
        CharacterDB c = gson.fromJson(in, CharacterDB.class);
        int val = PartyManagerDB.getManager().addNewCharacter(c);
        if (val > 0) response.sendRedirect(getServletContext().getContextPath() +
                "/characters?id=%s".formatted(val));
        else response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Map<String, String[]> pars = request.getParameterMap();
        int charId = -1;
        if (pars.containsKey("id")) {
            charId = Integer.parseInt(pars.get("id")[0]);
        }

        PrintWriter out = response.getWriter();
        if (charId > 0) {
            boolean deleted = PartyManagerDB.getManager().deleteCharacter(charId);
            out.println(deleted);
        } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        BufferedReader in = request.getReader();
        CharacterDB c = gson.fromJson(in, CharacterDB.class);
        boolean updated = PartyManagerDB.getManager().updateCharacter(c);
        PrintWriter out = response.getWriter();
        out.println(updated);
    }
    public void destroy() {
    }
}