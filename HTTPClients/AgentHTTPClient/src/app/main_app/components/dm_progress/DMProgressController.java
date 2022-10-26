package app.main_app.components.dm_progress;
import http.HttpClientUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import static app.utils.AppConstants.BASE_URL;

public class DMProgressController {
    @FXML Label numOfCompletedMissionsLabel;
    @FXML Label numOfMissionsTakenLabel;
    @FXML Label numOfCandidatesLabel;
    SimpleIntegerProperty numOfCompletedMissions;
    SimpleIntegerProperty numOfMissionsTaken;
    SimpleIntegerProperty numOfCandidatesCreated;

    @FXML
    public void initialize(){
        numOfCompletedMissionsLabel.textProperty().bind(numOfCompletedMissions.asString());
        numOfMissionsTakenLabel.textProperty().bind(numOfMissionsTaken.asString());
        numOfCandidatesLabel.textProperty().bind(numOfCandidatesCreated.asString());

        numOfCompletedMissions.addListener(observable -> updateAgentProgress());
        numOfMissionsTaken.addListener(observable -> updateAgentProgress());
        numOfCandidatesCreated.addListener(observable -> updateAgentProgress());
    }

    public DMProgressController(){
        numOfCompletedMissions = new SimpleIntegerProperty(0);
        numOfMissionsTaken = new SimpleIntegerProperty(0);
        numOfCandidatesCreated = new SimpleIntegerProperty(0);
    }

    public SimpleIntegerProperty getNumOfCompletedMissions() {
        return numOfCompletedMissions;
    }

    public SimpleIntegerProperty getNumOfMissionsTaken() {
        return numOfMissionsTaken;
    }

    public SimpleIntegerProperty getNumOfCandidatesCreated() {
        return numOfCandidatesCreated;
    }

    @FXML
    public void updateAgentProgress() {
        HttpUrl url =  Objects.requireNonNull(HttpUrl.parse(BASE_URL + "/update_agent_progress")).newBuilder()
                .addQueryParameter("missionsfinished", String.valueOf(numOfCompletedMissions.get()))
                .addQueryParameter("missionstaken", String.valueOf(numOfMissionsTaken.get()))
                .addQueryParameter("candidatescreated", String.valueOf(numOfCandidatesCreated.get()))
                .build();

        HttpClientUtil.runAsync(url.toString(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {

            }
        });

    }}
