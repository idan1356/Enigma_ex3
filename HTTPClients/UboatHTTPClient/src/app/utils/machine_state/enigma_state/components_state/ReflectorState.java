package app.utils.machine_state.enigma_state.components_state;

import DTO.DTO_enigma.DTO_enigma_outputs.DTOEnigmaReflector;
import engine.machine.utils.RomanNumbers;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ReflectorState {
    SimpleListProperty<Integer> reflector;
    SimpleObjectProperty<RomanNumbers> reflectorID;

    public ReflectorState(){
        reflector = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
        reflectorID = new SimpleObjectProperty<>();
    }

    public void setReflector(DTOEnigmaReflector reflector){
        this.reflector.set(FXCollections.observableArrayList(reflector.getReflector()));
        this.reflectorID.set(reflector.getReflectorID());
    }

    public RomanNumbers getReflectorID() {
        return reflectorID.get();
    }

    public ObservableList<Integer> getReflector() {
        return reflector.get();
    }
}
