package app.main_app.components.machine.code_calibration.plug_board_calibration;

import app.main_app.components.machine.MachineController;
import app.utils.machine_state.MachineState;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PlugboardCalibrationController {
    @FXML ComboBox<String> plug1Button;
    @FXML ComboBox<String> plug2Button;
    @FXML MachineController parentController;
    @FXML Button addPlugButton;
    @FXML Label plugsChosenLabel;
    private final SimpleStringProperty ABC;
    private final SimpleListProperty<String> curABC;
    private final SimpleStringProperty chosenPlugs;

    public PlugboardCalibrationController(){
        ABC = new SimpleStringProperty("");
        curABC = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
        chosenPlugs = new SimpleStringProperty("");
    }

    @FXML
    public void initialize(){
        plug1Button.itemsProperty().bind(curABC);
        plug2Button.itemsProperty().bind(curABC);

        ABC.addListener(observable -> {
            curABC.set(FXCollections.observableArrayList(
                    Arrays.stream(ABC.get().split(""))
                            .collect(Collectors.toList()))
            );
        });

        addPlugButton.disableProperty().bind(
                plug1Button.valueProperty().isNull()
                        .or(plug2Button.valueProperty().isNull())
                        .or(plug1Button.valueProperty().isEqualTo(plug2Button.valueProperty()))
        );
        plugsChosenLabel.textProperty().bind(chosenPlugs);
    }

    public void setParentController(MachineController parentController) {
        this.parentController = parentController;
    }

    public void setMachineState(MachineState machineState){
        ABC.bind(machineState.getABCProperty());
    }

    public void addPlugButtonAction(){
        // get plugs and add to plug string
        String curPlug = plug1Button.getValue() + plug2Button.getValue();
        chosenPlugs.set(chosenPlugs.getValue() + curPlug);

        // remove items from list
        curABC.getValue().removeAll(plug1Button.getValue(), plug2Button.getValue());

        //reset buttons value
        plug1Button.setValue(null);
        plug2Button.setValue(null);
    }

    public void resetPlugsButtonAction(){
        chosenPlugs.setValue("");

        //reset buttons value
        plug1Button.setValue(null);
        plug2Button.setValue(null);

        //reset current ABC to complete ABC
        curABC.set(new SimpleListProperty<>(FXCollections.observableArrayList(ABC.getValue().split(""))));
    }

    public String getChosenPlugs() {
        return chosenPlugs.get();
    }
}
