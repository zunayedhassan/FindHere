package com.appiomatic.FindHere;

import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Zunayed Hassan
 */
public class ImageViewer extends BorderPane {
    private       String    _filePath  = null;
    
    private final ImageView _imageView = new ImageView();
    
    public ImageViewer() {
        this._initializeData();
        this._initializeGUI();
        this._initializeEvents();
        this._initializeFinally();
    }
    
    private void _initializeData() {
        
    }
    
    private void _initializeGUI() {
        this.setCenter(new ScrollPane(this._imageView));
    }
    
    private void _initializeEvents() {
        
    }
    
    private void _initializeFinally() {
        
    }
    
    public void SetImage(String filePath) {
        this._filePath = filePath;
        this._imageView.setImage(new Image("file:" + this._filePath));
    }
}
