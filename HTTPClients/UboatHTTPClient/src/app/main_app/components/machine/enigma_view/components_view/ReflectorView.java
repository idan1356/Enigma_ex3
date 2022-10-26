package app.main_app.components.machine.enigma_view.components_view;
import app.utils.machine_state.enigma_state.components_state.ReflectorState;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ReflectorView extends VBox{

    public ReflectorView(ReflectorState reflectorState){
        Label header = new Label("Reflector " + reflectorState.getReflectorID().toString());
        header.setStyle("-fx-text-fill: black");

        this.getChildren().add(header);
        reflectorState.getReflector().forEach(reflect -> {
            Label label = new Label(reflect.toString());
            label.setStyle("-fx-text-fill: black;");
            this.getChildren().add(label);
        }
        );
        this.setAlignment(Pos.CENTER);
    }
}
