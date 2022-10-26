package app.main_app.contest.ally_active_teams_details;

import app.utils.AppUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import static app.utils.AppUtils.REFRESH_RATE;

public class ActiveTeamsDetailsController implements Closeable {
    @FXML private TableView<TeamInfoModel> teamsDetails;
    @FXML public TableColumn<TeamInfoModel, String> allyName;
    @FXML public TableColumn<TeamInfoModel, Integer> numOfAgents;
    @FXML public TableColumn<TeamInfoModel, Integer> missionSize;
    @FXML ObservableList<TeamInfoModel> data = FXCollections.observableList(new ArrayList<>());
    private Timer timer;
    private TimerTask refresher;



    @FXML
    public void initialize(){
        allyName.setCellValueFactory(new PropertyValueFactory<>("AllyName"));
        numOfAgents.setCellValueFactory(new PropertyValueFactory<>("NumOfAgents"));
        missionSize.setCellValueFactory(new PropertyValueFactory<>("MissionSize"));
        teamsDetails.setItems(data);
        startAlliesListRefresher();
    }
    private void updateUsersList(Collection<TeamInfoModel> usersNames) {
        Platform.runLater(() -> {
            data.clear();
            data.addAll(usersNames);
        });
    }

    public void startAlliesListRefresher(){

        refresher =  new AlliesListRefresher(System.out::println, this::updateUsersList);
        timer = new Timer();
        timer.schedule(refresher, REFRESH_RATE, REFRESH_RATE);
    }

    @Override
    public void close() {
        teamsDetails.getItems().clear();
        if (refresher != null && timer != null) {
            refresher.cancel();
            timer.cancel();
        }
    }

    public ObservableList<TeamInfoModel> getTeamDetails(){
        return teamsDetails.getItems();
    }
}
