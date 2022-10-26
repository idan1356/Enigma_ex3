package app.login;
import app.main_app.AllyAppController;
import http.HttpClientUtil;
import http.SimpleCookieManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.Objects;
import static app.utils.AppConstants.BASE_URL;

public class AllyLoginController {
    private static final String MAIN_APP_PATH = "../main_app/ally_main_app.fxml";
    OkHttpClient client = new OkHttpClient.Builder()
            .cookieJar(new SimpleCookieManager())
            .build();

    @FXML TextField UsernameTextField;
    @FXML Label errorMessageLabel;
    @FXML Stage primaryStage;

    @FXML
    public void signInButton(ActionEvent event) throws IOException {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(BASE_URL + "/login")).newBuilder();
        HttpUrl url = urlBuilder
                .addQueryParameter("username", UsernameTextField.getText())
                .addQueryParameter("usertype", "ally")
                .build();

        HttpClientUtil.runAsync(url.toString(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                HttpClientUtil.setCookieManagerLoggingFacility(System.out::println);
                Platform.runLater(
                        ()-> errorMessageLabel.setText("something went wrong, please try again"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    changeSceneToMainApp();
                } else {
                    assert response.body() != null;
                    Platform.runLater(() -> {
                        try {
                            errorMessageLabel.setText(response.body().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });;
                }
            }
        });
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public void changeSceneToMainApp() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(MAIN_APP_PATH));
        TabPane borderPane = fxmlLoader.load();
        AllyAppController allyAppController = fxmlLoader.getController();
        allyAppController.setPrimaryStage(primaryStage);
        primaryStage.getScene().setRoot(borderPane);
    }
}
