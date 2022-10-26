package app.main_app.dashboard.agents_details;

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

public class AgentListRefresher extends TimerTask {

    private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<List<AgentInfoModel>> usersListConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;

    public AgentListRefresher(BooleanProperty shouldUpdate, Consumer<String> httpRequestLoggerConsumer, Consumer<List<AgentInfoModel>> usersListConsumer) {
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
        httpRequestLoggerConsumer.accept("About to invoke: get all agents for ally " + " | Users Request # " + finalRequestNumber);
        HttpClientUtil.runAsync(BASE_URL + "/get_all_agents", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;

                JsonArray agents = JsonParser.parseString(response.body().string()).getAsJsonArray();
                List<AgentInfoModel> fromJsonToCollection = TeamInfoModelMaker(agents);
                usersListConsumer.accept(fromJsonToCollection);
            }
        });
    }

    public List<AgentInfoModel> TeamInfoModelMaker(JsonArray agents){
       List<AgentInfoModel> allAgents = new ArrayList<>();
        agents.forEach(ally ->{
            JsonObject agentJsonObject = ally.getAsJsonObject();

            AgentInfoModel info = new AgentInfoModel(
                    agentJsonObject.get("agentName").getAsString(),
                    agentJsonObject.get("numOfThreads").getAsInt(),
                    agentJsonObject.get("numOfMissions").getAsInt()
            );
            allAgents.add(info);
        });
        return allAgents;
    }
}
