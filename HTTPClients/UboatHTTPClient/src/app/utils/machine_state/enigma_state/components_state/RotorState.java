package app.utils.machine_state.enigma_state.components_state;

import DTO.DTO_enigma.DTO_enigma_outputs.DTORotors.DTORotor.DTOEnigmaRotor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.util.Pair;

import java.util.ArrayList;

public class RotorState {
    SimpleListProperty<Pair<String, String>> rotor;
    SimpleIntegerProperty rotorID;
    SimpleIntegerProperty offset;

    SimpleIntegerProperty notch;

    public RotorState(){
        rotor = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
        rotorID = new SimpleIntegerProperty();
        offset = new SimpleIntegerProperty();
        notch = new SimpleIntegerProperty();
    }

    public void setRotor(DTOEnigmaRotor dtoRotor){
        rotorID.set(dtoRotor.getRotorID());
        offset.set(dtoRotor.getRotorOffset());
        rotor.getValue().clear();
        notch.set(dtoRotor.getNotch());
        dtoRotor.getRotor().forEach(
                positioning ->  rotor.getValue().add(positioning.getPosition())
        );

    }

    public SimpleIntegerProperty getRotorIDProperty() {
        return rotorID;
    }

    public SimpleListProperty<Pair<String, String>> getRotorProperty() {
        return rotor;
    }

    public SimpleIntegerProperty getOffsetProperty() {
        return offset;
    }

    public SimpleIntegerProperty getNotchProperty() {
        return notch;
    }
}
