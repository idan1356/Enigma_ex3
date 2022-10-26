package user_types.ally.decryption_manager;
import DTO.DTOSpecs;
import DTO.DTO_machine.DTOMachineSpecs;
import engine.EncryptionMachineEngine;
import javafx.util.Pair;
import misc.BruteForceDifficulty;
import user_types.ally.decryption_manager.decryption_manager_utils.OneTimeSupplier;
import user_types.ally.decryption_manager.decryption_manager_utils.Permuter;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MissionMakerBuilder {
    private EncryptionMachineEngine engine;
    private Supplier<List<Integer>> rotorsPositioningSupplier;
    private Supplier<List<Integer>> rotorsIDSupplier;
    private List<Integer> reflectorSupplier;
    private double totalNumberofPositions;
    private final int missionSize;

    private double totalNumOfMissions;

    public MissionMakerBuilder(EncryptionMachineEngine engine, int missionSize) {
        this.engine = engine;
        this.missionSize = missionSize;
        DTOSpecs specs = engine.getSpecs();
        reflectorSupplier = IntStream.range(1, specs.getMachineSpecs().getReflectorsCount() + 1)
                .boxed()
                .collect(Collectors.toList());

        rotorsPositioningSupplier = new Permuter(specs.getMachineSpecs().getRotorsUsedCount());
        rotorsIDSupplier = new OneTimeSupplier<>(specs.getCurEnigmaSpecs().getRotors().getRotorsString()
                .stream()
                .map(Pair::getKey)
                .collect(Collectors.toList()));
    }

    private void reflector(){
        reflectorSupplier = Collections.singletonList(engine.getSpecs().getCurEnigmaSpecs().getReflectorID().value());
    }

    private void rotorsPositioning(){
        rotorsPositioningSupplier = new OneTimeSupplier<>(
                IntStream.range(0, engine.getSpecs().getMachineSpecs().getReflectorsCount())
                .boxed()
                .collect(Collectors.toList())
        );
    }
    private void rotors(){
        rotorsIDSupplier = new OneTimeSupplier<>(engine.getSpecs().getCurEnigmaSpecs().getRotors().getRotorsString()
                .stream()
                .map(Pair::getKey)
                .collect(Collectors.toList()));
    }

    public MissionMaker build(BruteForceDifficulty bruteForceDifficulty){
        if (bruteForceDifficulty.value() < 2) {
            this.reflector();
        }
        if (bruteForceDifficulty.value() < 3) {
           this.rotorsPositioning();
        }
        if (bruteForceDifficulty.value() < 4) {
            this.rotors();
        }

        totalNumberofPositions = Math.pow(engine.getSpecs().getMachineSpecs().getABC().length(),
                engine.getSpecs().getMachineSpecs().getRotorsUsedCount());

        totalNumOfMissions = getTotalNumberOfMissions(bruteForceDifficulty, engine);

        return new MissionMaker(this);
    }

    private double getTotalNumberOfMissions(BruteForceDifficulty bruteForceDifficulty, EncryptionMachineEngine engine){
        DTOMachineSpecs machineSpecs = engine.getSpecs().getMachineSpecs();
        int rotorsUsed = machineSpecs.getRotorsUsedCount();
        int rotorsGiven = machineSpecs.getRotorsGivenCount();
        int reflector = machineSpecs.getReflectorsCount();
        double count = Math.pow(machineSpecs.getABC().length(), rotorsUsed);

        if (bruteForceDifficulty.value() > 1)
            count *= reflector;
        if (bruteForceDifficulty.value() > 2)
            count *= factorial(rotorsUsed);
        if(bruteForceDifficulty.value() > 3){
            count *= (double) (factorial(rotorsGiven) / (factorial(rotorsUsed) * factorial(rotorsGiven - rotorsUsed)));
        }

        return count / missionSize;
    }

    private static int factorial(int n) {
        if (n == 0)
            return 1;

        return n*factorial(n-1);
    }

    public List<Integer> getReflectorSupplier() {
        return reflectorSupplier;
    }

    public Supplier<List<Integer>> getRotorsIDSupplier() {
        return rotorsIDSupplier;
    }

    public Supplier<List<Integer>> getRotorsPositioningSupplier() {
        return rotorsPositioningSupplier;
    }

    public double getTotalNumberofPositions() {
        return totalNumberofPositions;
    }

    public int getMissionSize() {
        return missionSize;
    }

    public double getTotalNumOfMissions() {
        return totalNumOfMissions;
    }
}
