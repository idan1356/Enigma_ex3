package app.main_app.contest;

import DTO.DTOCandidate;
import DTO.DTODmprogress;
import app.main_app.AllyAppController;
import app.main_app.contest.agent_progress.AgentProgressController;
import app.main_app.contest.agent_progress.AgentProgressModel;
import app.main_app.contest.ally_active_teams_details.ActiveTeamsDetailsController;
import app.main_app.contest.ally_active_teams_details.TeamInfoModel;
import app.main_app.contest.candidate_details.AgentCandidatesDataController;
import app.main_app.contest.contest_data.ContestDataController;
import com.google.gson.Gson;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static app.utils.AppConstants.BASE_URL;
import static http.HttpClientUtil.REFRESH_RATE;

public class ContestController {
    @FXML FlowPane contestDataComponent;
    @FXML ContestDataController contestDataComponentController;
    @FXML TableView<AgentProgressModel> agentProgressComponent;
    @FXML AgentProgressController agentProgressComponentController;
    @FXML TableView<TeamInfoModel> activeTeamsDetailsComponent;
    @FXML ActiveTeamsDetailsController activeTeamsDetailsComponentController;
    @FXML TableView<DTOCandidate> candidateProgressComponent;
    @FXML AgentCandidatesDataController candidateProgressComponentController;

    @FXML Label numOfFinishedMissions;
    @FXML Label nunOfTotalMissions;
    @FXML Label numOfCreatedMissions;
    Timer timer = new Timer();

    AllyAppController parentController;

    @FXML
    public void initialize(){
        updateProgress();

        contestDataComponentController.setParentController(this);
    }

    public void updateProgressLabels(DTODmprogress dmprogress){
        numOfFinishedMissions.setText(String.valueOf(dmprogress.getNumOfFinishedMissions()));
        nunOfTotalMissions.setText(String.valueOf(dmprogress.getNumOfTotalMissions()));
        numOfCreatedMissions.setText(String.valueOf(dmprogress.getNumOfMissionsCreated()));
    }

    public void updateProgress(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                HttpClientUtil.runAsync(BASE_URL + "/get_dm_progress", new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if(response.isSuccessful()){
                            assert response.body() != null;
                            DTODmprogress dmProgress = new Gson().fromJson(response.body().string(), DTODmprogress.class);
                            Platform.runLater(()->updateProgressLabels(dmProgress));
                        }
                    }
                });
            }
        };

        timer.schedule(timerTask, REFRESH_RATE, REFRESH_RATE);
    }

    public void setParentController(AllyAppController parentController) {
        this.parentController = parentController;
    }

    public AllyAppController getParentController() {
        return parentController;
    }

    public void cleanAllData(){
        candidateProgressComponentController.cleanAllData();
    }
}
