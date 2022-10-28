package app.main_app.components.contest.winner_info;
import DTO.DTOCandidate;
import DTO.DTOContestInfo;
import app.main_app.components.contest.ContestController;
import com.google.gson.Gson;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import static app.utils.AppConstants.BASE_URL;
import static http.HttpClientUtil.*;

public class WinnerInfoController {
    Timer timer = new Timer();
    @FXML Label allyNameLabel;
    @FXML Label decodedMessageLabel;
    @FXML Label positionLabel;
    @FXML Label rotorsLabel;
    @FXML Label reflectorLabel;
    @FXML Button endCompetitionButton;
    ContestController parentController;

    @FXML
    public void initialize(){
        checkIfWinnerExists();
        endCompetitionButton.disableProperty().bind(allyNameLabel.textProperty().isEmpty());
    }

    public void checkIfWinnerExists(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                HttpClientUtil.runAsync(BASE_URL + "/get_battlefield", new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        assert response.body() != null;
                        DTOContestInfo contestInfo = new Gson().fromJson(response.body().string(), DTOContestInfo.class);
                        if(contestInfo != null && contestInfo.getWinner() != null && allyNameLabel.textProperty().isEmpty().getValue()) {
                            Platform.runLater(()-> updateWinnerInfoLabels(contestInfo));
                        }
                    }
                });
            }
        };

        timer.schedule(timerTask, REFRESH_RATE, REFRESH_RATE);
    }

    public void updateWinnerInfoLabels(DTOContestInfo contestInfo){
        DTOCandidate winnerCandidate = contestInfo.getWinner();
        allyNameLabel.setText(winnerCandidate.getAllyName());
        decodedMessageLabel.setText(winnerCandidate.getCandidateString());
        positionLabel.setText(winnerCandidate.getInitialPosition());
        rotorsLabel.setText(winnerCandidate.getRotorsPosition());
        reflectorLabel.setText(String.valueOf(winnerCandidate.getReflector()));
    }

    public void endCompetitionButtonAction(){
        HttpClientUtil.runAsync(BASE_URL + "/reset_competition", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if(response.isSuccessful()){
                    parentController.cleanAllData();
                }
            }
        });
    }

    public void setParentController(ContestController parentController) {
        this.parentController = parentController;
    }

    public void clearAllData(){
        Platform.runLater(() -> {
            allyNameLabel.setText("");
            decodedMessageLabel.setText("");
            positionLabel.setText(null);
            rotorsLabel.setText(null);
            reflectorLabel.setText(null);
        });

    }
}
