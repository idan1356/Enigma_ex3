package servlets.agent_servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.AllyManager;
import user_types.ally.Ally;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "servlets.GetAllAlliesServlet", urlPatterns = "/get_all_allies")
public class GetAllAlliesServlet extends HttpServlet{

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            AllyManager allyManager = ServletUtils.getAllyManager(getServletContext());

            try (PrintWriter out = response.getWriter()) {

                List<String> lst = allyManager.getAllAllies().stream()
                        .map(Ally::getAllyName)
                        .collect(Collectors.toList());

                response.setStatus(HttpServletResponse.SC_ACCEPTED);

                out.print(new Gson().toJson(lst));
                out.flush();
            }
        }
}
