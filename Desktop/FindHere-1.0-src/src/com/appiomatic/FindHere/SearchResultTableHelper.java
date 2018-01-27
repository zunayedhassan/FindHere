package com.appiomatic.FindHere;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

/**
 *
 * @author Zunayed Hassan
 */
public class SearchResultTableHelper {
    // Icon
    public static TableColumn<SearchResult, ImageView> GET_ICON_COLUMN() {
        TableColumn<SearchResult, ImageView> column = new TableColumn<>("");
        PropertyValueFactory<SearchResult, ImageView> cellValueFactory = new PropertyValueFactory<>("icon");
        column.setCellValueFactory(cellValueFactory);
        
        column.setPrefWidth(24);
        
        return column;
    }
    
    // Name
    public static TableColumn<SearchResult, String> GET_NAME_COLUMN() {
        TableColumn<SearchResult, String> column = new TableColumn<>("Name");
        PropertyValueFactory<SearchResult, String> cellValueFactory = new PropertyValueFactory<>("name");
        column.setCellValueFactory(cellValueFactory);
        
        return column;
    }
    
    // Size
    public static TableColumn<SearchResult, String> GET_SIZE_COLUMN() {
        TableColumn<SearchResult, String> column = new TableColumn<>("Size");
        PropertyValueFactory<SearchResult, String> cellValueFactory = new PropertyValueFactory<>("size");
        column.setCellValueFactory(cellValueFactory);
        
        return column;
    }
    
    // Path
    public static TableColumn<SearchResult, String> GET_PATH_COLUMN() {
        TableColumn<SearchResult, String> column = new TableColumn<>("Path");
        PropertyValueFactory<SearchResult, String> cellValueFactory = new PropertyValueFactory<>("path");
        column.setCellValueFactory(cellValueFactory);
        column.setPrefWidth(160);
        
        return column;
    }
    
    // Created
    public static TableColumn<SearchResult, String> GET_CREATED_COLUMN() {
        TableColumn<SearchResult, String> column = new TableColumn<>("Date Created");
        PropertyValueFactory<SearchResult, String> cellValueFactory = new PropertyValueFactory<>("created");
        column.setCellValueFactory(cellValueFactory);
        
        return column;
    }
    
    // Modified
    public static TableColumn<SearchResult, String> GET_MODIFIED_COLUMN() {
        TableColumn<SearchResult, String> column = new TableColumn<>("Date Modified");
        PropertyValueFactory<SearchResult, String> cellValueFactory = new PropertyValueFactory<>("modified");
        column.setCellValueFactory(cellValueFactory);
        
        return column;
    }
    
    // Accessed
    public static TableColumn<SearchResult, String> GET_ACCESSED_COLUMN() {
        TableColumn<SearchResult, String> column = new TableColumn<>("Date Accessed");
        PropertyValueFactory<SearchResult, String> cellValueFactory = new PropertyValueFactory<>("accessed");
        column.setCellValueFactory(cellValueFactory);
        
        return column;
    }
}
