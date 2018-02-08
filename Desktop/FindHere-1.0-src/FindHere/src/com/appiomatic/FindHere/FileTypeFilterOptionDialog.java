package com.appiomatic.FindHere;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;

/**
 *
 * @author Zunayed Hassan
 */
public class FileTypeFilterOptionDialog extends Dialog {
    private final Accordion     _accordion         = new Accordion();
    
    private       Program       _program           = null;
    
    private final Button        _selectAllButton   = new Button("Select All");    
    private final Button        _deselectAllButton = new Button("Deselect All");    
    
    public FileTypeFilterOptionDialog(Program program) {
        this._initializeData(program);
        this._initializeGUI();
        this._initializeEvents();
        this._initializeFinally();
    }
    
    private void _initializeData(Program program) {
        this._program = program;
    }
    
    private void _initializeGUI() {
        this.setTitle("Settings");
        this.setHeaderText("Choose File Types to Search");
        this.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream("icons/settings-32.png"))));
        ((Stage) this.getDialogPane().getScene().getWindow()).getIcons().add(new Image(this.getClass().getResourceAsStream("icons/icon.png")));
                
        this._accordion.setMinHeight(300);
        this._accordion.setMaxHeight(300);
        this.setResizable(false);
        
        BorderPane root = new BorderPane();
        root.setCenter(this._accordion);
        this.getDialogPane().setContent(root);
        
        this._accordion.getPanes().addAll(
                new FileOptionTitledPane("Office Documents", this._program.MsOfficeFileTypeTitles,    this._program.MsOfficeFileTypeOptions),
                new FileOptionTitledPane("Text/Web/Code",    this._program.TextRelatedFileTypeTitles, this._program.TextRelatedFileTypeOptions),
                new FileOptionTitledPane("Other",            this._program.OtherFileTypeTitles,       this._program.OtherFileTypeOptions)
        );
        
        HBox topToolBar = new HBox(
                Program.PADDING,
                this._deselectAllButton,
                this._selectAllButton
        );
        
        topToolBar.setPadding(new Insets(0, 0, Program.PADDING * 2, 0));
        topToolBar.setAlignment(Pos.CENTER);
        
        root.setTop(topToolBar);
        
        this.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
    }
    
    private void _initializeEvents() {        
        // Deselect All
        this._deselectAllButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (TitledPane titledPane: _accordion.getPanes()) {
                    if (titledPane.isExpanded()) {
                        for (Node node: ((VBox) ((ScrollPane) titledPane.getContent()).getContent()).getChildren()) {
                            HBox row = (HBox) node;
                            ToggleSwitch toggle = (ToggleSwitch) row.getChildren().get(2);

                            toggle.setSelected(false);
                        }
                    }
                }
            }
        });
        
        // Select All
        this._selectAllButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (TitledPane titledPane: _accordion.getPanes()) {
                    if (titledPane.isExpanded()) {
                        for (Node node: ((VBox) ((ScrollPane) titledPane.getContent()).getContent()).getChildren()) {
                            HBox row = (HBox) node;
                            ToggleSwitch toggle = (ToggleSwitch) row.getChildren().get(2);

                            toggle.setSelected(true);
                        }
                    }
                }
            }
        });
    }
    
    private void _initializeFinally() {
        if (this._accordion.getPanes().size() > 0) {
            this._accordion.setExpandedPane(this._accordion.getPanes().get(0));
        }
    }
    
    public void Show() {
        this.showAndWait();
    }
}
