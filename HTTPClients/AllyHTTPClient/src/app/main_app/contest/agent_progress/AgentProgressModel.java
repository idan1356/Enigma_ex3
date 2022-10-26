package app.main_app.contest.agent_progress;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AgentProgressModel {
    private final SimpleStringProperty agentName;
    private final SimpleIntegerProperty numOfCandidate;
    private final SimpleIntegerProperty numOfCompletedMissions;
    private final SimpleIntegerProperty numOfMissionsTaken;

    public AgentProgressModel(String agentName, Integer numOfMissions, Integer numOfCandidate, Integer numOfMissionsTaken) {
        this.agentName = new SimpleStringProperty(agentName);
        this.numOfCompletedMissions = new SimpleIntegerProperty(numOfMissions);
        this.numOfCandidate = new SimpleIntegerProperty(numOfCandidate);
        this.numOfMissionsTaken = new SimpleIntegerProperty(numOfMissionsTaken);
    }

    public String getAgentName() {
        return agentName.get();
    }

    public int getNumOfCompletedMissions() {
        return numOfCompletedMissions.get();
    }

    public int getNumOfCandidate() {
        return numOfCandidate.get();
    }

    public SimpleIntegerProperty numOfMissionsTakenProperty() {
        return numOfMissionsTaken;
    }
}
