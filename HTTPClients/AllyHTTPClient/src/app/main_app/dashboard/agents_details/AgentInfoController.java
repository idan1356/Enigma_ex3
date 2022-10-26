package app.main_app.dashboard.agents_details;
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

public class AgentInfoController implements Closeable {
    @FXML private TableView<AgentInfoModel> agentsDetails;
    @FXML public TableColumn<AgentInfoModel, String> agentName;
    @FXML public TableColumn<AgentInfoModel, Integer> numOfThreads;
    @FXML public TableColumn<AgentInfoModel, Integer> numOfMissions;
    @FXML ObservableList<AgentInfoModel> data = FXCollections.observableList(new ArrayList<>());
    private Timer timer;
    private TimerTask refresher;

    @FXML
    public void initialize(){
        agentName.setCellValueFactory(new PropertyValueFactory<>("AgentName"));
        numOfThreads.setCellValueFactory(new PropertyValueFactory<>("NumOfThreads"));
        numOfMissions.setCellValueFactory(new PropertyValueFactory<>("NumOfMissions"));
        agentsDetails.setItems(data);
    }
    private void updateUsersList(Collection<AgentInfoModel> usersNames) {
        Platform.runLater(() -> {
            data.clear();
            data.addAll(usersNames);
        });
    }

    public void  startAgentListRefresher(){
        SimpleBooleanProperty simpleBooleanProperty = new SimpleBooleanProperty(true);
        refresher =  new AgentListRefresher(simpleBooleanProperty,
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
