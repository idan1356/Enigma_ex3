package app.main_app.components.contest;

import app.main_app.components.contest.active_teams_details.ActiveTeamsDetailsController;
import app.main_app.components.contest.active_teams_details.TeamInfoModel;
import app.main_app.components.contest.brute_force_input.BruteForceInputController;
import app.main_app.components.contest.candidates_details.CandidateModel;
import app.main_app.components.contest.candidates_details.CandidatesDataController;
import app.main_app.components.contest.winner_info.WinnerInfoController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ContestController {
    @FXML TableView<TeamInfoModel> activeTeamsDetailsComponent;
    @FXML ActiveTeamsDetailsController activeTeamsDetailsComponentController;
    @FXML HBox bruteForceInputComponent;
    @FXML BruteForceInputController bruteForceInputComponentController;
    @FXML VBox winnerInfoComponent;
    @FXML WinnerInfoController winnerInfoComponentController;

    @FXML TableView<CandidateModel> candidateDetailsComponent;
    @FXML CandidatesDataController candidateDetailsComponentController;

    @FXML
    public void initialize(){
        bruteForceInputComponentController.setParentController(this);
        winnerInfoComponentController.setParentController(this);
    }

    public void setActive() {
        activeTeamsDetailsComponentController.startAlliesListRefresher();
    }

    public void setInActive() {
        try {
            activeTeamsDetailsComponentController.close();
        } catch (Exception ignored) {}
    }

    public void loadDataDecipherState(){
        bruteForceInputComponentController.loadDataDecipherState();
    }

    public void cleanUserInputLabels(){
        bruteForceInputComponentController.cleanInputLabels();
    }
    public void cleanAllData(){
        bruteForceInputComponentController.cleanAllData();
        candidateDetailsComponentController.clearAllData();
        winnerInfoComponentController.clearAllData();
    }
}
