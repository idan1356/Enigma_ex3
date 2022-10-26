package app.main_app.components.machine.machine_details;

import app.main_app.components.machine.MachineController;
import app.utils.machine_state.MachineState;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MachineDetailsController {
    //components
    @FXML Label possibleRotorsLabel;
    @FXML Label reflectorCountLabel;
    @FXML Label messagesProcessedLabel;

    //parent
    MachineController parentController;

    public void setMachineState(MachineState machineState) {
        machineState.getReflectorCountProperty().addListener(event -> {
            possibleRotorsLabel.setText(
                    String.format("%d/%d",
                            machineState.getRotorsUsedCountProperty().get(), machineState.getRotorsGivenCountProperty().get())
            );
        });

        machineState.getReflectorCountProperty().addListener(event -> {
            reflectorCountLabel.setText(
                    String.valueOf(machineState.getReflectorCountProperty().get())
            );
        });

        machineState.getMessagesProcessedCountProperty().addListener(event -> {
            messagesProcessedLabel.setText(
                    String.valueOf(machineState.getMessagesProcessedCountProperty().get())
            );
        });
    }

    public void setParentController(MachineController parentController) {
        this.parentController = parentController;
    }
}
