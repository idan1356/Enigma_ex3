package app.main_app.components.machine.code_calibration;

import app.main_app.components.machine.MachineController;
import app.main_app.components.machine.code_calibration.reflector_calibration.ReflectorCalibrationController;
import app.main_app.components.machine.code_calibration.rotors_calibration.RotorsCalibrationController;
import http.HttpClientUtil;
import app.utils.machine_state.MachineState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import static app.utils.AppConstants.BASE_URL;

public class CodeCalibrationController {
    //components
    @FXML VBox reflectorCalibrationComponent;
    @FXML ReflectorCalibrationController reflectorCalibrationComponentController;
    @FXML VBox rotorsCalibrationComponent;
    @FXML RotorsCalibrationController rotorsCalibrationComponentController;

    //parent
    @FXML MachineController parentController;

    //buttons
    @FXML Button randomButton;
    @FXML Button setButton;

    //members
    private MachineState machineState;

    public CodeCalibrationController(){
        machineState = new MachineState();
    }

    @FXML
    public void initialize(){
        reflectorCalibrationComponentController.setParentController(this);
        rotorsCalibrationComponentController.setParentController(this);

        setButton.disableProperty().bind(rotorsCalibrationComponentController.getIsValid().not()
                .or(reflectorCalibrationComponentController.getIsValid().not()));
    }

    public void setButtonAction() throws IOException {
        // get all info from javafx components
        String rotorsRaw = rotorsCalibrationComponentController.getRotors().toString();
        String rotors = rotorsRaw.substring(1,rotorsRaw.length() - 1).replace(" ", "");
        String initializePosition = rotorsCalibrationComponentController.getPositions();
        int reflector = reflectorCalibrationComponentController.getReflector().value();

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(BASE_URL + "/set_engine")).newBuilder();
        String url = urlBuilder.addQueryParameter("rotors", rotors)
                .addQueryParameter("reflector", String.valueOf(reflector))
                .addQueryParameter("initializeposition", initializePosition)
                .build().toString();

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    machineState.refresh();
                    resetCalibration();
                }
                else {
                    response.close();
                }
            }
        });
    }

    public void randomButtonAction(){
        HttpClientUtil.runAsync(BASE_URL + "/set_random_engine", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    machineState.refresh();
                    resetCalibration();
                }
                else {
                    response.close();
                }
            }
        });
    }

    public void setParentController(MachineController parentController) {
        this.parentController = parentController;
    }

    public void setMachineState(MachineState machineState) {
        this.machineState = machineState;

        reflectorCalibrationComponentController.setMachineState(machineState);
        rotorsCalibrationComponentController.setMachineState(machineState);
    }

    public void resetCalibration(){
        rotorsCalibrationComponentController.resetRotorsButtonAction();
        parentController.getParentController().cleanUboatStringInput();
    }

}
