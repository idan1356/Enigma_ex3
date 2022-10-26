package app.utils.machine_state.history_state;

import DTO.DTO_enigma.DTO_enigma_outputs.DTOEnigmaHistory;
import app.utils.machine_state.enigma_state.EnigmaState;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class EnigmaHistoryState {
    private EnigmaState enigmaState;
    private final SimpleListProperty<EncodingState> encodingStateListProperty;

    public EnigmaHistoryState(DTOEnigmaHistory dtoEnigmaHistory){
        encodingStateListProperty = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));

        enigmaState = new EnigmaState();
        enigmaState.refresh(dtoEnigmaHistory.getSpecs());
        encodingStateListProperty.set(
                FXCollections.observableArrayList(
                dtoEnigmaHistory.getEncodings()
                .stream()
                .map(EncodingState::new)
                .collect(Collectors.toList())
        ));
    }

    public EnigmaState getEnigmaState() {
        return enigmaState;
    }

    public ObservableList<EncodingState> getEncodingStateListProperty() {
        return encodingStateListProperty.get();
    }
}
