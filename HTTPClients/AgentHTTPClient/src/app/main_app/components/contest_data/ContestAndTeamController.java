package app.main_app.components.contest_data;
import DTO.DTOContestInfo;
import app.main_app.AgentAppController;
import http.HttpClientUtil;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
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
import static app.utils.AppUtils.REFRESH_RATE;

public class ContestAndTeamController {

    @FXML Label battlefieldLabel;
    @FXML Label uboatLabel;
    @FXML Label levelLabel;
    @FXML Label statusLabel;
    @FXML Label alliesLabel;

    SimpleStringProperty winnerName;
    SimpleBooleanProperty isWinnerExists;
    SimpleBooleanProperty isBattlefieldExists;

    AgentAppController parentController;

    @FXML
    public void initialize(){
        statusLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            if(Objects.equals(oldValue, "Active")){
                String messageContent = parentController.getAllyName().equals(winnerName.get()) ? WIN_MESSAGE : LOSE_MESSAGE_PREFIX + winnerName.get();
                MessagePopUp message = new MessagePopUp("End of Competition", messageContent);
                Platform.runLater(message::pop);
                parentController.cleanAllData();
            }
        });
    }


    public ContestAndTeamController(){
        isWinnerExists = new SimpleBooleanProperty(false);
        winnerName = new SimpleStringProperty();
    }

    public void getContestInfo(){
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
                                isBattlefieldExists.setValue(true);

                                if(contestInfo.getWinner() != null) {
                                    isWinnerExists.set(true);
                                    winnerName.set(contestInfo.getWinner().getAllyName());
                                }

                            }
                        );
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, REFRESH_RATE, REFRESH_RATE);
    }
    public void setIsBattlefieldExists(SimpleBooleanProperty isBattlefieldExists) {
        this.isBattlefieldExists = isBattlefieldExists;
    }

    public boolean getIsWinnerExists(){
        return isWinnerExists.get();
    }
    public void cleanAllData(){
        isWinnerExists.set(false);
        winnerName.set(null);
    }

    public void setParentController(AgentAppController parentController) {
        this.parentController = parentController;
    }
}