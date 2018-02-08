package com.appiomatic.FindHere;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author Zunayed Hassan
 */
public class WebPageViewer extends TabPane {
    public  final Tab       WebPageTab        = new Tab("Web Page");
    public  final Tab       SourceCodeTab     = new Tab("Source Code");
    
    private final TextArea  _sourceCodeViewer = new TextArea();
    private final WebView   _browser          = new WebView();
    private final WebEngine _webEngine        = this._browser.getEngine();
    
    
    private       String    _filePath         = null;
    
    public WebPageViewer() {
        this._initializeData();
        this._initializeGUI();
        this._initializeEvents();
        this._initializeFinally();
    }
    
    private void _initializeData() {
        
    }
    
    private void _initializeGUI() {
        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        
        this.getTabs().addAll(
                this.WebPageTab,
                this.SourceCodeTab
        );
        
        this._sourceCodeViewer.setEditable(false);
        this._sourceCodeViewer.setFont(Font.loadFont(this.getClass().getResourceAsStream(Settings.SEARCH_TEXT_FONT), 12));
        this.SourceCodeTab.setContent(this._sourceCodeViewer);
        
        this.WebPageTab.setContent(this._browser);
    }
    
    private void _initializeEvents() {
        
    }
    
    private void _initializeFinally() {
        
    }
    
    public void SetWebPage(String filePath) {
        this._filePath = filePath;
        
        this._sourceCodeViewer.clear();
        List<String> lines = Program.GET_TEXT_FROM_FILE(this._filePath);
        String text = "";

        for (String line: lines) {
            text += line + "\n";
        }

        this._sourceCodeViewer.setText(text);
        
        try {
            String url = new File(this._filePath).toURI().toURL().toString();
            this._webEngine.load(url);
        }
        catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
    }
}
