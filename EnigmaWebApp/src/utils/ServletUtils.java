package utils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import managers.AllyManager;
import managers.BattlefieldManager;
import managers.UserManager;
import user_types.agent.Agent;
import user_types.agent.AgentConstant;
import user_types.ally.Ally;
import user_types.ally.AllyConstant;
import battlefield.Battlefield;
import user_types.uboat.UBoat;
import user_types.uboat.UboatConstant;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String BATTLEFIELD_MANAGER_ATTRIBUTE_NAME = "battlefieldManager";
    private static final String ALLY_MANAGER_ATTRIBUTE_NAME = "allyManager";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object userManagerLock = new Object();
    private static final Object battlefieldManagerLock = new Object();
    private static final Object allyManagerLock = new Object();

    public static Battlefield getBattlefieldFromSession(HttpSession httpSession, ServletContext servletContext){
        UBoat uBoat = (UBoat) httpSession.getAttribute(UboatConstant.UBOAT_OBJECT);
        Ally ally = (Ally) httpSession.getAttribute(AllyConstant.ALLY_OBJECT);
        Agent agent = (Agent)httpSession.getAttribute(AgentConstant.AGENT_OBJECT);

        String battlefieldName = null;
        if(uBoat != null)
            battlefieldName = uBoat.getBattlefieldName();
        if(ally != null) 
            battlefieldName = ally.getBattlefieldName();
        if(agent != null) {
            Ally agent_ally = getAllyManager(servletContext).getAlly(agent.getConnectedAlly());
            battlefieldName = agent_ally.getBattlefieldName();
        }

        return getBattlefieldManager(servletContext).getBattlefield(battlefieldName);
    }

    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static BattlefieldManager getBattlefieldManager(ServletContext servletContext) {

        synchronized (battlefieldManagerLock) {
            if (servletContext.getAttribute(BATTLEFIELD_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(BATTLEFIELD_MANAGER_ATTRIBUTE_NAME, new BattlefieldManager());
            }
        }
        return (BattlefieldManager) servletContext.getAttribute(BATTLEFIELD_MANAGER_ATTRIBUTE_NAME);
    }

    public static AllyManager getAllyManager(ServletContext servletContext) {
        synchronized (allyManagerLock) {
            if (servletContext.getAttribute(ALLY_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(ALLY_MANAGER_ATTRIBUTE_NAME, new AllyManager());
            }
        }
        return (AllyManager) servletContext.getAttribute(ALLY_MANAGER_ATTRIBUTE_NAME);
    }
}
