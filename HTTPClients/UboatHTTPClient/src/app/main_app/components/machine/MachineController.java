package app.main_app.components.machine;

import app.main_app.UboatAppController;
import app.main_app.components.machine.code_calibration.CodeCalibrationController;
import app.main_app.components.machine.current_machine_conf.MachineConfigurationController;
import app.main_app.components.machine.enigma_view.EnigmaViewController;
import app.main_app.components.machine.machine_details.MachineDetailsController;
import app.utils.machine_state.MachineState;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MachineController {
    //subcomponents
    @FXML BorderPane CodeCalibrationComponent;
    @FXML CodeCalibrationController CodeCalibrationComponentController;
    @FXML VBox machineConfigurationComponent;
    @FXML MachineConfigurationController machineConfigurationComponentController;
    @FXML HBox rotorsViewComponent;
    @FXML EnigmaViewController rotorsViewComponentController;
    @FXML VBox machineDetailsComponent;
    @FXML MachineDetailsController machineDetailsComponentController;

    //parent
    @FXML UboatAppController parentController;

    //members and properties

    @FXML
    public void initialize(){
        CodeCalibrationComponentController.setParentController(this);
        machineConfigurationComponentController.setParentController(this);
    }

    public void setParentController(UboatAppController parentController) {
        this.parentController = parentController;
    }

    public void setMachineState(MachineState machineState) {
        CodeCalibrationComponentController.setMachineState(machineState);
        machineDetailsComponentController.setMachineState(machineState);
        machineConfigurationComponentController.setMachineState(machineState);
        rotorsViewComponentController.setMachineState(machineState);
    }

    public MachineConfigurationController getMachineConfigurationComponentController() {
        return machineConfigurationComponentController;
    }
    public void cleanAll(){
        rotorsViewComponentController.cleanAll();
    }

    public UboatAppController getParentController() {
        return parentController;
    }


}
