package servlets.uboat_servlets.get_info;
import com.google.gson.Gson;
import engine.machine.components.enigma_factory.enigma.generated.CTEDecipher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import battlefield.Battlefield;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "servlets.uboat_servlets.get_info.GetDecipherStateServlet", urlPatterns = "/decipher_state")
public class GetDecipherStateServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Battlefield battlefield = ServletUtils.getBattlefieldFromSession(request.getSession(), getServletContext());
        CTEDecipher cteDecipher = battlefield.getDecipher();

        Gson gson = new Gson();
        String decipherJsonString = gson.toJson(cteDecipher);

        try (PrintWriter out = response.getWriter()) {
            out.print(decipherJsonString);
            out.flush();
        }
    }
}
