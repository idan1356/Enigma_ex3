package app.main_app.components.contest.active_teams_details;

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
import java.util.*;
import java.util.function.Consumer;

import static app.utils.AppConstants.BASE_URL;

public class AlliesListRefresher extends TimerTask {

    private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<List<TeamInfoModel>> usersListConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;


    public AlliesListRefresher(BooleanProperty shouldUpdate, Consumer<String> httpRequestLoggerConsumer, Consumer<List<TeamInfoModel>> usersListConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
        this.usersListConsumer = usersListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        final int finalRequestNumber = ++requestNumber;
        httpRequestLoggerConsumer.accept("About to invoke: " + " | Users Request # " + finalRequestNumber);
        HttpClientUtil.runAsync(BASE_URL + "/get_allies_from_battlefield", new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                JsonArray allies = JsonParser.parseString(response.body().string()).getAsJsonArray();
                List<TeamInfoModel> fromJsonToCollection = TeamInfoModelMaker(allies);
                usersListConsumer.accept(fromJsonToCollection);
            }
        });
    }

    public List<TeamInfoModel> TeamInfoModelMaker(JsonArray allies){
       List<TeamInfoModel> allAllies = new ArrayList<>();
        allies.forEach(ally ->{
            JsonObject allyJsonObject = ally.getAsJsonObject();
            TeamInfoModel info = new TeamInfoModel(allyJsonObject.get("allyName").getAsString(),
                    allyJsonObject.get("agents").getAsJsonArray().size(),
                    allyJsonObject.get("missionSize").getAsInt()
            );

            allAllies.add(info);
        });
        return allAllies;
    }
}
