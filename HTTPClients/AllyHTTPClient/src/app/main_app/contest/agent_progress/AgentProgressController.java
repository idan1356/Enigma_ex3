package app.main_app.contest.agent_progress;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
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

public class AgentProgressController implements Closeable {
    @FXML private TableView<AgentProgressModel> agentsDetails;
    @FXML public TableColumn<AgentProgressModel, String> agentName;
    @FXML public TableColumn<AgentProgressModel, Integer> numOfCandidate;
    @FXML public TableColumn<AgentProgressModel, Integer> numOfCompletedMissions;
    @FXML public TableColumn<AgentProgressModel, Integer> numOfMissionsTaken;

    @FXML
    ObservableList<AgentProgressModel> data = FXCollections.observableList(new ArrayList<>());
    private Timer timer;
    private TimerTask refresher;

    @FXML
    public void initialize(){
        agentName.setCellValueFactory(new PropertyValueFactory<>("AgentName"));
        numOfCandidate.setCellValueFactory(new PropertyValueFactory<>("numOfCandidate"));
        numOfCompletedMissions.setCellValueFactory(new PropertyValueFactory<>("numOfCompletedMissions"));
        numOfMissionsTaken.setCellValueFactory(new PropertyValueFactory<>("numOfMissionsTaken"));
        agentsDetails.setItems(data);
        startContestListRefresher();
    }
    private void updateUsersList(Collection<AgentProgressModel> progress) {
        Platform.runLater(() -> {
            data.clear();
            data.addAll(progress);
        });
    }

    public void  startContestListRefresher(){
        SimpleBooleanProperty simpleBooleanProperty = new SimpleBooleanProperty(true);
        refresher =  new AgentProgressListRefresher(simpleBooleanProperty,
                System.out::println, this::updateUsersList);
        timer = new Timer();
        timer.schedule(refresher,REFRESH_RATE, REFRESH_RATE);
    }

    @Override
    public void close() {
        agentsDetails.getItems().clear();
        if (refresher != null && timer != null) {
            refresher.cancel();
            timer.cancel();
        }
    }

}
