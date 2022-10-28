package user_types.ally.decryption_manager;

import DTO.DTOMission;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MissionMaker implements Runnable {
    private final static int QUEUE_SIZE = 1000;
    private final BlockingQueue<DTOMission> missionBlockingQueue;
    final Supplier<List<Integer>> rotorsIDSupplier;
    Supplier<List<Integer>> rotorsPositionSupplier;
    private final List<Integer> reflectors;
    double totalNumOfPositions;
    int missionSize;
    int missionsCreatedCounter;
    double totalMissionsNum;

    public MissionMaker(MissionMakerBuilder builder) {
        this.missionBlockingQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
        this.rotorsIDSupplier = builder.getRotorsIDSupplier();
        this.rotorsPositionSupplier = builder.getRotorsPositioningSupplier();
        this.reflectors = builder.getReflectorSupplier();
        this.totalNumOfPositions =builder.getTotalNumberofPositions();
        this.missionSize = builder.getMissionSize();
        this.totalMissionsNum = builder.getTotalNumOfMissions();
        missionsCreatedCounter = 0;
    }

    @Override
    public void run() {
        missionsCreatedCounter = 0;

        for (List<Integer> rotors = rotorsIDSupplier.get(); rotors != null; rotors = rotorsIDSupplier.get()) {
            System.out.println(rotors);
            for (List<Integer> rotorsPerm = rotorsPositionSupplier.get(); rotorsPerm != null; rotorsPerm = rotorsPositionSupplier.get()) {
                List<Integer> curRotors = rotorsPerm.stream().map(rotors::get).collect(Collectors.toList());
                System.out.println("\t" + curRotors);
                for (Integer reflector : reflectors) {
                    System.out.println("\t\t" + reflector);
                    for(int i = 0; i < totalNumOfPositions; i += missionSize){
                        System.out.println("\t\t\t" + i);
                        DTOMission curMission = new DTOMission(curRotors, reflector, i, missionSize);
                        try {
                            missionBlockingQueue.put(curMission);
                            missionsCreatedCounter++;
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    public List<DTOMission> takeMissions(int numOfMissions) throws InterruptedException {
        List<DTOMission> missionsList = new ArrayList<>(numOfMissions);

        synchronized (missionBlockingQueue) {
            for (int i = 0; i < numOfMissions; i++)
                missionsList.add( missionBlockingQueue.take());
        }
        return missionsList;
    }

    public synchronized int getMissionsCreatedCounter() {
        return missionsCreatedCounter;
    }

    public double getTotalMissionsNum() {
        return totalMissionsNum;
    }

    public void reset(){
        missionsCreatedCounter = 0;
        //totalMissionsNum = 0;
    }
}


