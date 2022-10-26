package app.main_app.components.contest.candidates_details;

import javafx.beans.property.SimpleStringProperty;

public class CandidateModel {

    private final SimpleStringProperty allyName = new SimpleStringProperty();
    private final SimpleStringProperty decodedMessage = new SimpleStringProperty();
    private final SimpleStringProperty position = new SimpleStringProperty();
    private final SimpleStringProperty rotors = new SimpleStringProperty();
    private final SimpleStringProperty reflector = new SimpleStringProperty();

    public CandidateModel(String allyName, String decodedMessage, String position, String rotors, String reflector){
        this.allyName.set(allyName);
        this.decodedMessage.set(decodedMessage);
        this.position.set(position);
        this.rotors.set(rotors);
        this.reflector.set(reflector);
    }

    public String getReflector() {
        return reflector.get();
    }

    public String getAllyName() {
        return allyName.get();
    }

    public String getDecodedMessage() {
        return decodedMessage.get();
    }

    public String getPosition() {
        return position.get();
    }

    public String getRotors() {
        return rotors.get();
    }

}
