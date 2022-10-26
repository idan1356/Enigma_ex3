package app.login;
import http.HttpClientUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static app.utils.AppConstants.BASE_URL;

public class AlliesListRefresher extends TimerTask {
    private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<List<String>> usersListConsumer;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;

    public AlliesListRefresher(BooleanProperty shouldUpdate, Consumer<String> httpRequestLoggerConsumer, Consumer<List<String>> usersListConsumer) {
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
        httpRequestLoggerConsumer.accept("About to invoke: get_all_allies" + " | Users Request # " + finalRequestNumber);
        HttpClientUtil.runAsync(BASE_URL + "/get_all_allies", new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                JsonArray agents = JsonParser.parseString(response.body().string()).getAsJsonArray();
                List<String> allyArray = new Gson().fromJson(agents, new TypeToken<List<String>>(){}.getType());
                usersListConsumer.accept(allyArray);
            }
        });
    }
}
