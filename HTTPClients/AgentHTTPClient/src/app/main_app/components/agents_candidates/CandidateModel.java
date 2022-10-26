package app.main_app.components.agents_candidates;

import javafx.beans.property.SimpleStringProperty;

public class CandidateModel {
    private final SimpleStringProperty initialPosition;
    private final SimpleStringProperty rotors;
    private final SimpleStringProperty reflector;
    private final SimpleStringProperty decodedMessage;

    public CandidateModel(String initialPosition, String rotors, String reflector, String decodedMessage) {
        this.initialPosition = new SimpleStringProperty(initialPosition);
        this.rotors = new SimpleStringProperty(rotors);
        this.reflector = new SimpleStringProperty(reflector);
        this.decodedMessage = new SimpleStringProperty(decodedMessage);
    }

    public String getRotors() {
        return rotors.get();
    }

    public String getInitialPosition() {
        return initialPosition.get();
    }

    public String getReflector() {
        return reflector.get();
    }

    public String getDecodedMessage() {
        return decodedMessage.get();
    }
}
