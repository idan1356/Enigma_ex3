package app.main_app;
import app.main_app.components.contest.ContestController;
import app.main_app.components.machine.MachineController;
import app.utils.AppUtils;
import http.HttpClientUtil;
import app.utils.machine_state.MachineState;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import static app.utils.AppConstants.BASE_URL;

public class UboatAppController {
    @FXML VBox contestComponent;
    @FXML ContestController contestComponentController;
    @FXML BorderPane machineComponent;
    @FXML MachineController machineComponentController;

    //components
    @FXML Button openFileButton;
    @FXML Label fileSelectedLabel;
    Stage primaryStage;
    MachineState machineState;

    private final SimpleStringProperty selectedFileProperty;


    public UboatAppController(){
        selectedFileProperty = new SimpleStringProperty();
        machineState = new MachineState();
    }

    @FXML
    public void initialize() {
        fileSelectedLabel.textProperty().bind(selectedFileProperty);
        machineComponentController.setParentController(this);
        machineComponentController.setMachineState(machineState);

        contestComponentController.setActive();
//        bruteForceComponentController.setParentController(this);
//        bruteForceComponentController.setDecipherState(decipherState);

//        themeSelectionComboBox.itemsProperty().get().addAll(AppTheme.values());
//        themeSelectionComboBox.valueProperty().addListener((observable, oldValue, newValue) ->  {
//            themeSelectionComboBox.getScene().getStylesheets().clear();
//            themeSelectionComboBox.getScene().getStylesheets().add(getClass().getResource(newValue.resource()).toExternalForm());
//        });
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void openFileButtonAction() {
        // choose file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please select XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file","*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile == null)
            return;
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("xmlfile", selectedFile.getName(),
                        RequestBody.create(selectedFile, MediaType.parse("text/plain")))
                .build();

        HttpClientUtil.runAsyncPost(BASE_URL + "/upload_file", body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if(!response.isSuccessful()) {
                    assert response.body() != null;
                    Platform.runLater(() -> {
                        try {
                            handleFileError(new RuntimeException(response.body().string()));
                            response.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }


                Platform.runLater(() -> {
                    //update selected file
                    String absolutePath = selectedFile.getAbsolutePath();
                    AppUtils.isFileSelected.set(true);
                    selectedFileProperty.set(absolutePath);
                    machineState.refresh();
                    contestComponentController.loadDataDecipherState();
                });
            }
        });
    }
    private void handleFileError(Exception exception){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Invalid File");
        errorAlert.setContentText(exception.getMessage());
        errorAlert.showAndWait();
    }

    public void cleanUboatStringInput(){
        contestComponentController.cleanUserInputLabels();
    }

   // public MachineConfigurationController getEnigmaConf(){
    //    return machineComponentController.getMachineConfigurationComponentController();
   // }

   // public void cleanAll(){
    //    machineComponentController.cleanAll();
    //    encryptDecryptComponentController.cleanAll();
   // }
}


