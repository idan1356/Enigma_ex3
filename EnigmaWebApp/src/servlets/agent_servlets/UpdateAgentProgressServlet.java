package servlets.agent_servlets;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user_types.agent.Agent;
import user_types.agent.AgentConstant;

import java.io.IOException;

@WebServlet(name = "servlets.UpdateAgentProgress", urlPatterns = "/update_agent_progress")
public class UpdateAgentProgressServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Agent agent = (Agent) req.getSession().getAttribute(AgentConstant.AGENT_OBJECT);

        if (agent == null){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("user is not permitted for this action");
            return;
        }
        try {
            int missionsFinishedCount = Integer.parseInt(req.getParameter(AgentConstant.MISSIONS_FINISHED_COUNT));
            int missionsTakenCount = Integer.parseInt(req.getParameter(AgentConstant.MISSIONS_TAKEN_COUNT));
            int candidatesCreated = Integer.parseInt(req.getParameter(AgentConstant.CANDIDATES_CREATED));

            agent.setCandidatesCreated(candidatesCreated);
            agent.setMissionsFinishedCount(missionsFinishedCount);
            agent.setMissionsTakenCount(missionsTakenCount);
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            resp.getWriter().print("agent progress updated successfully");
        } catch (NumberFormatException e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("invalid input provided");
        }
    }
}
