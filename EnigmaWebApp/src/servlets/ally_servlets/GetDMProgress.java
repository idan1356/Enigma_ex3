package servlets.ally_servlets;
import DTO.DTODmprogress;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user_types.agent.Agent;
import user_types.ally.Ally;
import user_types.ally.AllyConstant;

import java.io.IOException;

@WebServlet(name = "servlets.GetDMProgress", urlPatterns = "/get_dm_progress")
public class GetDMProgress extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Ally ally = (Ally) req.getSession().getAttribute(AllyConstant.ALLY_OBJECT);

        if(ally == null){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("user is not permitted for this action");
        }

        assert ally != null;
        int numOfFinishedMissions = ally.getAgents().stream().mapToInt(Agent::getMissionsFinishedCount).sum();
        double numOfTotalMissions = ally.getTotalMissionCount();
        double numOfMissionsCreated = ally.getTotalMissionsCreated();

        DTODmprogress dmProgress = new DTODmprogress(numOfFinishedMissions, numOfMissionsCreated, numOfTotalMissions);

        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        resp.getWriter().print(new Gson().toJson(dmProgress));
    }
}
