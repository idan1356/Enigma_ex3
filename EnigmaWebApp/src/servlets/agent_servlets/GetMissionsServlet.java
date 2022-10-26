package servlets.agent_servlets;

import battlefield.Battlefield;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user_types.agent.Agent;
import user_types.agent.AgentConstant;
import user_types.ally.Ally;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "servlets.GetMissionsServlet", urlPatterns = "/get_missions")
public class GetMissionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Agent agent = (Agent) req.getSession().getAttribute(AgentConstant.AGENT_OBJECT);
        Battlefield battlefield = ServletUtils.getBattlefieldFromSession(req.getSession(), getServletContext());
        if (agent == null || battlefield == null || !battlefield.getIsStarted()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("user is not permitted for this action");
            return;
        }
        Ally ally = ServletUtils.getAllyManager(getServletContext()).getAlly(agent.getConnectedAlly());

        Gson gson = new Gson();
        try (PrintWriter out = resp.getWriter()) {
            String MissionsJsonResponse = gson.toJson(ally.takeMissions(agent.getNumOfMissions()));
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            out.print(MissionsJsonResponse);
            out.flush();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

