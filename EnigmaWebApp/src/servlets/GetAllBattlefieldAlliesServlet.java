package servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import battlefield.Battlefield;
import user_types.uboat.UBoat;
import user_types.uboat.UboatConstant;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "servlets.GetAllBattlefieldAllies", urlPatterns = "/get_allies_from_battlefield")
public class GetAllBattlefieldAlliesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Battlefield battlefield = ServletUtils.getBattlefieldFromSession(request.getSession(), getServletContext());

        if (battlefield == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("user is not permitted for this action");
        }

        Gson gson = new Gson();
        assert battlefield != null;
        String alliesJsonResponse = gson.toJson(battlefield.getAllyList());

        try (PrintWriter out = response.getWriter()) {
            out.print(alliesJsonResponse);
            out.flush();
        }
    }
}
