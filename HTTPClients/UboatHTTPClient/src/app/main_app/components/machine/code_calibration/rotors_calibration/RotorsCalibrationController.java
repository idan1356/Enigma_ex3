package app.main_app.components.machine.code_calibration.rotors_calibration;

import app.main_app.components.machine.code_calibration.CodeCalibrationController;
import app.utils.machine_state.MachineState;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RotorsCalibrationController {
    //components
    @FXML ComboBox<Integer> rotorSelectionButton;
    @FXML ComboBox<String> initialPositionSelectionButton;
    @FXML Button addRotorButton;
    @FXML Button resetRotorsButton;
    @FXML Label rotorsLeftLabel;
    @FXML Label rotorsSelectedLabel;

    //parent
    @FXML CodeCalibrationController parentController;

    //properties
    private final SimpleIntegerProperty rotorsGivenCount;
    private final SimpleIntegerProperty rotorsUsedCount;
    private final SimpleStringProperty ABC;
/////////////////
    private final SimpleIntegerProperty rotorsChosenCount;
    private final SimpleListProperty<Integer> rotorsChosen;
    private final SimpleStringProperty initialPositionsChosen;
    private final SimpleBooleanProperty isValid;

    public RotorsCalibrationController(){
        rotorsGivenCount = new SimpleIntegerProperty(0);
        rotorsUsedCount = new SimpleIntegerProperty(0);
        rotorsChosenCount = new SimpleIntegerProperty(0);
        isValid = new SimpleBooleanProperty(false);
        rotorsChosen = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
        initialPositionsChosen = new SimpleStringProperty("");
        ABC = new SimpleStringProperty("");
    }

    @FXML
    public void initialize(){
        addRotorButton.disableProperty().bind(rotorSelectionButton.valueProperty().isNull()
                                        .or(initialPositionSelectionButton.valueProperty().isNull()
                                                .or(isValid)));

        ABC.addListener(observable -> {
            initialPositionSelectionButton.getItems().clear();
            initialPositionSelectionButton.getItems().addAll(ABC.get().split(""));
        });

        rotorsGivenCount.addListener(observable -> {
            rotorSelectionButton.getItems().clear();
            rotorSelectionButton.getItems().addAll(
                    IntStream.range(1, rotorsGivenCount.intValue() + 1).boxed().collect(Collectors.toList())
            );
        });

        rotorsLeftLabel.textProperty().bind(Bindings.format("Rotors Left: %d", rotorsUsedCount.subtract(rotorsChosenCount)));
        rotorsSelectedLabel.textProperty().bind(Bindings.format("Rotors Selected: %s", rotorsChosen.asString()));
        isValid.bind(rotorsUsedCount.subtract(rotorsChosenCount).isEqualTo(0));
    }
    public void setParentController(CodeCalibrationController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void addRotorButtonAction() {
        //add to respective lists
        rotorsChosen.getValue().add(rotorSelectionButton.getValue());
        initialPositionsChosen.set(initialPositionsChosen.getValue() + initialPositionSelectionButton.getValue());

        //remove rotor from combo list and reset initial position
        rotorSelectionButton.getItems().remove(rotorSelectionButton.getValue());
        rotorSelectionButton.setValue(null);
        initialPositionSelectionButton.setValue(null);

        //update properties
        rotorsChosenCount.set(rotorsChosenCount.getValue() + 1);
    }

    public void resetRotorsButtonAction(){
        rotorsChosenCount.set(0);
        initialPositionsChosen.set("");
        rotorsChosen.getValue().clear();

        //add all rotors back to drop down menu
        initialPositionSelectionButton.setValue(null);
        rotorSelectionButton.getItems().clear();
        rotorSelectionButton.getItems().addAll(
                IntStream.range(1, rotorsGivenCount.intValue() + 1).boxed().collect(Collectors.toList())
        );

        //reset initial position value
    }

    public ObservableList<Integer> getRotors() {
        return rotorsChosen.getValue();
    }

    public String getPositions() {
        return initialPositionsChosen.get();
    }

    public SimpleBooleanProperty getIsValid(){
        return isValid;
    }

    public void setMachineState(MachineState machineState) {
        rotorsGivenCount.bind(machineState.getRotorsGivenCountProperty());
        rotorsUsedCount.bind(machineState.getRotorsUsedCountProperty());
        ABC.bind(machineState.getABCProperty());
    }
}
