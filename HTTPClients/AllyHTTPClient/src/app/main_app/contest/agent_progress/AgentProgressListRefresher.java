package app.main_app.contest.agent_progress;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import http.HttpClientUtil;
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

public class AgentProgressListRefresher extends TimerTask {

    private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<List<AgentProgressModel>> usersListConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;

    public AgentProgressListRefresher(BooleanProperty shouldUpdate, Consumer<String> httpRequestLoggerConsumer, Consumer<List<AgentProgressModel>> usersListConsumer) {
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

                JsonArray agentProgress = JsonParser.parseString(response.body().string()).getAsJsonArray();
                List<AgentProgressModel> fromJsonToCollection = AgentProgressModelMaker(agentProgress);
                usersListConsumer.accept(fromJsonToCollection);
            }
        });
    }

    public List<AgentProgressModel> AgentProgressModelMaker(JsonArray agents){
        List<AgentProgressModel> allAgentsProgress = new ArrayList<>();
        agents.forEach(ally ->{
            JsonObject agentJsonObject = ally.getAsJsonObject();

            AgentProgressModel info = new AgentProgressModel(
                    agentJsonObject.get("agentName").getAsString(),
                    agentJsonObject.get("missionsFinishedCount").getAsInt(),
                    agentJsonObject.get("candidatesCreated").getAsInt(),
                    agentJsonObject.get("missionsTakenCount").getAsInt()
            );
            allAgentsProgress.add(info);
        });
        return allAgentsProgress;
    }
}
