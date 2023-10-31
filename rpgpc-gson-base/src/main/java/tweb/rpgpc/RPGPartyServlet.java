package tweb.rpgpc;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tweb.rpgpc.rpgdb.PartyDB;
import tweb.rpgpc.rpgdb.PartyManagerDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "RPGPC-Party-Servlet", urlPatterns = {"/parties"})
public class RPGPartyServlet extends HttpServlet {

    private Gson gson;

    public void init() {
        gson = new Gson();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");

        Map<String, String[]> pars = request.getParameterMap();
        String shortName = "";
        if (pars.containsKey("shortname")) {
            shortName = pars.get("shortname")[0];
        }

        PrintWriter out = response.getWriter();
        if (!shortName.isEmpty()) {
            PartyDB p = PartyManagerDB.getManager().getParty(shortName);
            if (p != null) out.println(gson.toJson(p));
            else response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader in = request.getReader();
        PartyDB p = gson.fromJson(in, PartyDB.class);
        String val = PartyManagerDB.getManager().addNewParty(p);
        if (!val.isEmpty()) response.sendRedirect(getServletContext().getContextPath() +
                ("/parties?shortname=%s").formatted(val));
        else response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    public void destroy() {
    }
}