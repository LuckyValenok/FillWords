package net.luckyvalenok.fillwords;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ScreenController {
    protected void activate(Stage stage, String name) throws IOException {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource(name))));
        stage.show();
    }
}