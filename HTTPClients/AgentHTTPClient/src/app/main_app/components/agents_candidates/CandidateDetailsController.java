package app.main_app.components.agents_candidates;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Collection;

public class CandidateDetailsController {
    @FXML TableView<CandidateModel> agentCandidates;
    @FXML TableColumn<CandidateModel, String> initialPosition;
    @FXML TableColumn<CandidateModel, String> rotors;
    @FXML TableColumn<CandidateModel, String> reflector;
    @FXML TableColumn<CandidateModel, String> decodedMessage;
    @FXML ObservableList<CandidateModel> data = FXCollections.observableList(new ArrayList<>());

    @FXML
    public void initialize(){
        initialPosition.setCellValueFactory(new PropertyValueFactory<>("InitialPosition"));
        rotors.setCellValueFactory(new PropertyValueFactory<>("Rotors"));
        reflector.setCellValueFactory(new PropertyValueFactory<>("Reflector"));
        decodedMessage.setCellValueFactory(new PropertyValueFactory<>("DecodedMessage"));
        agentCandidates.setItems(data);
    }

    public void addCandidates(Collection<CandidateModel> candidates){
        data.addAll(candidates);
    }

    public void cleanAllData(){
        data.clear();
    }
}
