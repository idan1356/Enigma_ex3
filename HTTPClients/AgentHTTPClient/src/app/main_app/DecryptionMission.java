package app.main_app;
import DTO.DTOCandidate;
import DTO.DTOMission;
import app.main_app.utils.PositionConversionUtils;
import http.HttpClientUtil;
import app.utils.trie.Trie;
import com.google.gson.Gson;
import engine.EncryptionMachineEngine;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import static app.utils.AppConstants.BASE_URL;

public class DecryptionMission implements Runnable{
    Trie dictionary;
    EncryptionMachineEngine engine;
    DTOMission dtoMission;
    String processedMessage;
    String allyName;
    String agentName;
    List<DTOCandidate> candidateList;
    Consumer<Collection<DTOCandidate>> updateCandidatesDelegate;
    Consumer<Integer> updateNumOfMissionsTakenDelegate;

    public DecryptionMission(DTOMission dtoMission, Trie dictionary,
                             EncryptionMachineEngine engine, String processedMessage,String allyName, String agentName,
                             Consumer<Collection<DTOCandidate>> candidatesConsumer, Consumer<Integer> numOfMissionsTakenConsumer){
        this.dictionary = dictionary;
        this.engine = engine;
        this.dtoMission = dtoMission;
        this.processedMessage = processedMessage;
        this.allyName = allyName;
        this.candidateList = new ArrayList<>();
        this.updateCandidatesDelegate =  candidatesConsumer;
        this.updateNumOfMissionsTakenDelegate = numOfMissionsTakenConsumer;
        this.agentName = agentName;
    }
    @Override
    public void run() {
        PositionConversionUtils positionConversionUtils1 = new PositionConversionUtils(engine.getSpecs().getMachineSpecs().getABC(), engine.getSpecs().getMachineSpecs().getRotorsUsedCount());
        int startPosition = dtoMission.getStartingPosition();
        int endPosition = startPosition + dtoMission.getMissionSize();
        engine.setReflectors(dtoMission.getReflector());
        engine.setPlugboard("");

        String rotors1 = dtoMission.getRotors().toString().replaceAll(" ", "");
        String rotors = rotors1.substring(1, rotors1.length() - 1);
        engine.setRotors(rotors);

        updateNumOfMissionsTakenDelegate.accept(1);

        for(int i = startPosition; i < endPosition; i++){
            String curPosition = positionConversionUtils1.intToRotorPosition(i);
            System.out.println(curPosition);
            engine.setInitialPosition(curPosition);
            engine.setEnigmaSettings();
            String decoded = engine.processInput(processedMessage).getOutput();
            boolean allWordsInDict = Arrays.stream(decoded.split(" ")).allMatch(word -> dictionary.find(word));

            if(allWordsInDict){
                candidateList.add(new DTOCandidate(allyName, dtoMission.getReflector(),curPosition,
                        decoded, rotors, agentName));
                System.out.println("this is the candidate string-" + decoded);
            }
        }
        afterRun();
    }

    public void afterRun(){
        if(candidateList.isEmpty())
            return;

        String jsonCandidateList = new Gson().toJson(candidateList);

        HttpClientUtil.runAsyncPost(BASE_URL + "/update_candidate_list", RequestBody.create(jsonCandidateList.getBytes()), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    updateCandidatesDelegate.accept(candidateList);
                }
             }
        });
    }
}
