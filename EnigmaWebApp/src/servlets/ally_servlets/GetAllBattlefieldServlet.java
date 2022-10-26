package servlets.ally_servlets;

import DTO.DTOContestInfo;
import battlefield.Battlefield;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import managers.BattlefieldManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "servlets.GetAllBattlefieldServlet", urlPatterns = "/get_all_battlefield")
public class GetAllBattlefieldServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
        Collection<Battlefield> battlefieldList = battlefieldManager.getAllBattlefields();
        List<DTOContestInfo> lst = battlefieldList.stream()
                .map(battlefield -> new DTOContestInfo(
                    battlefield.getBattlefieldName(),
                    battlefield.getUboatName(),
                    battlefield.getContestStatus(),
                    battlefield.getLevel().name(),
                battlefield.getCurrentAlliesNum() + "/" + battlefield.getTotalAllyNumber(),
                    battlefield.getProcessMessage()))
                .collect(Collectors.toList());

        try (PrintWriter out = response.getWriter()) {
            out.print(new Gson().toJson(lst));
            out.flush();
        }
    }
}
