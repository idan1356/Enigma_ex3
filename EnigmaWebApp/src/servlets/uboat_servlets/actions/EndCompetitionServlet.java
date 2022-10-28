package servlets.uboat_servlets.actions;

import battlefield.Battlefield;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user_types.uboat.UBoat;
import user_types.uboat.UboatConstant;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "servlets.EndCompetitionServlet", urlPatterns = "/reset_competition")
public class EndCompetitionServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UBoat uBoat = (UBoat) req.getSession().getAttribute(UboatConstant.UBOAT_OBJECT);
        Battlefield battlefield = ServletUtils.getBattlefieldFromSession(req.getSession(), getServletContext());

        if(uBoat == null || battlefield == null || battlefield.getWinner() == null){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("invalid request");
            return;
        }

        battlefield.reset();
        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        resp.getWriter().print("competition was reset successfully");
    }
}
