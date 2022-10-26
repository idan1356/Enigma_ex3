package app.main_app.components.machine.enigma_view.components_view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Arrays;

public class KeyboardView extends VBox {

    public KeyboardView(String ABC){
        this.getChildren().add(new Label("Keyboard"));

        Arrays.stream(ABC.split("")).forEach(letter -> {
            Label label = new Label(letter);
            label.setStyle("-fx-text-fill: black;");
            this.getChildren().add(label);
        });
        this.setAlignment(Pos.CENTER);
    }
}
