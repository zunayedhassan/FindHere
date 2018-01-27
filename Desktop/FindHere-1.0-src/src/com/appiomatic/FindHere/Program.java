package com.appiomatic.FindHere;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;

/**
 *
 * @author Zunayed Hassan
 */
public class Program extends BorderPane {
    public  final static int                        PADDING                              = 5;
    public  final static List<String>               TEXT_FILE_TYPES                      = Program.GET_TEXT_FROM_FILE("text-file-search.csv");;
    
    private              Stage                      _stage                               = null;
    private              Search                     _search                              = null;
    private              FileTypeFilterOptionDialog _fileFilterOptionDialog              = null;
    
    private final        Label                      _statusLabel                         = new Label();
    private final        ProgressBar                _progressBar                         = new ProgressBar();
    private final        ToolBar                    _bottomToolBar                       = new ToolBar();
    private final        BorderPane                 _splitPane                           = new BorderPane();
    private final        BorderPane                 _centerPane                          = new BorderPane();
    private final        BorderPane                 _rootLeft                            = new BorderPane();
    private final        BorderPane                 _topPane                             = new BorderPane();
    private final        TextArea                   _searchArea                          = new TextArea();
    private final        IconButton                 _searchButton                        = new IconButton("Search", "icons/start.png", IconButton.POSITION.TOP);
    private final        IconButton                 _stopButton                          = new IconButton("Stop",   "icons/stop.png",  IconButton.POSITION.TOP);
    private final        IconButton                 _clearButton                         = new IconButton("Clear",  "icons/clear.png", IconButton.POSITION.TOP);
    private final        Label                      _topTitleLabel                       = new Label("Type Your Search Term Here");
    private final        ScrollPane                 _settingsScrollPane                  = new ScrollPane();
    private final        GridPane                   _settingsPane                        = new GridPane();
    private final        TextField                  _folderLocationTextField             = new TextField();
    private final        IconButton                 _browseButton                        = new IconButton("Browse", "icons/folder.png");
    private final        IconButton                 _fileTypesOptionButton               = new IconButton("Filter Option", "icons/settings.png");
    private final        ToggleSwitch               _isCaseSensativeToggleSwitch         = new ToggleSwitch();
    private final        ToggleSwitch               _isFindOnSubDirectoriesToggleSwitch  = new ToggleSwitch();
    private final        ToggleSwitch               _isFindOnFilesToggleSwitch           = new ToggleSwitch();
    private final        SplitPane                  _rootCenter                          = new SplitPane();
    private final        BorderPane                 _searchResultPane                    = new BorderPane();
    private final        BorderPane                 _previewPane                         = new BorderPane();
    private final        Alert                      _alert                               = new Alert(Alert.AlertType.WARNING, "You need to type something to search", ButtonType.CLOSE);
    private final        TableView<SearchResult>    _searchResultTable                   = new TableView<>();
    private final        Tooltip                    _baseFolderLocationToolTip           = new Tooltip();
    private final        DirectoryChooser           _baseChooserDialog                   = new DirectoryChooser();
    private final        TextArea                   _textPreviewArea                     = new TextArea();
    private final        WebPageViewer              _webPageViewer                       = new WebPageViewer();
    private final        ImageViewer                _imageViewer                         = new ImageViewer();
    private final        StackPane                  _noPreviewPane                       = new StackPane(new Label("Nothing to Preview Here"));
    private final        Button                     _exploreButton                       = new Button("Open File Location");
    
    public  final        String[]                   MsOfficeFileTypeTitles               = new String[] { "MS Word", "MS Excel", "MS PowerPoint" };
    public  final        SimpleBooleanProperty[]    MsOfficeFileTypeOptions              = new SimpleBooleanProperty[] { new SimpleBooleanProperty(true), new SimpleBooleanProperty(true), new SimpleBooleanProperty(true) };
    public  final        String[]                   OtherFileTypeTitles                  = new String[] { "PDF" };
    public  final        SimpleBooleanProperty[]    OtherFileTypeOptions                 = new SimpleBooleanProperty[] { new SimpleBooleanProperty(false) };
    public  final        String[]                   TextRelatedFileTypeTitles            = new String[TEXT_FILE_TYPES.size()];
    public  final        SimpleBooleanProperty[]    TextRelatedFileTypeOptions           = new SimpleBooleanProperty[TEXT_FILE_TYPES.size()];
    
    public Program(Stage stage) {
        this._initializeData(stage);
        this._initializeGUI();
        this._initializeEvents();
        this._initializeFinally();
    }
    
    private void _initializeData(Stage stage) {
        this._stage = stage;
        
        for (int i = 0; i < TextRelatedFileTypeOptions.length; i++) {
            TextRelatedFileTypeTitles[i]  = TEXT_FILE_TYPES.get(i);
            TextRelatedFileTypeOptions[i] = new SimpleBooleanProperty(true);
        }
    }
    
    private void _initializeGUI() {
        /* Window */
        this.SetupWindow();
        
        /* Status */
        this.setBottom(this._bottomToolBar);
        this._bottomToolBar.getItems().addAll(
                this._statusLabel,
                new ToolBarSpacer(),
                this._progressBar
        );
        
        this._progressBar.setProgress(0);
        
        /* Dialog */
        this._fileFilterOptionDialog = new FileTypeFilterOptionDialog(this);
        
        /* Search Settings */
        this._rootLeft.setTop(this._topPane);
        this._searchButton.setMinWidth(100);
        this._searchButton.setStyle("-fx-background-radius: 0;");
        this._stopButton.setMinWidth(100);
        this._stopButton.setStyle("-fx-background-radius: 0;");
        this._clearButton.setMinWidth(100);
        this._clearButton.setStyle("-fx-background-radius: 0;");
        
        BorderPane searchAreaCenterPane = new BorderPane();
        searchAreaCenterPane.setPadding(new Insets(Program.PADDING));
        
        this._topPane.setTop(new ToolBar(this._topTitleLabel));
        this._topPane.setCenter(searchAreaCenterPane);
        
        searchAreaCenterPane.setCenter(this._searchArea);
        searchAreaCenterPane.setRight(new VBox(this._searchButton, this._stopButton, this._clearButton));
        
        this._searchArea.setFont(Font.loadFont(this.getClass().getResourceAsStream(Settings.SEARCH_TEXT_FONT), 13));
        this._searchArea.setStyle("-fx-background-radius: 0;");
        
        BorderPane settingsRootPane = new BorderPane(this._settingsScrollPane);
        settingsRootPane.setTop(new ToolBar(new Label("Settings")));
        
        this._rootLeft.setCenter(settingsRootPane);
        this._settingsPane.setPadding(new Insets(Program.PADDING));
        this._settingsScrollPane.setContent(this._settingsPane);
        
        this._settingsPane.setHgap(Program.PADDING);
        this._settingsPane.setVgap(Program.PADDING);
        
        this._baseChooserDialog.setTitle("Choose search location");
        this._folderLocationTextField.setTooltip(this._baseFolderLocationToolTip);
        this._folderLocationTextField.setEditable(false);
        this._settingsPane.add(new Label("Search Location"),              0, 0);
        this._settingsPane.add(this._folderLocationTextField,             1, 0);
        this._settingsPane.add(this._browseButton,                        2, 0);
        this._settingsPane.add(new Label("Select file type"),             0, 1);
        this._settingsPane.add(this._fileTypesOptionButton,               1, 1);
        this._settingsPane.add(new Label("Is Case Sensative"),            0, 2);
        this._settingsPane.add(this._isCaseSensativeToggleSwitch,         1, 2);
        this._settingsPane.add(new Label("Is Find on Sub Directories"),   0, 3);
        this._settingsPane.add(this._isFindOnSubDirectoriesToggleSwitch,  1, 3);
        this._settingsPane.add(new Label("Is Find on Files"),             0, 4);
        this._settingsPane.add(this._isFindOnFilesToggleSwitch,           1, 4);
        
        this._rootLeft.setMaxWidth(400);
        
        /* Search Result and Preview */
        this._rootCenter.getItems().addAll(this._searchResultPane, this._previewPane);
        this._rootCenter.setOrientation(Orientation.VERTICAL);
        this._rootCenter.setDividerPositions(0.5, 0.5);
        
        this._searchResultPane.setTop(new ToolBar(new Label("Search Result")));
        this._previewPane.setTop(new ToolBar(new Label("Preview"), new ToolBarSpacer(), this._exploreButton));
        
        /* Search Result */
        this._searchResultPane.setCenter(this._searchResultTable);
        this._searchResultTable.setPlaceholder(new Label("No items match your search"));
        
        this._searchResultTable.getColumns().addAll(
                SearchResultTableHelper.GET_ICON_COLUMN(),
                SearchResultTableHelper.GET_NAME_COLUMN(),
                SearchResultTableHelper.GET_SIZE_COLUMN(),
                SearchResultTableHelper.GET_PATH_COLUMN(),
                SearchResultTableHelper.GET_CREATED_COLUMN(),
                SearchResultTableHelper.GET_MODIFIED_COLUMN(),
                SearchResultTableHelper.GET_ACCESSED_COLUMN()
        );
        
        /* Main Body */
        this.setCenter(this._centerPane);
        this._centerPane.setCenter(this._splitPane);
        
        this._splitPane.setLeft(this._rootLeft);
        this._splitPane.setCenter(this._rootCenter);
        
        /* Alert */
        ((Stage) this._alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(this.getClass().getResourceAsStream(Settings.APP_ICON)));
        
        /* Preview */
        this._textPreviewArea.setEditable(false);
        this._textPreviewArea.setFont(Font.loadFont(this.getClass().getResourceAsStream(Settings.SEARCH_TEXT_FONT), 12));
        
        this._previewPane.setCenter(this._noPreviewPane);
    }
    
    private void _initializeEvents() {
        // Search Button Height
        this._searchArea.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number height) {
                _searchButton.setPrefHeight(height.doubleValue() / 3.0);
                _stopButton.setPrefHeight(height.doubleValue() / 3.0);
                _clearButton.setPrefHeight(height.doubleValue() / 3.0);
            }
        });
        
        // Search
        this._searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _search();
            }
        });
        
        // Stop
        this._stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _stop();
            }
        });
        
        // Clear
        this._clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _clear();
            }
        });
        
        // Base folder location text
        this._folderLocationTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String text) {
                _baseFolderLocationToolTip.setText(text);
            }
        });
        
        // Search location button
        this._browseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _chooseSearchFolderLocation();
            }
        });
        
        // Filter Option Button
        this._fileTypesOptionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _fileFilterOptionDialog.Show();
            }
        });
        
        // Table
        this._searchResultTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SearchResult>() {
            @Override
            public void changed(ObservableValue<? extends SearchResult> observable, SearchResult oldValue, SearchResult selectedItem) {
                _onSearchResultTableRowSelected(selectedItem);
            }
        });
        
        // Explore
        this._exploreButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SearchResult selectedItem = _searchResultTable.getSelectionModel().getSelectedItem();
                
                if (selectedItem != null) {
                    File file = new File(selectedItem.path);
                    
                    String path = null;
                    
                    if (file.isFile()) {
                        path = new File(selectedItem.path).getParent();
                    }
                    else {
                        path = new File(selectedItem.path).toString();
                    }
                    
                    try {
                        String command = "";
                        
                        if (OsValidator.IS_WINDOWS()) {
                            if (!file.isFile()) {
                                command = "explorer.exe " + path;
                            }
                            else {
                                command = "explorer.exe " + path;
                            }
                        }
                        else if (OsValidator.IS_MAC()) {
                            command = "open " + path;
                        }
                        else if (OsValidator.IS_UNIX()) {
                            command = "nautilus " + path;
                        }
                        
                        Runtime.getRuntime().exec(command);
                    }
                    catch (IOException exception) {
                        if (OsValidator.IS_UNIX()) {
                            try {
                                String command = "dolphin " + path;
                                Runtime.getRuntime().exec(command);
                            }
                            catch (IOException iOException) {
                                iOException.printStackTrace();
                            }
                        }
                        
                        exception.printStackTrace();
                    }
                }
            }
        });
    }
    
    private void _initializeFinally() {
        this._folderLocationTextField.setText(this._getUserHomePath());
        this._baseChooserDialog.setInitialDirectory(new File(this.GetSearchLocation()));
        this._isFindOnSubDirectoriesToggleSwitch.setSelected(true);
        this._isFindOnFilesToggleSwitch.setSelected(true);
    }
    
    private void SetupWindow() {
        if (this._stage != null) {
            this._stage.setTitle(Settings.WINDOW_TITLE);
            this._stage.setWidth(Settings.WINDOW_WIDTH);
            this._stage.setHeight(Settings.WINDOW_HEIGHT);
            this._stage.getIcons().add(new Image(this.getClass().getResourceAsStream(Settings.APP_ICON)));
        }
    }
    
    public void UpdateStatus(String text) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                _statusLabel.setText(text);
            }
        });
    }
    
    public void ClearProgress() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                _progressBar.setProgress(0);
            }
        });
    }
    
    
    public void UpdateProgress(int i, int total) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                _progressBar.setProgress((1.0 / total) * i);
            }
        });
    }
    
    public String GetSearchTerm() {
        return this._searchArea.getText().trim();
    }
    
    public String GetSearchLocation() {
        return this._folderLocationTextField.getText();
    }
    
    private void _search() {
        if (!this.GetSearchTerm().equals("")) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    _previewPane.setCenter(_noPreviewPane);
                    _progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                }
            });
            
            this._search = new Search(this, this.GetSearchTerm(), this.GetSearchLocation(), this.IsCaseSensative(), this.IsFindOnSubfolders(), this.IsFindOnFiles());
            this._search.Run();
        }
        else {
            this._alert.showAndWait();
        }
    }
    
    private void _stop() {
        if (this._search != null) {
            this._search.Stop();
        }
    }
    
    private void _clear() {
        this._searchArea.setText("");
        this._searchResultTable.getItems().clear();
        this._progressBar.setProgress(0);
        this._previewPane.setCenter(this._noPreviewPane);
    }
    
    private String _getUserHomePath() {
        String path = this._getValidatedPath(new File(System.getProperty("user.home")).getAbsolutePath());
        
        return path;
    }
    
    private void _chooseSearchFolderLocation() {
        File selectedFolder = this._baseChooserDialog.showDialog(this._stage);
        
        if (selectedFolder != null) {
            if (selectedFolder.exists() && selectedFolder.isDirectory()) {
                String path = this._getValidatedPath(selectedFolder.getAbsolutePath());
                this._folderLocationTextField.setText(path);
            }
        }
    }
    
    private String _getValidatedPath(String path) {
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        
        return path;
    }
    
    public void AddSearchResult(SearchResult searchResult) {
        this._searchResultTable.getItems().add(searchResult);
    }
    
    public void ClearTable() {
        this._searchResultTable.getItems().clear();
    }
    
    public boolean IsCaseSensative() {
        return this._isCaseSensativeToggleSwitch.isSelected();
    }
    
    public boolean IsFindOnSubfolders() {
        return this._isFindOnSubDirectoriesToggleSwitch.isSelected();
    }
    
    public boolean IsFindOnFiles() {
        return this._isFindOnFilesToggleSwitch.isSelected();
    }
    
    public static String GET_FILE_EXTENSION(String filename) {
        if (filename == null) {
            return null;
        }
        
        int start = filename.lastIndexOf(".");
        int end   = filename.length();
        
        return filename.substring(start, end);
    }
    
    public static List<String> GET_TEXT_FROM_FILE(String filePath) {
        File file = new File(filePath);
        
        List<String> output = new ArrayList<>();
        
        if (file.exists() && file.isFile() && file.canRead()) {
            BufferedReader bufferReader = null;
            FileReader     fileReader   = null;
            
            try {
                fileReader = new FileReader(filePath);
                bufferReader = new BufferedReader(fileReader);
                
                String currentLine;

                while ((currentLine = bufferReader.readLine()) != null) {
                    if (!currentLine.trim().equals("")) {
                        output.add(currentLine);
                    }
                }
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            finally {
                try {
                    if (bufferReader != null) {
                        bufferReader.close();
                    }
                    
                    if (fileReader != null) {
                        fileReader.close();
                    }
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        return output;
    }
    
    private void _onSearchResultTableRowSelected(SearchResult selectedItem) {
        if ((selectedItem != null) && (_search != null)) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    // No Preview
                    _previewPane.setCenter(_noPreviewPane);

                    // File
                    if (new File(selectedItem.path).isFile()) {
                        if (selectedItem.name.contains(".")) {
                            String extension = Program.GET_FILE_EXTENSION(selectedItem.name).toLowerCase();

                            boolean isTextFile = _search.IsTextFile(extension, false);

                            // Text file
                            if (isTextFile) {
                                // Generic text file
                                if (!extension.equals(".htm") && !extension.equals(".html") && !extension.equals(".mht") && !extension.equals(".mhtml")) {
                                    _textPreviewArea.clear();
                                    List<String> lines = Program.GET_TEXT_FROM_FILE(selectedItem.path);
                                    String text = "";

                                    for (String line: lines) {
                                        text += line + "\n";
                                    }

                                    _textPreviewArea.setText(text);
                                    _previewPane.setCenter(_textPreviewArea);
                                }
                                // Web page
                                else {
                                    if (!extension.equals(".mht") && !extension.equals(".mhtml")) {
                                        _webPageViewer.SetWebPage(selectedItem.path);
                                        _previewPane.setCenter(_webPageViewer);
                                    }
                                }
                            }
                            // Image
                            else if (extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png") || extension.equals(".gif")) {
                                _imageViewer.SetImage(selectedItem.path);
                                _previewPane.setCenter(_imageViewer);
                            }
                        }
                    }
                }
            });
        }
    }
}
