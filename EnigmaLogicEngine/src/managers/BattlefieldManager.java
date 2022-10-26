package managers;

import battlefield.Battlefield;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BattlefieldManager {
    HashMap<String, Battlefield> battlefieldHashSet;

    public BattlefieldManager(){
        battlefieldHashSet = new HashMap<>();
    }

    public synchronized void addBattlefield(String battlefieldName, Battlefield battlefield) {
        battlefieldHashSet.put(battlefieldName, battlefield);
    }

    public synchronized void removeBattlefield(String battlefieldName) {
        battlefieldHashSet.remove(battlefieldName);
    }

    public synchronized Collection<Battlefield> getAllBattlefields() {
        return Collections.unmodifiableCollection(battlefieldHashSet.values());
    }

    public synchronized Battlefield getBattlefield(String battlefieldName){
        return battlefieldHashSet.get(battlefieldName);
    }


    public synchronized boolean isBattlefieldExists(String battlefieldName) {
        return battlefieldHashSet.containsKey(battlefieldName);

    }
}
