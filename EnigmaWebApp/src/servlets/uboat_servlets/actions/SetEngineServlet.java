package servlets.uboat_servlets.actions;

import engine.EncryptionMachineEngine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import battlefield.Battlefield;
import user_types.uboat.UboatConstant;
import utils.ServletConstants;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "servlets.uboat_servlets.actions.SetEngineServlet", urlPatterns = {"/set_engine"})
public class SetEngineServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException {
         String reflector = request.getParameter(ServletConstants.REFLECTOR);
         String rotors = request.getParameter(ServletConstants.ROTORS);
         String initializePosition  = request.getParameter(ServletConstants.INITIALIZEPOSITION);
         if(request.getSession().getAttribute(UboatConstant.UBOAT_OBJECT) == null){
              response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
              response.getWriter().println("user is not permitted for this action");
              return;
         }

         Battlefield battlefield = ServletUtils.getBattlefieldFromSession(request.getSession(), getServletContext());
         battlefield.setProcessMessage(null);
         battlefield.setOriginalMessage(null);

         EncryptionMachineEngine engine = battlefield.getEngine();
         engine.setPlugboard("");
         engine.setRotors(rotors);
         engine.setInitialPosition(initializePosition);
         engine.setReflectors(Integer.parseInt(reflector));
         engine.setEnigmaSettings();
         response.setStatus(HttpServletResponse.SC_OK);
         response.getWriter().println("engine was updated successfully");
    }
}
