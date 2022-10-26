package app.utils.machine_state.history_state;

import DTO.DTO_machine.DTOMachineHistory;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MachineHistoryState {
    SimpleListProperty<EnigmaHistoryState> machineHistory;

    public MachineHistoryState(){
        machineHistory = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
    }

    public void refresh(DTOMachineHistory dtoMachineHistory){
        machineHistory.set(
                FXCollections.observableArrayList(
                        dtoMachineHistory.getEnigmaHistories()
                                .stream()
                                .map(EnigmaHistoryState::new)
                                .collect(Collectors.toList())
                ));
    }

    public  SimpleListProperty<EnigmaHistoryState> getMachineHistoryProperty() {
        return machineHistory;
    }
}
