package app.main_app.components.contest.brute_force_input.dictionary_displayer;

import app.main_app.components.contest.brute_force_input.BruteForceInputController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

public class DictionaryController {

    @FXML TextField filterDictionaryTextField;
    @FXML FlowPane wordsFlowPane;
    @FXML ScrollPane dictScrollPane;
    @FXML Label excludedCharsLabel;
    BruteForceInputController parentController;

    @FXML
    public void initialize(){
        dictScrollPane.setFitToWidth(true);
    }

    public void setParentController(BruteForceInputController parentController) {
        this.parentController = parentController;

        filterDictionaryTextField.textProperty().addListener(observable -> {
            wordsFlowPane.getChildren().clear();

            parentController.getDictionary()
                    .findAll(filterDictionaryTextField.textProperty().get().toUpperCase())
                    .forEach(word -> {
                        Button button = new Button();
                        button.setStyle("-fx-text-fill: black;");
                        button.getStyleClass().add("dictionary_button");
                        button.setText(word);
                        wordsFlowPane.getChildren().add(button);

                        button.onActionProperty().set(event -> {
                            TextField userInput = parentController.getUserInputTextField();
                            String curWord = button.getText();
                            String prefix = userInput.textProperty().isEmpty().get() ? "" : " ";

                            userInput.setText(userInput.getText() + prefix + curWord);
                        });
                    });

            excludedCharsLabel.textProperty().bind(parentController.getExcludedCharsProperty());
        });
    }
}
