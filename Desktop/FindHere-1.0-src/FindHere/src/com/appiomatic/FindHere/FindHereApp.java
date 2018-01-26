package com.appiomatic.FindHere;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Zunayed Hassan
 */
public class FindHereApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Program program = new Program(primaryStage);
        Scene   scene   = new Scene(program);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
