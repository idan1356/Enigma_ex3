package servlets.agent_servlets;
import DTO.DTOCandidate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user_types.agent.Agent;
import user_types.agent.AgentConstant;
import user_types.ally.Ally;
import utils.ServletUtils;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;


@WebServlet(name = "servlets.UpdateCandidateListServlet", urlPatterns = "/update_candidate_list")
public class UpdateCandidateListServlet extends HttpServlet {
    private static final String CANDIDATE_LIST = "candidatelist";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Agent agent = (Agent) req.getSession().getAttribute(AgentConstant.AGENT_OBJECT);
        Ally ally = ServletUtils.getAllyManager(getServletContext()).getAlly(agent.getConnectedAlly());

        Scanner s = new Scanner(req.getReader());
        String result = s.hasNext() ? s.next() : "";

        List<DTOCandidate> candidates = new Gson().fromJson(result, new TypeToken<List<DTOCandidate>>() {}.getType());
        ally.addCandidate(candidates);
        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        resp.getWriter().println("candidates have been added successfully");
    }
}
