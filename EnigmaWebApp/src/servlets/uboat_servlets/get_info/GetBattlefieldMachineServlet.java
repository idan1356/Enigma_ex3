package servlets.uboat_servlets.get_info;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import battlefield.Battlefield;
import utils.ServletUtils;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "servlets.uboat_servlets.get_info.GetBattlefieldMachineServlet", urlPatterns = "/get_enigma_details")
public class GetBattlefieldMachineServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Battlefield battlefield = ServletUtils.getBattlefieldFromSession(request.getSession(), getServletContext());

        Gson gson = new Gson();
        String specs = gson.toJson(battlefield.getEngine().getSpecs());

        try (PrintWriter out = response.getWriter()) {
            out.print(specs);
            out.flush();
        }
    }
}
