package app.main_app.contest.ally_active_teams_details;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TeamInfoModel {
    private SimpleStringProperty allyName;
    private SimpleIntegerProperty numOfAgents;
    private SimpleIntegerProperty missionSize;

    public TeamInfoModel(String allyName, Integer numOfAgents, Integer missionSize) {
        this.allyName = new SimpleStringProperty(allyName);
        this.numOfAgents = new SimpleIntegerProperty(numOfAgents);
        this.missionSize = new SimpleIntegerProperty(missionSize);
    }

    public String getAllyName() {
        return allyName.get();
    }

    public int getNumOfAgents() {
        return numOfAgents.get();
    }

    public int getMissionSize() {
        return missionSize.get();
    }
}
