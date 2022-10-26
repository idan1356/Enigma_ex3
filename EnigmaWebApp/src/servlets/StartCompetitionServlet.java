package servlets;

import battlefield.Battlefield;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user_types.ally.Ally;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "servlets.StartCompetitionServlet", urlPatterns = {"/start_competition"})
public class StartCompetitionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Battlefield battlefield = ServletUtils.getBattlefieldFromSession(req.getSession(), getServletContext());
        boolean allAlliesReady = battlefield.getAllyList().stream().allMatch(Ally::getIsReady);
        boolean uboatIsReady = battlefield.getUboat().getIsReady();
        boolean competitionFull = battlefield.isFull();

        synchronized (this) {
            if (battlefield.getIsStarted()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            if (uboatIsReady && allAlliesReady && competitionFull) {
                System.out.println("competition started");
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
                resp.getWriter().println("competition started successfully");
                battlefield.getAllyList().forEach(
                        ally -> ally.startCompetition(battlefield.getEngine(), battlefield.getLevel())
                );
                battlefield.setIsStarted(true);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("not all players are ready");
            }
        }
    }
}
