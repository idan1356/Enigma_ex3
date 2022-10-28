package user_types.uboat;

import DTO.DTOCandidate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UBoat {
   private final String uboatName;
   private String battlefieldName;
   private final List<DTOCandidate> candidateList;
   private int candidateListDelta;

   private boolean isReady;
    public UBoat(String uboatName){
        this.uboatName = uboatName;
        this.isReady = false;
        candidateList = new ArrayList<>();
    }

    public void setBattlefield(String battlefield) {
        this.battlefieldName = battlefield;
    }

    public void setReadyStatus(boolean ready) {
        isReady = ready;
    }

    public String getBattlefieldName() {
        return battlefieldName;
    }

    public String getUboatName() {
        return uboatName;
    }

    public boolean getIsReady(){
        return isReady;
    }
    public void addCandidate(List<DTOCandidate> dtoCandidate){
        synchronized (this) {
            this.candidateList.addAll(dtoCandidate);
        }
    }

    public List<DTOCandidate> getCandidateListDelta() {
        List<DTOCandidate> candidatelist = candidateList.subList(candidateListDelta, candidateList.size());
        candidateListDelta = candidateList.size();
        return Collections.unmodifiableList(candidatelist);
    }

    public void setCandidateListDelta(int candidateListDelta) {
        this.candidateListDelta = candidateListDelta;
    }

    public void reset(){
        isReady = false;
        candidateList.clear();
        candidateListDelta = 0;
    }
}
