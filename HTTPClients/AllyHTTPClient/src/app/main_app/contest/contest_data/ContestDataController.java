package app.main_app.contest.contest_data;
import DTO.DTOContestInfo;
import app.main_app.contest.ContestController;
import com.google.gson.Gson;
import http.HttpClientUtil;
import http.MessagePopUp;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static app.utils.AppConstants.BASE_URL;
import static http.HttpClientUtil.*;

public class ContestDataController {
    @FXML Label battlefieldLabel;
    @FXML Label uboatLabel;
    @FXML Label levelLabel;
    @FXML Label statusLabel;
    @FXML Label alliesLabel;
    @FXML Label decodedMassageLabel;
    Timer timer = new Timer();
    ContestController parentController;
    SimpleStringProperty winnerName;

    public ContestDataController(){
        winnerName = new SimpleStringProperty();
    }
    @FXML
    public void initialize(){
        refreshAllLabels();

        statusLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            if(Objects.equals(oldValue, "Active")){
                String messageContent = parentController.getParentController().getAllyName().equals(winnerName.get()) ? WIN_MESSAGE : LOSE_MESSAGE_PREFIX + winnerName.get();
                MessagePopUp message = new MessagePopUp("End of Competition", messageContent);
                Platform.runLater(message::pop);
                parentController.getParentController().cleanAllData();
            }
        });
    }

    public void refreshAllLabels(){
        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                HttpClientUtil.runAsync(BASE_URL + "/get_battlefield", new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        assert response.body() != null;
                        if(!response.isSuccessful()) return;

                        String str = response.body().string();

                        DTOContestInfo contestInfo = new Gson().fromJson(str, DTOContestInfo.class);
                        Platform.runLater(()-> {
                                    battlefieldLabel.setText(contestInfo.getBattlefield());
                                    levelLabel.setText(contestInfo.getDifficulty());
                                    uboatLabel.setText(contestInfo.getUboat());
                                    statusLabel.setText(contestInfo.getStatus());
                                    alliesLabel.setText(contestInfo.getAllies());
                                    decodedMassageLabel.setText(contestInfo.getProcessMessage());

                                    if(contestInfo.getWinner() != null)
                                        winnerName.set(contestInfo.getWinner().getAllyName());
                                }
                        );
                    }
                });
            }
        };
        timer.schedule(timerTask, REFRESH_RATE, REFRESH_RATE);
    }

    public void setParentController(ContestController parentController) {
        this.parentController = parentController;
    }
}
