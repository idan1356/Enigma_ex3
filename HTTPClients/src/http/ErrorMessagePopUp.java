package http;

import javafx.scene.control.Alert;

public class ErrorMessagePopUp {
    Exception exception;
    String title;

    public ErrorMessagePopUp(Exception exception, String title){
        this.exception = exception;
        this.title = title;
    }

    public void pop(){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(title);
        errorAlert.setContentText(exception.getMessage());
        errorAlert.showAndWait();
    }
}
