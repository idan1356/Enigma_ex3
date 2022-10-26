package app.main_app.dashboard.contests_details;

import app.main_app.dashboard.DashboardController;
import http.HttpClientUtil;
import http.ErrorMessagePopUp;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

import static app.utils.AppConstants.BASE_URL;
import static app.utils.AppUtils.REFRESH_RATE;

public class ContestDetailsController implements Closeable {
    @FXML private TableView<ContestInfoModel> contestDetails;
    @FXML public TableColumn<ContestInfoModel, String> battlefield;
    @FXML public TableColumn<ContestInfoModel, String > uboat;
    @FXML public TableColumn<ContestInfoModel, String> status;
    @FXML public TableColumn<ContestInfoModel, String> difficulty;
    @FXML public TableColumn<ContestInfoModel, String> allies;
    @FXML ObservableList<ContestInfoModel> data = FXCollections.observableList(new ArrayList<>());
    private Timer timer;
    private TimerTask refresher;
    DashboardController parentController;

    @FXML
    public void initialize(){
        battlefield.setCellValueFactory(new PropertyValueFactory<>("Battlefield"));
        uboat.setCellValueFactory(new PropertyValueFactory<>("Uboat"));
        status.setCellValueFactory(new PropertyValueFactory<>("Status"));
        difficulty.setCellValueFactory(new PropertyValueFactory<>("Difficulty"));
        allies.setCellValueFactory(new PropertyValueFactory<>("Allies"));
        contestDetails.setItems(data);
    }

    private void updateContestList(Collection<ContestInfoModel> usersNames) {
        Platform.runLater(() -> {
            data.clear();
            data.addAll(usersNames);
        });
    }

    public void startContestListRefresher(){
        SimpleBooleanProperty simpleBooleanProperty = new SimpleBooleanProperty(true);
        refresher =  new ContestListRefresher(simpleBooleanProperty,
                System.out::println, this::updateContestList);
        timer = new Timer();
        timer.schedule(refresher,REFRESH_RATE, REFRESH_RATE);
    }

    @Override
    public void close() {
        contestDetails.getItems().clear();
        if (refresher != null && timer != null) {
            refresher.cancel();
            timer.cancel();
        }
    }

    @FXML
    private void handleRowSelect() {
        ContestInfoModel row = contestDetails.getSelectionModel().getSelectedItem();
        if (row == null) return;

        String url = Objects.requireNonNull(HttpUrl.parse(BASE_URL + "/ally_join_battlefield")).newBuilder()
                .addQueryParameter("battlefieldname", row.getBattlefield())
                .build().toString();

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;

                if(response.isSuccessful())
                    Platform.runLater(()-> {
                        parentController.setBattlefieldSelectedLabel(row.getBattlefield());
                    });
                else {
                    ErrorMessagePopUp messagePopUp = new ErrorMessagePopUp(new Exception(response.body().string()), "error");
                    messagePopUp.pop();
                }
            }
        });
    }

    public void setParentController(DashboardController parentController) {
        this.parentController = parentController;
    }
}
