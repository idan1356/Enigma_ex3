package servlets.ally_servlets;

import battlefield.Battlefield;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.BattlefieldManager;
import user_types.ally.Ally;
import user_types.ally.AllyConstant;
import utils.ServletConstants;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "servlets.JoinBattlefieldServlet", urlPatterns = "/ally_join_battlefield")
public class JoinBattlefieldServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Ally ally = (Ally) request.getSession().getAttribute(AllyConstant.ALLY_OBJECT);
        if(ally == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("user is not permitted for this type of action");
            return;
        }
        if(ally.getBattlefieldName() != null){
            //user already signed
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            response.getWriter().println(ally.getBattlefieldName());
            return;
        }

        synchronized (this){
            String battlefieldNameFromParameter = request.getParameter(ServletConstants.BATTLEFIELD_NAME);
            BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
            Battlefield battlefield = battlefieldManager.getBattlefield(battlefieldNameFromParameter);

            if(battlefield.isFull()){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("battlefield is already full, please a different battlefield");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(ally.getBattlefieldName());
            battlefield.addAlly(ally);
            ally.setBattlefield(battlefield.getBattlefieldName());
        }
    }
}
