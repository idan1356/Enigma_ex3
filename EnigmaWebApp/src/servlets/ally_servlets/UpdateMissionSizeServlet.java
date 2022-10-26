package servlets.ally_servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user_types.ally.Ally;
import user_types.ally.AllyConstant;
import utils.ServletConstants;

import java.io.IOException;

@WebServlet(name = "servlets.UpdateMissionSizeServlet", urlPatterns = "/update_mission")
public class UpdateMissionSizeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Ally ally = (Ally) req.getSession().getAttribute(AllyConstant.ALLY_OBJECT);
        int missionSizeFromParameter = Integer.parseInt(req.getParameter(ServletConstants.MISSION_SIZE));

        if(ally == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("user is not permitted for this type of action");
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        ally.setMissionSize(missionSizeFromParameter);
    }
}
