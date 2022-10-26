package servlets.agent_servlets;

import DTO.DTOAgentMetaData;
import battlefield.Battlefield;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sun.management.resources.agent;
import user_types.agent.Agent;
import user_types.agent.AgentConstant;
import user_types.ally.Ally;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "servlets.GetMetaDataServlet", urlPatterns = "/get_meta_data")
public class GetMetaDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Battlefield battlefield = ServletUtils.getBattlefieldFromSession(req.getSession(), getServletContext());
        Agent agent = (Agent) req.getSession().getAttribute(AgentConstant.AGENT_OBJECT);
        Ally ally = ServletUtils.getAllyManager(getServletContext()).getAlly(agent.getConnectedAlly());
        if (battlefield != null && battlefield.getUboat().getIsReady()) {
            DTOAgentMetaData dtoAgentMetaData = new DTOAgentMetaData(ally.getAllyName(), battlefield.getProcessMessage(), agent.getNumOfThreads());

            Gson gson = new Gson();
            try (PrintWriter out = resp.getWriter()) {
                String JsonResponse = gson.toJson(dtoAgentMetaData);
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                out.print(JsonResponse);
                out.flush();
            }
        }
        else
        {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
