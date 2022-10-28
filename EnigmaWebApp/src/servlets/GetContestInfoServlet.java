package servlets;

import DTO.DTOContestInfo;
import battlefield.Battlefield;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "servlets.GetContestInfoServlet", urlPatterns = "/get_battlefield")
public class GetContestInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Battlefield battlefield = ServletUtils.getBattlefieldFromSession(req.getSession(), getServletContext());

        if(battlefield == null){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        DTOContestInfo contestInfo = new DTOContestInfo(
                battlefield.getBattlefieldName(),
                battlefield.getUboatName(),
                battlefield.getContestStatus(),
                battlefield.getLevel().name(),
                battlefield.getCurrentAlliesNum() + "/" + battlefield.getTotalAllyNumber(),
                battlefield.getProcessMessage(),
                battlefield.getWinner());
        
        Gson gson = new Gson();
        String contestJsonString = gson.toJson(contestInfo);
        try (PrintWriter out = resp.getWriter()) {
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            out.print(contestJsonString);
            out.flush();
        }
    }
}
