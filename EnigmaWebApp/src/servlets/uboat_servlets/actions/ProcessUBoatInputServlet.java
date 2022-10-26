package servlets.uboat_servlets.actions;

import engine.EncryptionMachineEngine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import battlefield.Battlefield;
import utils.ServletConstants;
import utils.ServletUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "servlets.uboat_servlets.actions.ProcessUBoatInputServlet", urlPatterns = {"/process_uboat_input"})
public class ProcessUBoatInputServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        //get parameters and objects
        String message = request.getParameter(ServletConstants.UBOAT_INPUT);
        Battlefield battlefield = ServletUtils.getBattlefieldFromSession(request.getSession(), getServletContext());

        message = message.replaceAll(
                String.format("[%s]", battlefield.getDecipher().getCTEDictionary().getExcludeChars()
                ), "");

        //validate if all words in dictionary
        List<String> words = Arrays.stream(battlefield.getDecipher().getCTEDictionary().getWords().trim().toUpperCase()
                .replaceAll(String.format("[%s]", battlefield.getDecipher().getCTEDictionary().getExcludeChars()), "")
                .split(" ")).collect(Collectors.toList());
        boolean allWordsInDict = Arrays.stream(message.split(" ")).allMatch(words::contains);

        if(allWordsInDict) {
            //decode string and save processed message
            EncryptionMachineEngine engine = battlefield.getEngine();
            String encoded = engine.processInput(message).getOutput();
            battlefield.setOriginalMessage(message);
            battlefield.setProcessMessage(encoded);

            response.addHeader("original", message);
            response.addHeader("processed", encoded);

            response.setStatus(HttpServletResponse.SC_ACCEPTED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
