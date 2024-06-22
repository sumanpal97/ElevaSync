package org.elevaSync.gui.simulation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;
import org.elevaSync.gui.constants.Constants;
import org.elevaSync.gui.utils.FloorPlan;

import java.io.IOException;

public class Menu {
    @FXML
    private Spinner<Integer> floors;
    @FXML
    private Spinner<Integer> elevators;

    @FXML
    private void Start() {
        try {
            FXMLLoader loader = loadSimulationView();
            Parent root = loader.load();
            Simulation simulation = loader.getController();
            simulation.initData(readInput());
            Stage simulationStage = configureSimulation(root);
            simulationStage.show();
            simulationStage.setOnCloseRequest(event -> onClose(simulation, simulationStage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FXMLLoader loadSimulationView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(Constants.SIMULATION_FXML_PATH));
        return loader;
    }

    private FloorPlan readInput() {
        return new FloorPlan(elevators.getValue(), floors.getValue());
    }

    private Stage configureSimulation(Parent root) {
        Stage simulationStage = new Stage();
        simulationStage.setTitle(Constants.ELEVASYNC_SIMULATION);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getClassLoader().getResource(Constants.SIMULATION_CSS_PATH).toExternalForm());
        simulationStage.setScene(scene);
        return simulationStage;
    }

    private void onClose(Simulation simulationController, Stage simulationStage) {
        if (simulationController != null) {
            simulationController.stopSimulation();
        }
        simulationStage.close();
    }
}