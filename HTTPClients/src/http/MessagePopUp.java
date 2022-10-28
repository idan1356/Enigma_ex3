package http;

import javafx.scene.control.Alert;

public class MessagePopUp {
    String title;
    String message;

    public MessagePopUp(String title, String message){
        this.message = message;
        this.title = title;
    }

    public void pop(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
