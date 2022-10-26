package servlets;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import utils.ServletConstants;
import utils.ServletUtils;
import utils.UserType;
import java.io.IOException;

@WebServlet(name = "servlets.loginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet

            String usernameFromParameter = request.getParameter(ServletConstants.USER_NAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                //no username in session and no username in parameter - not standard situation. it's a conflict

                // stands for conflict in server state
                response.setStatus(HttpServletResponse.SC_CONFLICT);

            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();

                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";

                        // stands for unauthorized as there is already such user with this name
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(errorMessage);
                    }
                    else {
                        String userTypeString = request.getParameter(ServletConstants.USER_TYPE);
                        UserType userType = UserType.strToUserType(userTypeString);

                        switch (userType) {
                            case UBOAT:
                                request.getSession(true).setAttribute(UboatConstant.UBOAT_OBJECT, new UBoat(usernameFromParameter));
                                break;
                            case ALLY:
                                AllyManager allyManager = ServletUtils.getAllyManager(getServletContext());
                                Ally allyObject = new Ally(usernameFromParameter);
                                allyManager.addAlly(usernameFromParameter, allyObject);
                                request.getSession(true).setAttribute(AllyConstant.ALLY_OBJECT, allyObject);
                                break;
                            case AGENT:
                                //extract parameters
                                allyManager = ServletUtils.getAllyManager(getServletContext());
                                String allyNameFromParameter = request.getParameter(ServletConstants.ALLY_NAME);
                                int numThreadsFromParameter = Integer.parseInt(request.getParameter(AgentConstant.NUM_OF_THREADS));
                                int numMissionsFromParameter = Integer.parseInt(request.getParameter(AgentConstant.NUM_OF_MISSIONS));

                                Ally ally = allyManager.getAlly(allyNameFromParameter);

                                if(ally == null){
                                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                    response.getWriter().println("ally chosen doesnt exist");
                                    return;
                                }

                                Agent agent = new Agent(usernameFromParameter, ally.getAllyName(), numThreadsFromParameter, numMissionsFromParameter);
                                ally.addAgent(agent);
                                request.getSession(true).setAttribute(AgentConstant.AGENT_OBJECT, agent);
                                System.out.println("created agent with name " + usernameFromParameter + " ally: " + allyNameFromParameter);
                                break;

                            default:
                                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                response.getWriter().println("invalid role selected for login");
                        }
                        //add the new user to the users list
                        userManager.addUser(usernameFromParameter);

                        //redirect the request to the chat room - in order to actually change the URL
                        System.out.println("On login, request URI is: " + request.getRequestURI());
                        response.setStatus(HttpServletResponse.SC_OK); //200
                    }
                }
            }
        } else {
            //user is already logged in
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object sessionAttribute = session != null ? session.getAttribute(ServletConstants.USER_NAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }
}

