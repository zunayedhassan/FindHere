package com.appiomatic.FindHere;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Zunayed Hassan
 */
public class SearchResult {
    public static final File[]    LIST_OF_ICONS = new File("icons").listFiles();
    
    public              ImageView icon          = null;
    public              String    name          = null;
    public              String    size          = null;
    public              String    path          = null;
    public              String    created       = null;
    public              String    modified      = null;
    public              String    accessed      = null;
    
    public static enum ACTION {
        EXPLORE,
        OPEN
    }
    
    public SearchResult(File file) {
        try {
            String              fileName     = file.getName();
            String              fileSize     = "";

            if (file.isFile()) {
                fileSize = this._getFormattedFileSize(file.length());
            }

            String              filePath     = file.getAbsolutePath();
            BasicFileAttributes attributes   = Files.getFileAttributeView(Paths.get(file.getAbsolutePath()), BasicFileAttributeView.class).readAttributes();
            String              fileCreated  = this._getFormatedTimeStamp(attributes.creationTime().toString());
            String              fileModified = this._getFormatedTimeStamp(attributes.lastModifiedTime().toString());
            String              fileAccessed = this._getFormatedTimeStamp(attributes.lastAccessTime().toString());
            
            this._initialize(fileName, fileSize, filePath, fileCreated, fileModified, fileAccessed);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    
    private void _initialize(String name, String size, String path, String created, String modified, String accessed) {
        this.name      = name;
        this.size      = size;
        this.path      = path;
        this.created   = created;
        this.modified  = modified;
        this.accessed  = accessed;
        this.icon      = this._getIcon();
    }
    
    public ImageView getIcon() {
        return this.icon;
    }
    
    public void setIcon(String icon) {
        this.icon = new ImageView(new Image("file" + icon));
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSize() {
        return this.size;
    }
    
    public void setSize(String size) {
        this.size = size;
    }
    
    public String getPath() {
        return this.path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getCreated() {
        return this.created;
    }
    
    public void setCreated(String created) {
        this.created = created;
    }
    
    public String getModified() {
        return this.modified;
    }
    
    public void setModified(String modified) {
        this.modified = modified;
    }
    
    public String getAccessed() {
        return this.accessed;
    }
    
    public void setAccessed(String accessed) {
        this.accessed = accessed;
    }
    
    private String _getFormatedTimeStamp(String originalTimeStamp) {
//        String date = originalTimeStamp.split("T")[0] + " " + originalTimeStamp.split("T")[1].split(":")[0] + ":" + originalTimeStamp.split("T")[1].split(":")[1];
        
        return originalTimeStamp;
    }
    
    private String _getFormattedFileSize(long originalFileSize) {
        String originalFileSizeAsText = Long.toString(originalFileSize);
        String fileSize = originalFileSizeAsText + " byte";
        
        if (originalFileSizeAsText.length() >= 9) {
            fileSize = this._getFormattedDouble(originalFileSize / Math.pow(10, 9)) + " GB";
        }
        else if (originalFileSizeAsText.length() >= 6) {
            fileSize = this._getFormattedDouble(originalFileSize / Math.pow(10, 6)) + " MB";
        }
        else if (originalFileSizeAsText.length() >= 3) {
            fileSize = this._getFormattedDouble(originalFileSize / Math.pow(10, 3)) + " KB";
        }
        
        return fileSize;
    }
    
    private String _getFormattedDouble(double value) {
        return (new DecimalFormat("0.##").format(value));
    }
    
    private ImageView _getIcon() {
        String iconFile = "folder.png";
        File file = new File(this.path);
        
        if (file.isFile()) {
            iconFile = "file.png";
            
            try {
                if ((LIST_OF_ICONS != null) && file.getName().contains(".")) {
                    String currentFileExtension = Program.GET_FILE_EXTENSION(file.getName()).toLowerCase();
                    
                    for (File currentIconFile: LIST_OF_ICONS) {
                        if (currentIconFile.getName().endsWith(".png") && file.getName().contains(".")) {
                            String extension = "." + currentIconFile.getName().substring(0, currentIconFile.getName().length() - Program.GET_FILE_EXTENSION(currentIconFile.getName()).length());
                            
                            if (extension.equals(currentFileExtension)) {
                                iconFile = currentIconFile.getName();
                                break;
                            }
                        }
                    }
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        
        ImageView imageView = new ImageView(new Image("file:icons" + File.separator + iconFile));
        
        return imageView;
    }
}
