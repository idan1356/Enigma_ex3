package app.main_app.components.machine.current_machine_conf;
import app.main_app.components.machine.MachineController;
import app.utils.machine_state.MachineState;
import app.utils.machine_state.enigma_state.EnigmaState;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MachineConfigurationController {
    //components
    @FXML Label currentRotorsLabel;
    @FXML Label currentReflectorLabel;
    @FXML Label initialPositionLabel;
    @FXML Label currentNotchDistance;
    @FXML Label currentPlugBoardLabel;

    //parent
    MachineController parentController;

    public void setMachineState(MachineState machineState) {
        EnigmaState enigmaState = machineState.getEnigmaState();

        initialPositionLabel.textProperty().bind(enigmaState.getRotorsInitialPositionProperty());
        currentReflectorLabel.textProperty().bind(enigmaState.getReflectorChosenProperty().asString());

        enigmaState.getPlugboardProperty().addListener(observable -> {
            currentPlugBoardLabel.setText(
                    enigmaState.getPlugboardProperty().getValue().replaceAll("..", "$0 ")
            );
        });

        machineState.getIsChangeProperty().addListener(observable -> {
            currentRotorsLabel.setText(
                    enigmaState.getRotorsChosenProperty().get().toString()
            );
        });

        machineState.getIsChangeProperty().addListener(observable -> {
            currentNotchDistance.setText(
                    enigmaState.getRotorsDistanceFromNotch().get().toString()
            );
        });
    }

        public void setParentController(MachineController parentController) {
        this.parentController = parentController;
    }

    public StringProperty getPlugBoardLabelProperty() {
        return currentPlugBoardLabel.textProperty();
    }

    public StringProperty getNotchDistanceLabelProperty() {
        return currentNotchDistance.textProperty();
    }

    public StringProperty getReflectorLabelProperty() {
        return currentReflectorLabel.textProperty();
    }

    public StringProperty getInitialPositionLabelProperty() {
        return initialPositionLabel.textProperty();
    }

    public StringProperty getRotorsLabelProperty() {
        return currentRotorsLabel.textProperty();
    }
}
