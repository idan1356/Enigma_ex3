package app.main_app.contest.candidate_details;

import javafx.beans.property.SimpleStringProperty;

public class AgentCandidateModel {

    private final SimpleStringProperty agentName = new SimpleStringProperty();
    private final SimpleStringProperty decodedMessage = new SimpleStringProperty();
    private final SimpleStringProperty position = new SimpleStringProperty();
    private final SimpleStringProperty rotors = new SimpleStringProperty();
    private final SimpleStringProperty reflector = new SimpleStringProperty();

    public AgentCandidateModel(String agentName, String decodedMessage, String position, String rotors, String reflector){
        this.agentName.set(agentName);
        this.decodedMessage.set(decodedMessage);
        this.position.set(position);
        this.rotors.set(rotors);
        this.reflector.set(reflector);
    }

    public String getReflector() {
        return reflector.get();
    }

    public String getAllyName() {
        return agentName.get();
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

    public String getAgentName() {
        return agentName.get();
    }
}
