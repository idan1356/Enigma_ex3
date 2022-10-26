package app.main_app;

import app.main_app.contest.ContestController;
import app.main_app.dashboard.DashboardController;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AllyAppController {
    @FXML VBox dashboardComponent;
    @FXML DashboardController dashboardComponentController;
    @FXML VBox contestComponent;
    @FXML ContestController contestController;
    @FXML Stage primaryStage;

    @FXML
    public void initialize(){
        dashboardComponentController.startActive();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

}
