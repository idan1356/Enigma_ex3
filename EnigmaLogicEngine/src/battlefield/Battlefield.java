package battlefield;

import DTO.DTOCandidate;
import DTO.DTO_enigma.DTO_enigma_outputs.DTOEnigmaSpecs;
import engine.EncryptionMachineEngine;
import engine.machine.components.enigma_factory.enigma.generated.CTEDecipher;
import engine.machine.utils.RomanNumbers;
import javafx.util.Pair;
import misc.BruteForceDifficulty;
import misc.EngineCloner;
import user_types.ally.Ally;
import user_types.uboat.UBoat;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Battlefield {
    private final BruteForceDifficulty level;
    private final String battlefieldName;
    private final int totalAllyNumber;
    private final HashMap<String,Ally> allyList;
    String fileInputStreamString;
    EncryptionMachineEngine engine;
    UBoat uboat;
    CTEDecipher decipher;
    String processMessage;
    String originalMessage;
    boolean isStarted;

    public Battlefield(String battlefieldName, int allyNumber, BruteForceDifficulty level){
        this.totalAllyNumber = allyNumber;
        this.battlefieldName = battlefieldName;
        this.level = level;
        this.allyList = new HashMap<>();
        isStarted = false;
    }
    public void setEngine(EncryptionMachineEngine engine) throws IOException, ClassNotFoundException {
     EngineCloner cloneEnigma = new EngineCloner();
     this.engine = cloneEnigma.cloneEngine(engine);
    }

    public int getTotalAllyNumber (){
        return totalAllyNumber;
    }
    public String getBattlefieldName() {
        return battlefieldName;
    }
    public BruteForceDifficulty getLevel() {
        return level;
    }
    public void setUboat(UBoat uboat) {
        this.uboat = uboat;
    }
    public void addAlly(Ally ally){ allyList.put(ally.getAllyName(), ally);}
    public Collection<Ally> getAllyList() {
        return allyList.values();
    }
    public boolean isFull() {
        return allyList.size() == totalAllyNumber;
    }
    public EncryptionMachineEngine getEngine() {
        return engine;
    }
    public void setDecipher(CTEDecipher decipher) {
        this.decipher = decipher;
    }
    public CTEDecipher getDecipher() {
        return decipher;
    }
    public String getOriginalMessage() {
        return originalMessage;
    }
    public String getProcessMessage() {
        return processMessage;
    }
    public void setProcessMessage(String processMessage) {
        this.processMessage = processMessage;
    }
    public void setOriginalMessage(String originalMessage) {
        this.originalMessage = originalMessage;
    }
    public void setFileInputStream(String fileInputStream) {
        this.fileInputStreamString = fileInputStream;
    }
    public String getUboatName() {
        return uboat.getUboatName();
    }
    public String getContestStatus(){
        return isStarted ? "Active" : "Inactive";
    }
    public int getCurrentAlliesNum(){
        return allyList.size();
    }
    public String getFileInputStream() {
        return fileInputStreamString;
    }
    public void setIsStarted(boolean started) {
        isStarted = started;
    }

    public boolean getIsStarted(){
        return isStarted;
    }

    public UBoat getUboat() {
        return uboat;
    }
}
