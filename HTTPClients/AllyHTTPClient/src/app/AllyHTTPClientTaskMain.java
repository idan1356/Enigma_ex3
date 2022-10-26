package app;

import app.login.AllyLoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class AllyHTTPClientTaskMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("login/ally_login.fxml");
        loader.setLocation(mainFXML);
        VBox root = loader.load();

        AllyLoginController loginController = loader.getController();
        loginController.setPrimaryStage(primaryStage);

        //set stage
        primaryStage.setTitle("Ally App");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}