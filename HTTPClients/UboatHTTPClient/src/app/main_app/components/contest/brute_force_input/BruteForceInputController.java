package app.main_app.components.contest.brute_force_input;

import app.main_app.components.contest.ContestController;
import app.main_app.components.contest.brute_force_input.dictionary_displayer.DictionaryController;
import http.HttpClientUtil;
import app.utils.trie.Trie;
import com.google.gson.Gson;
import engine.machine.components.enigma_factory.enigma.generated.CTEDecipher;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import static app.utils.AppConstants.BASE_URL;
import static app.utils.AppUtils.REFRESH_RATE;

public class BruteForceInputController {
    //components
    @FXML TextField userInputTextField;
    @FXML Button clearUserInputButton;
    //subcomponents
    @FXML VBox dictionaryComponent;
    @FXML DictionaryController dictionaryComponentController;
    @FXML Label UboatProcessMessage;
    @FXML Label uboatOriginalMessage;
    @FXML Button readyButton;

    SimpleStringProperty excludedChars;
    SimpleObjectProperty<Trie> dictionary;
    SimpleBooleanProperty isMatchStarted;
    SimpleBooleanProperty isReady;
    ContestController parentController;

    public BruteForceInputController(){
        dictionary = new SimpleObjectProperty<>(new Trie());
        excludedChars = new SimpleStringProperty();
        isReady = new SimpleBooleanProperty(false);
        isMatchStarted = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize(){
        dictionaryComponentController.setParentController(this);
        readyButton.disableProperty().bind(UboatProcessMessage.textProperty().isEmpty()
                .or(isReady));
    }

    @FXML
    public void clearTextButtonAction(){
        userInputTextField.setText("");
    }

    public void setParentController(ContestController parentController) {
        this.parentController = parentController;
    }

    public Trie getDictionary() {
        return dictionary.get();
    }

    public SimpleStringProperty getExcludedCharsProperty() {
        return excludedChars;
    }

    public TextField getUserInputTextField() {
        return userInputTextField;
    }

    @FXML
    public void processInputButtonAction() {
        //update brute force controller of new DM task settings
        String url = Objects.requireNonNull(HttpUrl.parse(BASE_URL + "/process_uboat_input")).newBuilder()
                .addQueryParameter("uboatinput", userInputTextField.getText())
                .build().toString();

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                System.out.println("response code is " + response.code());
                   System.out.println(response.header("original"));
                   System.out.println(response.header("processed"));
                if(response.isSuccessful()){
                    Platform.runLater(() -> {
                        uboatOriginalMessage.setText(response.header("original"));
                        UboatProcessMessage.setText(response.header("processed"));
                    });
                }
            }
        });
    }

    public void loadDataDecipherState(){
        loadData();
    }

    public void loadData() {
        HttpClientUtil.runAsync(BASE_URL + "/decipher_state", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                CTEDecipher decipher = new Gson().fromJson(response.body().string(), CTEDecipher.class);
                excludedChars.set(decipher.getCTEDictionary().getExcludeChars());

                String words = decipher.getCTEDictionary().getWords().trim().toUpperCase()
                        .replaceAll(String.format("[%s]", excludedChars.getValue()), "");

                Arrays.stream(words.split(" "))
                        .collect(Collectors.toList())
                        .forEach(word -> dictionary.get().insert(word));
            }
        } );
        //TODO: remove chars not in ABC
    }

    public void cleanInputLabels(){
        Platform.runLater(() -> {
            UboatProcessMessage.setText(null);
            uboatOriginalMessage.setText(null);
        });
    }
    public void readyOnAction(){
        HttpClientUtil.runAsync(BASE_URL + "/set_ready", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if(response.isSuccessful()){
                    isReady.setValue(true);
                    startCompetitionTimerTask();
                }
            }
        });
    }
    public void startCompetitionTimerTask(){
        TimerTask startCompetitionTimerTask = new TimerTask() {
            @Override
            public void run() {
                HttpClientUtil.runAsync(BASE_URL + "/start_competition", new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if(response.isSuccessful()){
                            isMatchStarted.set(true);
                        }
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(startCompetitionTimerTask, REFRESH_RATE, REFRESH_RATE);
    }
}
