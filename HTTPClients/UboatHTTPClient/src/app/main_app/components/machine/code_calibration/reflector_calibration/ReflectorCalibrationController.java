package app.main_app.components.machine.code_calibration.reflector_calibration;

import app.main_app.components.machine.code_calibration.CodeCalibrationController;
import app.utils.machine_state.MachineState;
import engine.machine.utils.RomanNumbers;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReflectorCalibrationController {
    @FXML ComboBox<RomanNumbers> reflectorSelectButton;
    @FXML CodeCalibrationController parentController;
    private final SimpleIntegerProperty reflectorCount;
    private final SimpleBooleanProperty isValid;

    public ReflectorCalibrationController(){
        reflectorCount = new SimpleIntegerProperty(0);
        isValid = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize(){
        reflectorCount.addListener((observable, oldValue, newValue) -> {
            reflectorSelectButton.getItems().clear();
            reflectorSelectButton.getItems().addAll(
                    IntStream.range(1, reflectorCount.intValue() + 1)
                            .boxed()
                            .collect(Collectors.toList())
                            .stream()
                            .map(RomanNumbers::intToRomanNumber)
                            .collect(Collectors.toList())
            );
        });

        isValid.bind(reflectorSelectButton.valueProperty().isNotNull());
    }

    public void setParentController(CodeCalibrationController parentController) {
        this.parentController = parentController;
    }

    public RomanNumbers getReflector(){
        return reflectorSelectButton.getValue();
    }

    public SimpleBooleanProperty getIsValid(){
        return isValid;
    }

    public void setMachineState(MachineState machineState) {
        reflectorCount.bind(machineState.getReflectorCountProperty());
    }
}
