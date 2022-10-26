package servlets.agent_servlets;

import battlefield.Battlefield;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user_types.agent.AgentConstant;
import utils.ServletUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@WebServlet(name = "servlets.GetEngineServlet", urlPatterns = "/get_engine")
public class GetEngineServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Battlefield battlefield = ServletUtils.getBattlefieldFromSession(req.getSession(), getServletContext());

        if (req.getSession().getAttribute(AgentConstant.AGENT_OBJECT) == null || battlefield == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String inputStream = battlefield.getFileInputStream();

        try (PrintWriter out = resp.getWriter()) {
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            out.print(inputStream);
            out.flush();
        }
    }
}
