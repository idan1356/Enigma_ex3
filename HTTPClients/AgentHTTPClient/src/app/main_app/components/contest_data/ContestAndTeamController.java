package app.main_app.components.contest_data;
import DTO.DTOContestInfo;
import http.HttpClientUtil;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
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


    SimpleBooleanProperty isBattlefieldExists;

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
                                isBattlefieldExists.setValue(true);
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
}