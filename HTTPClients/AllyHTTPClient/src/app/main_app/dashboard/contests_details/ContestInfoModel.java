package app.main_app.dashboard.contests_details;

import javafx.beans.property.SimpleStringProperty;

public class ContestInfoModel {
    private SimpleStringProperty battlefield;
    private SimpleStringProperty uboat;
    private SimpleStringProperty status;
    private SimpleStringProperty difficulty;
    private SimpleStringProperty allies;

    public ContestInfoModel(String battlefield, String uboat, String status,
                            String difficulty, String allies) {
        this.battlefield = new SimpleStringProperty(battlefield);
        this.uboat = new SimpleStringProperty(uboat);
        this.status = new SimpleStringProperty(status);
        this.difficulty = new SimpleStringProperty(difficulty);
        this.allies = new SimpleStringProperty(allies);

    }

    public String getBattlefield() {
        return battlefield.get();
    }

    public String getUboat() {
        return uboat.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String getAllies() {
        return allies.get();
    }

    public String getDifficulty() {
        return difficulty.get();
    }
}
