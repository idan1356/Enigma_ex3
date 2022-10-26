package app.utils.machine_state;

import DTO.DTOSpecs;
import DTO.DTO_enigma.DTO_enigma_outputs.DTOEnigmaSpecs;
import DTO.DTO_machine.DTOMachineSpecs;
import http.HttpClientUtil;
import app.utils.machine_state.enigma_state.EnigmaState;
import com.google.gson.Gson;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static app.utils.AppConstants.BASE_URL;

public class MachineState {
    private final EnigmaState enigmaState;
    private final SimpleIntegerProperty rotorsGivenCount;
    private final SimpleIntegerProperty rotorsUsedCount;
    private final SimpleIntegerProperty reflectorCount;
    private final SimpleIntegerProperty messagesProcessedCount;
    private final SimpleStringProperty ABC;
    private final SimpleBooleanProperty isChange;

    public MachineState(){
        enigmaState = new EnigmaState();
        rotorsGivenCount = new SimpleIntegerProperty(0);
        rotorsUsedCount = new SimpleIntegerProperty(0);
        reflectorCount = new SimpleIntegerProperty(0);
        messagesProcessedCount = new SimpleIntegerProperty(-1);
        ABC = new SimpleStringProperty("");
        isChange = new SimpleBooleanProperty(false);
        isChange.set(!isChange.get());
    }

    public void refresh(){
        HttpClientUtil.runAsync(BASE_URL + "/get_enigma_details", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                DTOSpecs specs = new Gson().fromJson(response.body().string(), DTOSpecs.class);
                DTOMachineSpecs machineSpecs = specs.getMachineSpecs();
                DTOEnigmaSpecs enigmaSpecs = specs.getCurEnigmaSpecs();

                Platform.runLater(() ->{
                      enigmaState.refresh(enigmaSpecs);
                      rotorsGivenCount.set(machineSpecs.getRotorsGivenCount());
                      rotorsUsedCount.set(machineSpecs.getRotorsUsedCount());
                      reflectorCount.set(machineSpecs.getReflectorsCount());
                      messagesProcessedCount.set(machineSpecs.getEncodesCount());
                      ABC.set(machineSpecs.getABC());
                      isChange.set(!isChange.get());
                });
            }
        });
    }

    public EnigmaState getEnigmaState() {
        return enigmaState;
    }

    public SimpleIntegerProperty getRotorsGivenCountProperty() {
        return rotorsGivenCount;
    }

    public SimpleIntegerProperty getRotorsUsedCountProperty() {
        return rotorsUsedCount;
    }

    public SimpleIntegerProperty getReflectorCountProperty(){
        return reflectorCount;
    }

    public SimpleIntegerProperty getMessagesProcessedCountProperty(){
        return messagesProcessedCount;
    }

    public SimpleStringProperty getABCProperty() {
        return ABC;
    }

    public SimpleBooleanProperty getIsChangeProperty(){return isChange;}
}
