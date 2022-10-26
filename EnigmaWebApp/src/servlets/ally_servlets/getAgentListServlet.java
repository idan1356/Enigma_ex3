package servlets.ally_servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user_types.ally.Ally;
import user_types.ally.AllyConstant;

import java.io.IOException;
import java.io.PrintWriter;
 @WebServlet(name = "servlets.getAgentListServlet", urlPatterns = "/get_all_agents")
public class getAgentListServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            Ally ally = (Ally) request.getSession().getAttribute(AllyConstant.ALLY_OBJECT);

            if (ally == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("user is not permitted for this action");
            }

            assert ally != null;
            Gson gson = new Gson();
            String agentsJsonResponse = gson.toJson(ally.getAgents());

            try (PrintWriter out = response.getWriter()) {
                out.print(agentsJsonResponse);
                out.flush();
            }
        }
}
