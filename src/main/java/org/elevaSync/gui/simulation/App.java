package org.elevaSync.gui.simulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.elevaSync.gui.constants.Constants;

import java.io.IOException;

public class App extends Application {

    public void start(Stage primaryStage) throws IOException {
        Parent menuView = loadMenuView();
        configureMenu(primaryStage, menuView);
        primaryStage.show();
    }

    private Parent loadMenuView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(Constants.MENU_FXML_PATH));
        return loader.load();
    }

    private void configureMenu(Stage primaryStage, Parent menuView) {
        Scene scene = new Scene(menuView);
        scene.getStylesheets().add(getClass().getClassLoader().getResource(Constants.MENU_CSS_PATH).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle(Constants.ELEVASYNC_MENU);
        primaryStage.minHeightProperty();
        primaryStage.minWidthProperty();
    }

}
