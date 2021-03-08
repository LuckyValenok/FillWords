package net.luckyvalenok.fillwords;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Game extends Application {
    @FXML
    private Button start, proceed;
    
    private ScreenController screenController;
    
    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        screenController = new ScreenController();
        screenController.activate(primaryStage, "/main.fxml");
        
        primaryStage.show();
    }
    
    @FXML
    public void onStart(ActionEvent event) throws IOException {
        screenController.activate((Stage) start.getScene().getWindow(), "/game.fxml");
    }
    
    @FXML
    public void onProceed(ActionEvent event) throws IOException {
        screenController.activate((Stage) proceed.getScene().getWindow(), "/game.fxml");
    }
}
