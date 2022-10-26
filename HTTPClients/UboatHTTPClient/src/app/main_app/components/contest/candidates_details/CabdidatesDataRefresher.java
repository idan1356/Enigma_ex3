package app.main_app.components.contest.candidates_details;

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

public class CabdidatesDataRefresher extends TimerTask {
    private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<List<CandidateModel>> usersListConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;


    public CabdidatesDataRefresher(BooleanProperty shouldUpdate, Consumer<String> httpRequestLoggerConsumer, Consumer<List<CandidateModel>> usersListConsumer) {
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
                JsonArray candidates = JsonParser.parseString(response.body().string()).getAsJsonArray();
                List<CandidateModel> fromJsonToCollection = TeamInfoModelMaker(candidates);
                usersListConsumer.accept(fromJsonToCollection);
            }
        });
    }

    public List<CandidateModel> TeamInfoModelMaker(JsonArray allies){
       List<CandidateModel> allAllies = new ArrayList<>();
        allies.forEach(ally ->{
            JsonObject allyJsonObject = ally.getAsJsonObject();
            //CandidateModel info = new CandidateModel();
            //allAllies.add(info);
        });

        return allAllies;
    }
}
