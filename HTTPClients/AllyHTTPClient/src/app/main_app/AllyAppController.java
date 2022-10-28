package app.main_app;

import app.main_app.contest.ContestController;
import app.main_app.dashboard.DashboardController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AllyAppController {
    @FXML VBox dashboardComponent;
    @FXML DashboardController dashboardComponentController;
    @FXML VBox contestComponent;
    @FXML ContestController contestComponentController;
    @FXML Stage primaryStage;

    SimpleStringProperty allyName;

    public AllyAppController(){
        allyName = new SimpleStringProperty();
    }

    @FXML
    public void initialize(){
        dashboardComponentController.startActive();

        contestComponentController.setParentController(this);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setAllyName(String allyName) {
        this.allyName.set(allyName);
    }

    public String getAllyName() {
        return allyName.get();
    }
    public void cleanAllData(){
        contestComponentController.cleanAllData();
        dashboardComponentController.cleanAllData();
    }
}
