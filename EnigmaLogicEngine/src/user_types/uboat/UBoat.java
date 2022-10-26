package user_types.uboat;

public class UBoat {
   private final String uboatName;
   private String battlefieldName;

   private boolean isReady;
    public UBoat(String uboatName){
        this.uboatName = uboatName;
        this.isReady = false;
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
}
