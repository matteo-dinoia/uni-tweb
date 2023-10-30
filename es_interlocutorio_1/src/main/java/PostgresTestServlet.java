import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

import data.Character;
import data.CharacterDB;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "es", value = "/characters")
public class PostgresTestServlet extends HttpServlet {
    CharacterDB cDB;

    public void init() {
        cDB = new CharacterDB();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String strId = request.getParameter("id");
        String strEdit = request.getParameter("edit");

        if(strId == null && strEdit == null){
            try{
                ArrayList<Character> list = cDB.getCharactersList();
                out.println("<!DOCTYPE html><html><body><ul>");
                for(int i = 0; list != null && i < list.size(); i++){
                    out.println("<li>" + list.get(i).toString() + "</li>");
                }
                out.println("</ul></body></html>");
            }catch (SQLException e) {response.sendError(404);}

        } else if(strId != null){
            if("true" == strEdit){

            }
        }else if("true" == strEdit){

        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    public void destroy() {
    }
}