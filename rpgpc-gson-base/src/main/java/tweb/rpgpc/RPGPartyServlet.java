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
import java.util.ArrayList;
import java.util.HashMap;
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
        } else{
            ArrayList<PartyDB> parties = PartyManagerDB.getManager().getAllParties();
            HashMap<String, String>[] res = new HashMap[parties.size()];
            for(int i = 0; i < parties.size(); i++){
                res[i] = new HashMap<>();
                res[i].put("shorname", parties.get(i).getShortname());
                res[i].put("name", parties.get(i).getName());
            }

            out.println(gson.toJson(res));
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader in = request.getReader();
        PartyDB p = gson.fromJson(in, PartyDB.class);
        String val = PartyManagerDB.getManager().addNewParty(p);
        if (!val.isEmpty()) response.sendRedirect(getServletContext().getContextPath() +
                ("/parties?shortname=%s").formatted(val));
        else response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String partyShortName = request.getParameter("party");
        String toPut = request.getParameter("method");
        PartyDB party = PartyManagerDB.getManager().getParty(partyShortName);

        if(party == null || toPut == null  || toPut.isEmpty()){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        if("changename".equals(toPut)){
            String newName = request.getParameter("newname");
            if(PartyManagerDB.getManager().changePartyName(party, newName)){
                response.getWriter().println("Done");
            } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }else{
            String idStr = request.getParameter("id");
            try {
                int id = Integer.parseInt(idStr);

                if("removemember".equals(toPut)){
                    if(PartyManagerDB.getManager().removePartyMember(party, id)){
                        response.getWriter().println("Done");
                    } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }else if("addmember".equals(toPut)){
                    if(PartyManagerDB.getManager().addPartyMember(party, id)){
                        response.getWriter().println("Done");
                    } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }else response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }catch (Exception e){
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }

    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String shortName = request.getParameter("shortname");

        PrintWriter out = response.getWriter();
        if (shortName != null) {
            boolean deleted = PartyManagerDB.getManager().deleteParty(shortName);
            out.println(deleted);
        } else response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    public void destroy() {
    }
}