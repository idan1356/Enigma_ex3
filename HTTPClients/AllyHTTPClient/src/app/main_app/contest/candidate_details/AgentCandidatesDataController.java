package app.main_app.contest.candidate_details;

import DTO.DTOCandidate;
import app.main_app.components.contest.candidates_details.CandidateModel;
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
import java.util.stream.Collectors;

import static http.HttpClientUtil.REFRESH_RATE;

public class AgentCandidatesDataController implements Closeable {
    @FXML private TableView<AgentCandidateModel> candidatesDetails;
    @FXML public TableColumn<AgentCandidateModel, String> agentName;
    @FXML public TableColumn<AgentCandidateModel, String> decodedMessage;
    @FXML public TableColumn<AgentCandidateModel, String> position;
    @FXML public TableColumn<AgentCandidateModel, String> rotors;
    @FXML public TableColumn<AgentCandidateModel, String> reflector;

    @FXML ObservableList<AgentCandidateModel> data = FXCollections.observableList(new ArrayList<>());
    private Timer timer = new Timer();
    private TimerTask refresher;

    @FXML
    public void initialize(){
        agentName.setCellValueFactory(new PropertyValueFactory<>("AgentName"));
        decodedMessage.setCellValueFactory(new PropertyValueFactory<>("DecodedMessage"));
        position.setCellValueFactory(new PropertyValueFactory<>("Position"));
        rotors.setCellValueFactory(new PropertyValueFactory<>("Rotors"));
        reflector.setCellValueFactory(new PropertyValueFactory<>("Reflector"));
        candidatesDetails.setItems(data);
        startCandidateListRefresher();
    }
    private void updateUsersList(Collection<DTOCandidate> dtoCandidates) {
        Collection<AgentCandidateModel> candidates = dtoCandidates.stream().map(dtoCandidate -> new AgentCandidateModel(
                dtoCandidate.getAgentName(),
                dtoCandidate.getCandidateString(),
                dtoCandidate.getInitialPosition(),
                dtoCandidate.getRotorsPosition(),
                String.valueOf(dtoCandidate.getReflector())
        )).collect(Collectors.toList());

        Platform.runLater(() -> data.addAll(candidates));
    }

    public void startCandidateListRefresher(){
        refresher =  new AgentCabdidatesDataRefresher(System.out::println, this::updateUsersList);
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

    public void cleanAllData(){
        data.clear();
        candidatesDetails.refresh();
    }
}
