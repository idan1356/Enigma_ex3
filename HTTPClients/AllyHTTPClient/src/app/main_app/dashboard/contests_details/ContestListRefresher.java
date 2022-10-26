package app.main_app.dashboard.contests_details;

import http.HttpClientUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static app.utils.AppConstants.BASE_URL;

public class ContestListRefresher extends TimerTask {

    private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<List<ContestInfoModel>> contestListConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;

    public ContestListRefresher(BooleanProperty shouldUpdate, Consumer<String> httpRequestLoggerConsumer, Consumer<List<ContestInfoModel>> usersListConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
        this.contestListConsumer = usersListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {
        if (!shouldUpdate.get()) {
            return;
        }

        final int finalRequestNumber = ++requestNumber;
        httpRequestLoggerConsumer.accept("About to invoke: " + " | Users Request # " + finalRequestNumber);
        HttpClientUtil.runAsync(BASE_URL + "/get_all_battlefield", new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                JsonArray battlefields = JsonParser.parseString(response.body().string()).getAsJsonArray();
                List<ContestInfoModel> fromJsonToCollection = contestInfoModelMaker(battlefields);
                contestListConsumer.accept(fromJsonToCollection);
            }
        });
    }

    public List<ContestInfoModel> contestInfoModelMaker(JsonArray battlefields){
       List<ContestInfoModel> AllContrsts = new ArrayList<>();

        battlefields.forEach(battlefield ->{
            JsonObject battlefieldJsonObject = battlefield.getAsJsonObject();
            ContestInfoModel info = new ContestInfoModel(
                    battlefieldJsonObject.get("battlefield").getAsString(),
                    battlefieldJsonObject.get("uboat").getAsString(),
                    battlefieldJsonObject.get("status").getAsString(),
                    battlefieldJsonObject.get("difficulty").getAsString(),
                    battlefieldJsonObject.get("allies").getAsString()
            );
            AllContrsts.add(info);
        });
        return AllContrsts;
    }
}
