package managers;

import javafx.collections.FXCollections;
import user_types.ally.Ally;

import java.util.*;
import java.util.stream.Collectors;

public class AllyManager {
    HashMap<String, Ally> allyHashMap;

    public AllyManager(){
        allyHashMap = new HashMap<>();
    }

    public synchronized void addAlly(String allyName, Ally ally) {
        allyHashMap.put(allyName, ally);
    }

    public synchronized void removeAlly(String allyName) {
        allyHashMap.remove(allyName);
    }

    public synchronized List<Ally> getAllAllies() {
        return Collections.unmodifiableList(new ArrayList<>(allyHashMap.values()));
    }

    public synchronized Ally getAlly(String battlefieldName){
        return allyHashMap.get(battlefieldName);
    }

    public synchronized boolean isAllyExists(String battlefieldName) {
        return allyHashMap.containsKey(battlefieldName);
    }
}
