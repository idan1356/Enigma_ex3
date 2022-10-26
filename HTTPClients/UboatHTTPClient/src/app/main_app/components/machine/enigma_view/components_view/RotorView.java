package app.main_app.components.machine.enigma_view.components_view;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RotorView extends VBox {
    Map<String, Label> left;
    Map<String, Label> right;

    public RotorView(List<Pair<String, String>> positioning, int rotorID, int rotorOffset, int notch){
        left = new HashMap<>();
        right = new HashMap<>();

        //create list and rearrange it based on current offset
        List<Pair<String, String>> sublist1 = new ArrayList<>();
        if (rotorOffset != 0)
            sublist1 = positioning.subList(0, rotorOffset);

        List<Pair<String, String>> sublist2 = positioning.subList(rotorOffset, positioning.size());

        List<Pair<String, String>> newList = Stream.concat(sublist2.stream(), sublist1.stream())
                .collect(Collectors.toList());

        //add label for rotor and add rows
        Label label = new Label("Rotor " + rotorID);
        label.setStyle("-fx-text-fill: black;");
        this.getChildren().add(label);
        newList.forEach(this::addRow);

        //find notch and color it
        String notchKey = positioning.get(notch - 1).getValue();

        right.get(notchKey).getParent().setStyle("-fx-background-color: white;");
        this.setAlignment(Pos.CENTER);
    }
    private void addRow(Pair<String, String> positioning){
        HBox positioningRow = new HBox();
        positioningRow.setSpacing(30);
        positioningRow.setAlignment(Pos.CENTER);

        Label leftLabel = new Label(positioning.getKey());
        leftLabel.setStyle("-fx-text-fill: black;");
        left.put(positioning.getKey(), leftLabel);

        Label rightLabel = new Label(positioning.getValue());
        rightLabel.setStyle("-fx-text-fill: black;");

        right.put(positioning.getValue(), rightLabel);

        positioningRow.getChildren().add(leftLabel);
        positioningRow.getChildren().add(rightLabel);

        this.getChildren().add(positioningRow);
    }
}
