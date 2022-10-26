package app.utils.machine_state.enigma_state;

import DTO.DTO_enigma.DTO_enigma_outputs.DTOEnigmaSpecs;
import app.utils.machine_state.enigma_state.components_state.ReflectorState;
import app.utils.machine_state.enigma_state.components_state.RotorState;
import engine.machine.utils.RomanNumbers;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnigmaState {
    //rotors
    private final List<RotorState> rotors;
    private final SimpleListProperty<Integer> rotorsChosen;
    private final SimpleListProperty<Integer> rotorsDistanceFromNotch;

    private final SimpleStringProperty rotorsInitialPosition;

    //rest of components
    private final SimpleObjectProperty<RomanNumbers> reflectorChosen;
    private final SimpleStringProperty plugboard;
    private final SimpleObjectProperty<ReflectorState> reflector;

    public EnigmaState(){
        rotors = new ArrayList<>();
        rotorsChosen = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
        rotorsDistanceFromNotch = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
        rotorsInitialPosition = new SimpleStringProperty();
        reflectorChosen = new SimpleObjectProperty<>();
        plugboard = new SimpleStringProperty("");
        reflector = new SimpleObjectProperty<>();
    }

    public void refresh(DTOEnigmaSpecs enigmaSpecs){
        if (enigmaSpecs == null)
            return;

        rotors.clear();
        enigmaSpecs.getRotors().getRotorsDetails().forEach(rotor -> {
            RotorState rotorState = new RotorState();
            rotorState.setRotor(rotor);
            rotors.add(rotorState);
        });

        rotorsChosen.set(
                FXCollections.observableArrayList(
                enigmaSpecs.getRotors().getRotorsString()
                        .stream()
                        .map(Pair::getKey)
                        .collect(Collectors.toList())
                )
        );

        rotorsDistanceFromNotch.set(
                FXCollections.observableArrayList(
                        enigmaSpecs.getRotors().getRotorsString()
                                .stream()
                                .map(Pair::getValue)
                                .collect(Collectors.toList())
                )
        );

        rotorsInitialPosition.set(enigmaSpecs.getInitialPosition());
        reflectorChosen.set(enigmaSpecs.getReflectorID());
        plugboard.set(enigmaSpecs.getPlugBoard());

        reflector.set(new ReflectorState());
        reflector.get().setReflector(enigmaSpecs.getReflector());
    }

    public List<RotorState> getRotors() {
        return rotors;
    }

    public SimpleListProperty<Integer> getRotorsChosenProperty() {
        return rotorsChosen;
    }

    public SimpleStringProperty getRotorsInitialPositionProperty() {
        return rotorsInitialPosition;
    }

    public SimpleObjectProperty<RomanNumbers> getReflectorChosenProperty() {
        return reflectorChosen;
    }

    public SimpleStringProperty getPlugboardProperty() {
        return plugboard;
    }

    public SimpleListProperty<Integer> getRotorsDistanceFromNotch() {
        return rotorsDistanceFromNotch;
    }

    public ReflectorState getReflector() {
        return reflector.get();
    }
}
