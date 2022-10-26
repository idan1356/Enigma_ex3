package servlets.uboat_servlets.actions;

import engine.EncryptionMachineEngine;
import engine.EnigmaMachineEngine;
import engine.machine.components.enigma_factory.enigma.generated.CTEBattlefield;
import engine.machine.components.enigma_factory.enigma.generated.CTEDecipher;
import engine.machine.components.enigma_factory.enigma.generated.CTEEnigma;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import managers.BattlefieldManager;
import misc.BruteForceDifficulty;
import battlefield.Battlefield;
import user_types.uboat.UBoat;
import user_types.uboat.UboatConstant;
import utils.ServletUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static engine.machine.Machine.JAXB_XML_GAME_PACKAGE_NAME;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
@WebServlet(name = "servlets.uploadFileServlet", urlPatterns = "/upload_file")
public class FileUploadServlet extends HttpServlet {

    private final static String XML_FILE_CONSTANT = "xmlfile";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain");
        Part part = request.getPart(XML_FILE_CONSTANT);

        EncryptionMachineEngine engine = new EnigmaMachineEngine();

        try {
            CTEEnigma cteEnigma = deserializeJAXB(part.getInputStream());
            CTEDecipher decipher = cteEnigma.getCTEDecipher();
            CTEBattlefield battlefield = cteEnigma.getCTEBattlefield();

            //validate file
            engine.loadXMLFile(cteEnigma);

            //get data
            BruteForceDifficulty difficulty = BruteForceDifficulty.strToDifficulty(battlefield.getLevel());
            Battlefield battleField = new Battlefield(battlefield.getBattleName(),battlefield.getAllies(), difficulty);
            battleField.setEngine(engine);
            battleField.setDecipher(decipher);


            part.getInputStream().reset();
            battleField.setFileInputStream(readFromInputStream(part.getInputStream()));
            BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());

            synchronized (this){
                if (battlefieldManager.isBattlefieldExists(battlefield.getBattleName())) {
                    String errorMessage = "Battlefield " + battlefield.getBattleName() + " already exists. Please enter a different username.";
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//405
                    response.getOutputStream().print(errorMessage);
                }
                else{
                    battlefieldManager.addBattlefield(battlefield.getBattleName(), battleField);
                    UBoat uboat = (UBoat) request.getSession().getAttribute(UboatConstant.UBOAT_OBJECT);
                    battleField.setUboat(uboat);
                    uboat.setBattlefield(battleField.getBattlefieldName());
                    System.out.println("On login, request URI is: " + request.getRequestURI());
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }

        } catch (JAXBException | RuntimeException | ClassNotFoundException e) {
            System.out.println( e.getClass().getCanonicalName());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
        }
    }

    public CTEEnigma deserializeJAXB(InputStream input) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(input);
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }
}
