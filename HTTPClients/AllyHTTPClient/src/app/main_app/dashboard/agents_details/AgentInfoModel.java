package app.main_app.dashboard.agents_details;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AgentInfoModel {
    private SimpleStringProperty agentName;
    private SimpleIntegerProperty numOfThreads;
    private SimpleIntegerProperty numOfMissions;

    public AgentInfoModel(String agentName, Integer numOfThreads, Integer numOfMissions) {
        this.agentName = new SimpleStringProperty(agentName);
        this.numOfThreads = new SimpleIntegerProperty(numOfThreads);
        this.numOfMissions = new SimpleIntegerProperty(numOfMissions);
    }

    public int getNumOfThreads() {
        return numOfThreads.get();
    }
    public String getAgentName() {
        return agentName.get();
    }
    public int getNumOfMissions() {
        return numOfMissions.get();
    }
}
