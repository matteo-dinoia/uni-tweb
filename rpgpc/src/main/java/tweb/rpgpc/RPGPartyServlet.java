package tweb.rpgpc;

import java.io.*;
import java.util.Arrays;
import java.util.Map;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import tweb.rpgpc.rpg.PartyManager;

/* URL Patterns
    /party --> per il momento mostra delle istruzioni per l'uso
    /party/[nome-party] --> mostra il party [nome-party] senza nessun personaggio selezionato
    /party/[nome-party]?charid=N --> mostra il party da visualizzare selezionando il personaggio con id = N

    se non esiste un party [nome-party], o se in esso non c'è un personaggio con id=N,
    deve venire generato un errore 404 not found
 */
@WebServlet(name = "RPGPC-Party-Servlet", urlPatterns = {"/party/view/*"})
public class RPGPartyServlet extends HttpServlet {

    public void init() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");


        PrintWriter out = response.getWriter();
        out.println("<h1>TODO</h1>");
    }

    public void destroy() {
    }
}