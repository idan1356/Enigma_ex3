package app.login;

import app.main_app.AgentAppController;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

import static http.HttpClientUtil.REFRESH_RATE;

public class AgentLoginController {
    @FXML Button signInButton;
    @FXML TextField agentName;
    @FXML Slider numOfThreadsSlider;
    @FXML TextField missionSizeTextField;
    @FXML ComboBox<String> allyNameList;
    @FXML Stage primaryStage;
    @FXML Label errorLabel;
    ObservableList<String> allies;
    private Timer timer;
    private TimerTask refresher;

    private static final String MAIN_APP_PATH = "../main_app/main_app.fxml";
    public AgentLoginController(){
        allies = FXCollections.observableList(new ArrayList<>());
    }

    private static final String BASE_URL ="http://localhost:8080/EnigmaWebApp_Web_exploded";

    @FXML
    public void initialize(){
        signInButton.disableProperty().bind(agentName.textProperty().isEmpty()
                .or(allyNameList.valueProperty().isNull())
                .or(missionSizeTextField.textProperty().isEmpty()));

        allyNameList.setItems(allies);

        allyNameList.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                allyNameList.setValue(oldValue);
            } else {
                allyNameList.setValue(newValue);
            }
        });

        startAgentListRefresher();
    }

    @FXML
    public void signInButton() {
        String url = Objects.requireNonNull(HttpUrl.parse(BASE_URL + "/login")).newBuilder()
                .addQueryParameter("numofmissions", missionSizeTextField.getText())
                .addQueryParameter("numofthreads", String.valueOf((int)numOfThreadsSlider.getValue()))
                .addQueryParameter("username", agentName.getText())
                .addQueryParameter("allyname", allyNameList.getValue())
                .addQueryParameter("usertype", "agent")

                .build().toString();

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                HttpClientUtil.setCookieManagerLoggingFacility(System.out::println);
                Platform.runLater(
                        ()-> errorLabel.setText("something went wrong, please try again"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    changeSceneToMainApp();
                    return;
                }

                assert response.body() != null;
                Platform.runLater(()-> {
                    try {
                        errorLabel.setText(response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void updateAllyList(List<String> allies){
        Platform.runLater(
                () -> {
                    this.allies.clear();
                    this.allies.addAll(allies);
                }
        );
    }

    public void startAgentListRefresher(){
        SimpleBooleanProperty simpleBooleanProperty = new SimpleBooleanProperty(true);
        refresher =  new AlliesListRefresher(simpleBooleanProperty,
                System.out::println, this::updateAllyList);
        timer = new Timer();
        timer.schedule(refresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void changeSceneToMainApp() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(MAIN_APP_PATH));
        BorderPane borderPane = fxmlLoader.load();

        AgentAppController agentAppController = fxmlLoader.getController();
        agentAppController.setPrimaryStage(primaryStage);

        agentAppController.setAgentName(agentName.getText());
        agentAppController.setAllyName(allyNameList.getValue());
        agentAppController.setNumOfThreads((int)numOfThreadsSlider.getValue());
        primaryStage.getScene().setRoot(borderPane);
    }
}
