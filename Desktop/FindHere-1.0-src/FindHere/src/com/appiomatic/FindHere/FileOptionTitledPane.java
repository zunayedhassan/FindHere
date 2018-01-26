package com.appiomatic.FindHere;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.ToggleSwitch;

/**
 *
 * @author Zunayed Hassan
 */
public class FileOptionTitledPane extends TitledPane {
    private String                  _title        = null;
    private String[]                _optionTitles = null;
    public  SimpleBooleanProperty[] Options       = null;
    
    public FileOptionTitledPane(String title, String[] optionTitles, SimpleBooleanProperty[] options) {
        this._initializeData(title, optionTitles, options);
        this._initializeGUI();
        this._initializeEvents();
        this._initializeFinally();
    }
    
    private void _initializeData(String title, String[] optionTitles, SimpleBooleanProperty[] options) {
        this._title         = title;
        this._optionTitles  = optionTitles;
        this.Options        = options;
    }
    
    private void _initializeGUI() {
        this.setText(this._title);
        
        VBox vbox = new VBox(Program.PADDING);
        vbox.setPadding(new Insets(Program.PADDING * 3));
        
        ScrollPane scrollPane = new ScrollPane(vbox);
        this.setContent(scrollPane);
        
        for (int i = 0; i < this._optionTitles.length; i++) {
            HBox row = new HBox(Program.PADDING);
            
            ToggleSwitch toggleSwitch = new ToggleSwitch();
            
            row.getChildren().addAll(
                    new Label(this._optionTitles[i]),
                    new ToolBarSpacer(),
                    toggleSwitch
            );
            
            vbox.getChildren().add(row);
            
            toggleSwitch.selectedProperty().bindBidirectional(this.Options[i]);
        }
    }
    
    private void _initializeEvents() {
        
    }
    
    private void _initializeFinally() {
        
    }
}
