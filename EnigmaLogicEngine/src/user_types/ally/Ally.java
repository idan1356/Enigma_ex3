package user_types.ally;
import DTO.DTOCandidate;
import DTO.DTOMission;
import engine.EncryptionMachineEngine;
import misc.BruteForceDifficulty;
import user_types.agent.Agent;
import user_types.ally.decryption_manager.MissionMaker;
import user_types.ally.decryption_manager.MissionMakerBuilder;

import java.util.*;

public class Ally {
    private String battlefield;
    private final String allyName;
    private final HashSet<Agent> agents;
    private int missionSize;
    private MissionMaker missionMaker;
    private final List<DTOCandidate> candidateList;
    private int candidateListDelta;
    private Thread curThread;
    private boolean isReady;

    public Ally(String name){
        this.agents = new HashSet<>();
        this.allyName = name;
        this.isReady = false;
        candidateList = new ArrayList<>();
        candidateListDelta = 0;
    }

    public void addAgent(Agent agent) {
        agents.add(agent);
    }
    public String getAllyName() {
        return allyName;
    }
    public HashSet<Agent> getAgents() {
        return agents;
    }
    public String getBattlefieldName() {
        return battlefield;
    }
    public void setBattlefield(String battlefield) {
        this.battlefield = battlefield;
    }
    public void setMissionSize(int missionSize) {
        this.missionSize = missionSize;
    }

    public void startCompetition(EncryptionMachineEngine engine, BruteForceDifficulty difficulty){
        missionMaker = new MissionMakerBuilder(engine, missionSize).build(difficulty);
        curThread = new Thread(missionMaker);
        curThread.start();
    }
    
    public List<DTOMission> takeMissions(int numOfMissions) throws InterruptedException {
        return missionMaker.takeMissions(numOfMissions);
    }

    public void setReadyStatus(boolean ready) {
        isReady = ready;
    }

    public boolean getIsReady(){
        return isReady;
    }

    public void addCandidate(Collection<DTOCandidate> candidateList){
        synchronized (this) {
            this.candidateList.addAll(candidateList);
        }
    }
    public double getTotalMissionCount(){
        return missionMaker.getTotalMissionsNum();
    }

    public double getTotalMissionsCreated(){
        return missionMaker.getMissionsCreatedCounter();
    }

    public List<DTOCandidate> getCandidateListDelta() {
        List<DTOCandidate> candidatelist = candidateList.subList(candidateListDelta, candidateList.size());
        candidateListDelta = candidateList.size();
        return Collections.unmodifiableList(candidatelist);
    }

    public void stopCreatingMissions(){
        curThread.interrupt();
    }

    public void reset(){
        stopCreatingMissions();
        candidateListDelta = 0;
        candidateList.clear();
        isReady = false;

        missionMaker.reset();
        agents.forEach(Agent::reset);
    }
}
