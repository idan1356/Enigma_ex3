package app.main_app.components.machine.enigma_view;

import app.main_app.components.machine.enigma_view.components_view.KeyboardView;
import app.main_app.components.machine.enigma_view.components_view.ReflectorView;
import app.main_app.components.machine.enigma_view.components_view.RotorView;
import app.utils.machine_state.MachineState;
import app.utils.machine_state.enigma_state.EnigmaState;
import app.utils.machine_state.enigma_state.components_state.ReflectorState;
import app.utils.machine_state.enigma_state.components_state.RotorState;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

import java.util.List;

public class EnigmaViewController {
    @FXML HBox rotorsPanel;

    private void addReflector(ReflectorState reflector){
        ReflectorView reflectorView = new ReflectorView(reflector);
        rotorsPanel.getChildren().add(reflectorView);
        reflectorView.getStyleClass().add("enigma_element");
    }
    private void addKeyboard(String ABC){
        KeyboardView keyboardView = new KeyboardView(ABC);
        rotorsPanel.getChildren().add(keyboardView);
        keyboardView.getStyleClass().add("enigma_element");
    }

    private void addRotor(RotorState rotorState){
        RotorView rotor = new RotorView(
                rotorState.getRotorProperty().get(),
                rotorState.getRotorIDProperty().get(),
                rotorState.getOffsetProperty().get(),
                rotorState.getNotchProperty().get()
        );

        rotorsPanel.getStylesheets().add("app/main_app/components/machine/enigma_view/rotor.css");
        rotorsPanel.getChildren().add(rotor);
        rotor.getStyleClass().add("enigma_element");
    }

    public void setMachineState(MachineState machineState){
        EnigmaState enigmaState = machineState.getEnigmaState();

        machineState.getIsChangeProperty().addListener(observable -> {
            if (machineState.getEnigmaState().getReflector() != null) {
                rotorsPanel.getChildren().clear();
                addReflector(enigmaState.getReflector());
                setRotors(enigmaState.getRotors());
                addKeyboard(machineState.getABCProperty().getValue());
            }
        });

    }

    public void setRotors(List<RotorState> rotorStateList){
        rotorStateList.forEach(this::addRotor);
    }

    public void cleanAll(){
        rotorsPanel.getChildren().clear();
    }
}
