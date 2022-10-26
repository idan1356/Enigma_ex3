package user_types.agent;

public class Agent {
    String agentName;
    String connectedAlly;
    int numOfThreads;
    int numOfMissions;
    int missionsFinishedCount;
    int missionsTakenCount;
    int candidatesCreated;

    public Agent(String agentName,String ally, int numOfThreads, int numOfMissions){
        this.agentName = agentName;
        this.connectedAlly = ally;
        this.numOfThreads = numOfThreads;
        this.numOfMissions = numOfMissions;
        missionsFinishedCount = 0;
        missionsTakenCount = 0;
        candidatesCreated = 0;
    }

    public String getAgentName() {
        return agentName;
    }

    public int getNumOfMissions() {
        return numOfMissions;
    }

    public int getNumOfThreads() {
        return numOfThreads;
    }

    public String getConnectedAlly() {
        return connectedAlly;
    }

    public int getMissionsFinishedCount() {
        return missionsFinishedCount;
    }

    public int getMissionsTakenCount() {
        return missionsTakenCount;
    }

    public int getCandidatesCreated() {
        return candidatesCreated;
    }

    public void setCandidatesCreated(int candidatesCreated) {
        this.candidatesCreated = candidatesCreated;
    }

    public void setMissionsFinishedCount(int missionsFinishedCount) {
        this.missionsFinishedCount = missionsFinishedCount;
    }

    public void setMissionsTakenCount(int missionsTakenCount) {
        this.missionsTakenCount = missionsTakenCount;
    }
}
