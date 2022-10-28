package app.main_app;
import DTO.DTOAgentMetaData;
import DTO.DTOCandidate;
import DTO.DTOMission;
import app.main_app.components.agents_candidates.CandidateDetailsController;
import app.main_app.components.agents_candidates.CandidateModel;
import app.main_app.components.dm_progress.DMProgressController;
import app.main_app.components.contest_data.ContestAndTeamController;
import http.HttpClientUtil;
import app.utils.trie.Trie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import engine.EncryptionMachineEngine;
import engine.EnigmaMachineEngine;
import engine.machine.components.enigma_factory.enigma.generated.CTEDictionary;
import engine.machine.components.enigma_factory.enigma.generated.CTEEnigma;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static app.utils.AppConstants.BASE_URL;
import static engine.machine.Machine.JAXB_XML_GAME_PACKAGE_NAME;
import static http.HttpClientUtil.REFRESH_RATE;

public class AgentAppController {
    @FXML
    Stage primaryStage;
    @FXML
    HBox contestDataComponent;
    @FXML
    ContestAndTeamController contestDataComponentController;
    @FXML
    TableView<CandidateModel> agentsCandidateComponent;
    @FXML
    CandidateDetailsController agentsCandidateComponentController;
    @FXML
    VBox dmProgressComponent;
    @FXML
    DMProgressController dmProgressComponentController;

    SimpleStringProperty allyName;
    SimpleStringProperty agentName;
    SimpleStringProperty processedString;
    SimpleIntegerProperty numOfThreads;
    SimpleBooleanProperty isBattlefieldExists;

    SimpleObjectProperty<EncryptionMachineEngine> engine;
    SimpleObjectProperty<Trie> trie;
    Timer timer;
    ExecutorService executorService;
    List<Future<?>> futureList;


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public AgentAppController() {
        isBattlefieldExists = new SimpleBooleanProperty(false);
        engine = new SimpleObjectProperty<>(new EnigmaMachineEngine());
        trie = new SimpleObjectProperty<>(new Trie());
        allyName = new SimpleStringProperty();
        agentName = new SimpleStringProperty();
        processedString = new SimpleStringProperty();
        numOfThreads = new SimpleIntegerProperty();
        timer = new Timer();
        futureList = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        contestDataComponentController.setParentController(this);
        contestDataComponentController.getContestInfo();
        contestDataComponentController.setIsBattlefieldExists(isBattlefieldExists);

        checkIfMissionPoolIsEmpty();
        getMetaData();
        isBattlefieldExists.addListener(observable -> {
            HttpClientUtil.runAsync(BASE_URL + "/get_engine", new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    assert response.body() != null;
                    String engineString = response.body().string();
                    Platform.runLater(() -> {
                        CTEEnigma cteEnigma;
                        try {
                            cteEnigma = deserializeJAXB(engineString);
                            setTrie(cteEnigma.getCTEDecipher().getCTEDictionary());
                            engine.get().loadXMLFile(cteEnigma);
                        } catch (JAXBException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            });
        });
    }

    public CTEEnigma deserializeJAXB(String xmlFile) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();

        StringReader reader = new StringReader(xmlFile);
        return (CTEEnigma) u.unmarshal(reader);
    }

    public void setTrie(CTEDictionary cteDictionary) {
        Arrays.stream(cteDictionary.getWords().toUpperCase().replaceAll(String.format("[%s]", cteDictionary.getExcludeChars()), "")
                        .split(" "))
                .collect(Collectors.toList())
                .forEach(word -> trie.get().insert(word));
    }

    @FXML
    public void getMetaData() {
        TimerTask getMetadataTask = new TimerTask() {
            @Override
            public void run() {
                HttpClientUtil.runAsync(BASE_URL + "/get_meta_data", new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        assert response.body() != null;
                        DTOAgentMetaData agentMetaData = new Gson().fromJson(response.body().string(), DTOAgentMetaData.class);
                        if (response.isSuccessful()) {
                            //allyName.set(agentMetaData.getAllyName());
                            //numOfThreads.set(agentMetaData.getNumOfThreads());
                            processedString.set(agentMetaData.getProcessedString());
                        }
                    }
                });
            }
        };
        timer.schedule(getMetadataTask, REFRESH_RATE, REFRESH_RATE);
    }

    @FXML
    public void sendGetMission() {
        if (contestDataComponentController.getIsWinnerExists())
            return;

        HttpClientUtil.runAsync(BASE_URL + "/get_missions", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                Consumer<Collection<DTOCandidate>> updateCandidatesMetrics = (Collection<DTOCandidate> candidates) -> {
                    List<CandidateModel> candidateModels = candidates.stream()
                            .map(candidate -> new CandidateModel(candidate.getInitialPosition(), candidate.getRotorsPosition(), String.valueOf(candidate.getReflector()), candidate.getCandidateString()))
                            .collect(Collectors.toList());

                    Platform.runLater(() -> {
                        dmProgressComponentController.getNumOfCandidatesCreated().set(dmProgressComponentController.getNumOfCandidatesCreated().get() + candidateModels.size());
                        agentsCandidateComponentController.addCandidates(candidateModels);
                    });
                };
                Consumer<Integer> updateMissionsTaken = (Integer integer) -> {
                    Platform.runLater(() -> dmProgressComponentController.getNumOfMissionsTaken().set(dmProgressComponentController.getNumOfMissionsTaken().get() + integer));
                };

                if (response.isSuccessful()) {
                    List<DTOMission> missions = new Gson().fromJson(response.body().string(), new TypeToken<List<DTOMission>>() {
                    }.getType());
                    futureList = missions.stream()
                            .map(mission -> executorService.submit(new DecryptionMission(mission, trie.get(), engine.get(), processedString.get(), allyName.get(), agentName.get(), updateCandidatesMetrics, updateMissionsTaken)))
                            .collect(Collectors.toList());
                }
            }
        });
    }

    @FXML
    public void checkIfMissionPoolIsEmpty() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                boolean allDone = futureList.stream().allMatch(Future::isDone);
                if (allDone) {
                    Platform.runLater(() -> {
                        dmProgressComponentController.getNumOfCompletedMissions().set(dmProgressComponentController.getNumOfCompletedMissions().get() + futureList.size());
                        futureList.clear();
                        sendGetMission();
                    });
                }
            }
        };
        timer.schedule(timerTask, 200, 200);
    }

    public void setAgentName(String agentName) {
        this.agentName.set(agentName);
    }

    public void setAllyName(String allyName) {
        this.allyName.set(allyName);
    }

    public void setNumOfThreads(int numOfThreads) {
        if (executorService != null) {
            throw new RuntimeException("num of threads cannot be set twice");
        }
        this.numOfThreads.set(numOfThreads);
        executorService = Executors.newFixedThreadPool(numOfThreads);
    }

    public void cleanAllData() {
        for (Future<?> future : futureList)
            future.cancel(true);
        futureList.clear();

        agentsCandidateComponentController.cleanAllData();
        contestDataComponentController.cleanAllData();
        dmProgressComponentController.cleanData();
    }

    public String getAllyName() {
        return allyName.get();
    }
}

