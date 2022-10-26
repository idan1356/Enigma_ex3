package app.main_app.components.contest.candidates_details;

import app.main_app.components.contest.active_teams_details.TeamInfoModel;
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

public class CandidatesDataController implements Closeable {
    @FXML private TableView<CandidateModel> candidatesDetails;
    @FXML public TableColumn<CandidateModel, String> allyName;
    @FXML public TableColumn<CandidateModel, String> decodedMessage;
    @FXML public TableColumn<CandidateModel, String> position;
    @FXML public TableColumn<CandidateModel, String> rotors;
    @FXML public TableColumn<CandidateModel, String> reflector;

    @FXML ObservableList<CandidateModel> data = FXCollections.observableList(new ArrayList<>());
    private Timer timer;
    private TimerTask refresher;

    @FXML
    public void initialize(){
        allyName.setCellValueFactory(new PropertyValueFactory<>("AllyName"));
        decodedMessage.setCellValueFactory(new PropertyValueFactory<>("DecodedMessage"));
        position.setCellValueFactory(new PropertyValueFactory<>("Position"));
        rotors.setCellValueFactory(new PropertyValueFactory<>("Rotors"));
        reflector.setCellValueFactory(new PropertyValueFactory<>("Reflector"));
        candidatesDetails.setItems(data);
    }
    private void updateUsersList(Collection<CandidateModel> candidateModels) {
        Platform.runLater(() -> {
            data.clear();
            data.addAll(candidateModels);
        });
    }

    public void startCandidateListRefresher(){
        refresher =  new CabdidatesDataRefresher(AppUtils.isFileSelected,
                System.out::println, this::updateUsersList);
        timer = new Timer();
        timer.schedule(refresher,REFRESH_RATE,REFRESH_RATE);
    }

    @Override
    public void close() {
        candidatesDetails.getItems().clear();
        if (refresher != null && timer != null) {
            refresher.cancel();
            timer.cancel();
        }
    }
}
