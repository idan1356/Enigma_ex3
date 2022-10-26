package app.main_app.dashboard;

import app.main_app.dashboard.agents_details.AgentInfoController;
import app.main_app.dashboard.agents_details.AgentInfoModel;
import app.main_app.dashboard.contests_details.ContestDetailsController;
import app.main_app.dashboard.contests_details.ContestInfoModel;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import static app.utils.AppConstants.BASE_URL;

public class DashboardController {
    @FXML TableView<AgentInfoModel> agentInfoComponent;
    @FXML AgentInfoController agentInfoComponentController;
    @FXML TableView<ContestInfoModel> contestInfoComponent;
    @FXML ContestDetailsController contestInfoComponentController;
    @FXML Label battlefieldSelectedLabel;
    @FXML Label missionSizeLabel;
    @FXML TextField missionSizeTextField;
    @FXML Button readyButton;
    @FXML Button setMissionSizeButton;
    SimpleBooleanProperty isReady;

    @FXML
    public void initialize(){
        contestInfoComponentController.setParentController(this);

        readyButton.disableProperty().bind(isReady.or(setMissionSizeButton.textProperty().isEmpty())
                .or(battlefieldSelectedLabel.textProperty().isEmpty()
                        .or(Bindings.isEmpty(agentInfoComponent.getItems()))));

        missionSizeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                missionSizeTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public DashboardController(){
        isReady = new SimpleBooleanProperty(false);
    }

    public void startActive() {
        agentInfoComponentController.startAgentListRefresher();
        contestInfoComponentController.startContestListRefresher();
    }

    public void setInActive() {
        try {
            agentInfoComponentController.close();
        } catch (Exception ignored) {}
    }

    public void setBattlefieldSelectedLabel(String battlefieldName) {
        this.battlefieldSelectedLabel.setText(battlefieldName);
    }
    public void setMissionSize() {
        String url = Objects.requireNonNull(HttpUrl.parse(BASE_URL + "/update_mission")).newBuilder()
                .addQueryParameter("missionsize", missionSizeTextField.getText())
                .build().toString();

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful())
                    Platform.runLater(() -> missionSizeLabel.setText(missionSizeTextField.getText()));
            }

        });

    }

    @FXML
    public void readyOnActionButton(){
        HttpClientUtil.runAsync(BASE_URL + "/set_ready", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if(response.isSuccessful()){
                    isReady.setValue(true);
                }
            }
        });
    }
}


