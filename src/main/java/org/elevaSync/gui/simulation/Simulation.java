package org.elevaSync.gui.simulation;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import org.elevaSync.System.Elevator.Elevator;
import org.elevaSync.System.Engine.Engine;
import org.elevaSync.System.utilities.Call;
import org.elevaSync.System.utilities.ChangeObserver;
import org.elevaSync.System.utilities.MoveDirection;
import org.elevaSync.gui.utils.FloorPlan;
import org.elevaSync.gui.utils.IconProvider;

import java.net.URL;
import java.util.List;

public class Simulation implements ChangeObserver {
    private final int width = 30;
    private final int height = 35;
    private final IconProvider guiElevatorLoader = new IconProvider();
    private int floors;
    private int elevators;
    private Engine engine;
    private Thread engineThread;

    @FXML
    private GridPane buttonGrid;
    @FXML
    private GridPane elevatorGrid;
    @FXML
    private GridPane innerGrid;

    public void initData(FloorPlan floorPlan) {
        this.floors = floorPlan.getFloors();
        this.elevators = floorPlan.getElevators();
        this.engine = new Engine(elevators);
        setupButtons(floors);
        setupElevators(elevators);
        setupInner(elevators);
        engine.addObserver(this);
        engineThread = new Thread(engine);
        engineThread.start();
    }

    @Override
    public void refreshView(List<Elevator> elevators) {
        Platform.runLater(() -> {
            clearGrid();
            setupElevators(elevators.size());
            for (int i = 0; i < elevators.size(); i++) {
                Elevator elevator = elevators.get(i);
                ImageView lift = guiElevatorLoader.getElevatorIcon(elevator.getDirection(), elevator.getStatus());
                elevatorGrid.add(lift, i+1, floors-elevator.getCurrentFloor()+1);
            }
        });
    }

    public void stopSimulation() {
        engine.breakSimulation();
        engineThread.interrupt();
    }

    private void setupButtons(int floors) {
        for (int i = 0; i < 3; i++) {
            buttonGrid.getColumnConstraints().add(new ColumnConstraints(width+6));
        }

        for (int i = 0; i < floors; i++) {
            buttonGrid.getRowConstraints().add(new RowConstraints(height));
            Label label = new Label(Integer.toString(floors-i));
            buttonGrid.add(label, 0, i);
            GridPane.setHalignment(label, HPos.CENTER);
        }
        for (int i = 1; i < floors; i++) {
            configureUpwardButton(i);
        }
        for (int i = 0; i < floors-1; i++) {
            configureDownwardButton(i);
        }
    }

    private void configureUpwardButton(int i){
        Button button = new Button();

        URL resource = getClass().getClassLoader().getResource("icons/up-arrow.png");
        Image iconImage = new Image(resource.toString()); // Replace with your actual icon path
        ImageView iconView = new ImageView(iconImage);
        iconView.setFitWidth(18); // Set the width
        iconView.setFitHeight(18); // Set the height
        button.setGraphic(iconView);

        int finalI = floors-i;
        button.setOnAction(event -> callElevator(finalI, MoveDirection.UP));
        buttonGrid.add(button, 1, i);
        GridPane.setHalignment(button, HPos.CENTER);
    }

    private void configureDownwardButton(int i){
        Button button = new Button();

        URL resource = getClass().getClassLoader().getResource("icons/down-arrow.png");
        Image iconImage = new Image(resource.toString()); // Replace with your actual icon path
        ImageView iconView = new ImageView(iconImage);
        iconView.setFitWidth(18); // Set the width
        iconView.setFitHeight(18); // Set the height
        button.setGraphic(iconView);

        int finalI = floors-i;
        button.setOnAction(event -> callElevator(finalI, MoveDirection.DOWN));
        buttonGrid.add(button, 2, i);
        GridPane.setHalignment(button, HPos.CENTER);
    }

    private void setupElevators(int elevators) {
        //first row from top
        elevatorGrid.getRowConstraints().add(new RowConstraints(height));
        for (int i = 0; i < elevators; i++) {
            elevatorGrid.getColumnConstraints().add(new ColumnConstraints(width+6));
            Label label = new Label(Integer.toString(i+1));
            elevatorGrid.add(label, i+1, 0);
            GridPane.setHalignment(label, HPos.CENTER);

            configureOOSButton(i);
        }
        elevatorGrid.getRowConstraints().add(new RowConstraints(height));

        for (int i = 0; i < floors; i++) {
            elevatorGrid.getRowConstraints().add(new RowConstraints(height));
            Label label = new Label(Integer.toString(floors-i));
            elevatorGrid.add(label, 0, i+1);
            GridPane.setHalignment(label, HPos.CENTER);
        }
    }

    private void configureOOSButton(int i){
        Button button = new Button();

        URL resource = getClass().getClassLoader().getResource("icons/oos.png");
        Image iconImage = new Image(resource.toString()); // Replace with your actual icon path
        ImageView iconView = new ImageView(iconImage);
        iconView.setFitWidth(18); // Set the width
        iconView.setFitHeight(18); // Set the height
        button.setGraphic(iconView);

        int finalI = i;
        button.setOnAction(event -> engine.toggle(finalI));
        elevatorGrid.add(button, i+1, floors+1);
        GridPane.setHalignment(button, HPos.CENTER);
    }

    private void setupInner(int elevators) {
        for (int i = 0; i < elevators; i++) {
            innerGrid.getRowConstraints().add(new RowConstraints(height));
            Spinner<Integer> spinner = new Spinner<>(1, floors, 1);
            spinner.setPrefSize(60, 30);
            Label label = new Label(" Elevator " + (i+1) + " ");
            Button button = configureGoButton();
            int finalI = i;
            button.setOnAction(event -> engine.internalCall(finalI, spinner.getValue()));
            innerGrid.add(label, 0, i);
            innerGrid.add(spinner, 1, i);
            innerGrid.add(button, 2, i);
        }
    }

    private Button configureGoButton(){
        Button button = new Button();

        URL resource = getClass().getClassLoader().getResource("icons/go.png");
        Image iconImage = new Image(resource.toString()); // Replace with your actual icon path
        ImageView iconView = new ImageView(iconImage);
        iconView.setFitWidth(18); // Set the width
        iconView.setFitHeight(18); // Set the height
        button.setGraphic(iconView);
        return button;
    }

    private void clearGrid() {
        elevatorGrid.getChildren().retainAll(elevatorGrid.getChildren().get(0));
        elevatorGrid.getColumnConstraints().clear();
        elevatorGrid.getRowConstraints().clear();
    }

    private void callElevator(int floor, MoveDirection direction) {
        engine.addCall(new Call(floor, direction));
    }
}
