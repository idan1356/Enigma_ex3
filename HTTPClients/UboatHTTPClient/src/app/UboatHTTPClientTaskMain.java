package app;

import app.login.UboatLoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class UboatHTTPClientTaskMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("login/login.fxml");
        loader.setLocation(mainFXML);
        VBox root = loader.load();

        UboatLoginController loginController = loader.getController();
        loginController.setPrimaryStage(primaryStage);

        //set stage
        primaryStage.setTitle("Enigma App");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}