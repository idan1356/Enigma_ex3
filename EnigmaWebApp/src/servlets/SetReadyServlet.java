package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import user_types.ally.Ally;
import user_types.ally.AllyConstant;
import user_types.uboat.UBoat;
import user_types.uboat.UboatConstant;

import java.io.IOException;

@WebServlet(name = "servlets.SetReadyServlet", urlPatterns = {"/set_ready"})
public class SetReadyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UBoat uBoat = (UBoat) req.getSession().getAttribute(UboatConstant.UBOAT_OBJECT);
        Ally ally = (Ally) req.getSession().getAttribute(AllyConstant.ALLY_OBJECT);

        if(uBoat != null){
            uBoat.setReadyStatus(true);
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        } else {
            ally.setReadyStatus(true);
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        }

    }
}
