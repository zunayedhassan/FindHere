package com.appiomatic.FindHere;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Zunayed Hassan
 */
public class IconButton extends Button {
    public static enum POSITION {
        TOP,
        LEFT
    }
    
    private String   _icon         = null;
    private String   _title        = null;
    private POSITION _iconPosition = null;
    
    public IconButton(String title, String icon) {
        this(title, icon, POSITION.LEFT);
    }
    
    public IconButton(String title, String icon, POSITION iconPosition) {
        this._initializeData(title, icon, iconPosition);
        this._initializeGUI();
        this._initializeEvents();
        this._initializeFinally();
    }
    
    private void _initializeData(String title, String icon, POSITION iconPosition) {
        this._title        = title;
        this._icon         = icon;
        this._iconPosition = iconPosition;
    }
    
    private void _initializeGUI() {
        if (this._title != null) {
            this.setText(this._title);
        }
        
        if (this._icon != null) {
            this.setGraphic(new ImageView(new Image(this.getClass().getResourceAsStream(this._icon))));
            
            if (this._iconPosition == POSITION.TOP) {
                this.setContentDisplay(ContentDisplay.TOP);
            }
        }
    }
    
    private void _initializeEvents() {
        
    }
    
    private void _initializeFinally() {
        
    }
}
