package com.appiomatic.FindHere;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.concurrent.Task;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

/**
 *
 * @author Zunayed Hassan
 */
public class Search {
    private     ArrayList<String> _fileList             = new ArrayList<>();
    private     Program           _program              = null;
    private     boolean           _isStop               = false;
    private     String            _searchTerm           = null;
    private     String            _searchLocation       = null;
    private     boolean           _isCaseSensative      = false;
    private     boolean           _isFindOnSubfolders   = true;
    private     boolean           _isFindOnFiles        = true;
    
    protected   int               result                = 0;
    
    public Search(Program program, String searchTerm, String searchLocation, boolean isCaseSensative, boolean isFindOnSubfolders, boolean isFindOnFiles) {
        this._program               = program;
        this._isCaseSensative       = isCaseSensative;
        this._searchTerm            = searchTerm;
        this._isFindOnSubfolders    = isFindOnSubfolders;
        this._isFindOnFiles         = isFindOnFiles;
        
        if (!this._isCaseSensative) {
            this._searchTerm = this._searchTerm.toLowerCase();
        }
        
        this._searchLocation  = searchLocation;
    }
    
    public void Run() {
        this._isStop = false;
        Thread thread = new Thread(this._task);
        thread.setDaemon(true);
        thread.start();
    }
    
    private Task _task = new Task() {
        @Override
        protected Object call() {
            if ((_searchTerm != null) && (_searchLocation != null)) {
                if (_program != null) {
                    _program.ClearTable();
                    _program.ClearProgress();
                    _fileList.clear();
                }
                
                _readFileList(_searchLocation);

                if (_program != null) {
                    if (_fileList.size() > 0) {
                        for (int i = 0; i < _fileList.size(); i++) {
                            if (!_isStop && (new File(_fileList.get(i)).canRead())) {
                                String fileName = new File(_fileList.get(i)).getName();
                            
                                _program.UpdateProgress(i, _fileList.size());
                                _program.UpdateStatus("Matches Found: " + result + " | Now searching at: " + fileName);
                                
                                boolean isFound = false;
                                
                                if (new File(_fileList.get(i)).isFile() && fileName.contains(".")) {
                                    String extension = Program.GET_FILE_EXTENSION(new File(_fileList.get(i)).getName()).toLowerCase();
                                    
                                    boolean isTextFile = IsTextFile(extension);
         
                                    // If text related file
                                    if (isTextFile)  {
                                        List<String> lines = Program.GET_TEXT_FROM_FILE(_fileList.get(i));
                                        isFound = _isFoundAfterSearching(lines);
                                    }
                                    // pdf
                                    else if (extension.equals(".pdf") && _program.OtherFileTypeOptions[0].get()) {
                                        ArrayList<String> pages = GetTextFromPDF(_fileList.get(i));                                        
                                        
                                        for (String page: pages) {
                                            if (!_isCaseSensative) {
                                                page = page.toLowerCase();
                                            }
                                            
                                            if (page.contains(_searchTerm)) {
                                                isFound = true;
                                                
                                                break;
                                            }
                                        }
                                    }
                                    // MS Word Document (XML)
                                    else if (extension.equals(".docx") && _program.MsOfficeFileTypeOptions[0].get()) {
                                        List<String> lines = GetTextFromMSWord(_fileList.get(i));
                                        isFound = _isFoundAfterSearching(lines);
                                    }
                                    // MS Word Document (ISO)
                                    else if (extension.equals(".doc") && _program.MsOfficeFileTypeOptions[0].get()) {
                                        String text = GetTextFromMSWordOld(_fileList.get(i));
                                        
                                        if (!_isCaseSensative) {
                                            text = text.toLowerCase();
                                        }
                                        
                                        if (text.contains(_searchTerm)) {
                                            isFound = true;
                                        }
                                    }
                                    // MS Excel (XML)
                                    else if (extension.equals(".xlsx") && _program.MsOfficeFileTypeOptions[1].get()) {
                                        String text = GetTextFromMSExcel(_fileList.get(i));
                                        
                                        if (!_isCaseSensative) {
                                            text = text.toLowerCase();
                                        }
                                        
                                        if (text.contains(_searchTerm)) {
                                            isFound = true;
                                        }
                                    }
                                    // MS Excel (Old format, ISO)
                                    else if (extension.equals(".xls") && _program.MsOfficeFileTypeOptions[1].get()) {
                                        String text = GetTextFromMSExcelOld(_fileList.get(i));
                                        
                                        if (!_isCaseSensative) {
                                            text = text.toLowerCase();
                                        }
                                        
                                        if (text.contains(_searchTerm)) {
                                            isFound = true;
                                        }
                                    }
                                    // MS Power Point (XML)
                                    else if (extension.equals(".pptx") && _program.MsOfficeFileTypeOptions[2].get()) {
                                        String text = GetTextFromMSPowerPoint(_fileList.get(i));
                                        
                                        if (!_isCaseSensative) {
                                            text = text.toLowerCase();
                                        }
                                        
                                        if (text.contains(_searchTerm)) {
                                            isFound = true;
                                        }
                                    }
                                }
                                
                                if (!isFound) {
                                    // Checking on the file name
                                    if (!_isCaseSensative) {
                                        fileName = fileName.toLowerCase();
                                    }

                                    if (fileName.contains(_searchTerm)) {
                                        isFound = true;
                                    }
                                }
                                
                                if (isFound) {
                                    _program.UpdateStatus("Matches Found: " + (++result));
                                    _program.AddSearchResult(new SearchResult(new File(_fileList.get(i))));
                                }

                                updateProgress(i, _fileList.size());
                            }
                            else {
                                break;
                            }
                        }
                        
                        if (result == 0) {
                            _program.UpdateStatus("Nothing Found | Finished Searching");
                        }
                        else {
                            _program.UpdateStatus("Matches Found: " + result + " | Finished Searching");
                        }
                    }
                }
                
                _searchTerm = null;
            }
            
            return null;
        }
    };
    
    private void _readFileList(String path) {
        if (!this._isStop) {
            File baseFolder = new File(path);
            
            if (baseFolder.isDirectory() && baseFolder.canRead()) {
                try {
                    File[] listOfContents = baseFolder.listFiles();
                    
                    if (listOfContents != null) {
                        for (File file: listOfContents) {                    
                            if (file.canRead()) {
                                if (file.isDirectory() || (this._isFindOnFiles && file.isFile())) {
                                    this._fileList.add(file.getAbsolutePath());
                                    
                                    String status = "Making list of files: " + this._fileList.size();
                                    this._program.UpdateStatus(status);
                                }

                                if (file.isDirectory() && this._isFindOnSubfolders) {
                                    this._readFileList(file.getAbsolutePath());
                                }
                            }
                        }
                    }
                }
                catch (NullPointerException nullPointerException) {
                    nullPointerException.printStackTrace();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                finally {
                    return;
                }
            }
        }
    }
    
    public void Stop() {
        this._isStop     = true;
        this._searchTerm = null;    
        
        if (this._program != null) {
            this._program.UpdateStatus("Searching Halted");
            this._program.ClearProgress();
        }
    }
    
    public boolean IsTextFile(String extension, boolean isConsiderFilterOption) {
        boolean isFound = false;
        extension = extension.toLowerCase().substring(1, extension.length());
        
        for (int i = 0; i < Program.TEXT_FILE_TYPES.size(); i++) {  
            String currentTextFileExtension = this._program.TextRelatedFileTypeTitles[i];
            
            if (currentTextFileExtension.equals(extension)) {
                if (!isConsiderFilterOption) {
                    isFound = true;
                }
                else {
                    isFound = this._program.TextRelatedFileTypeOptions[i].get();
                }
                
                break;
            }
        }
        
        return isFound;
    }
    
    public boolean IsTextFile(String extension) {
        return IsTextFile(extension, true);
    }
    
    private boolean _isFoundAfterSearching(List<String> lines) {
        boolean isFound = false;
        
        if (lines != null) {
            String[] searchTerms = null;

            if (_searchTerm.contains("\n")) {
                searchTerms = _searchTerm.split("\n");
            }
            else {
                searchTerms = new String[] { _searchTerm };
            }

            int searchTermIndex = 0;

            for (String line: lines) {
                if (!_isCaseSensative) {
                    line = line.toLowerCase();
                }

                if (line.contains(searchTerms[searchTermIndex].trim())) {
                    if (searchTermIndex == 0) {
                        isFound = true;
                    }

                    isFound = isFound && true;

                    if (searchTerms.length == ++searchTermIndex) {
                        break;
                    }
                }
            }
        }
        
        return isFound;
    }
    
    public ArrayList<String> GetTextFromPDF(String filePath) {
        PDFTextStripper   pdfStripper = null;
        PDDocument        pdDoc       = null;
        COSDocument       cosDoc      = null;
        File              file        = new File(filePath);
        ArrayList<String> pages       = new ArrayList<>();
        
        try {
            PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
            parser.parse();
            cosDoc      = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc       = new PDDocument(cosDoc);
            
            for (int i = 1; i <= pdDoc.getNumberOfPages(); i++) {
                pdfStripper.setStartPage(i);
                pdfStripper.setEndPage(i);
                pages.add(pdfStripper.getText(pdDoc));
            }
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
        
        return pages;
    }
    
    public ArrayList<String> GetTextFromMSWord(String fileName) {
        ArrayList<String> output = new ArrayList<>();
        
        try {
            FileInputStream in = new FileInputStream( new File(fileName));
            XWPFDocument document = new XWPFDocument(in);

            List<XWPFParagraph> paragraphs = document.getParagraphs();

            for (XWPFParagraph paragraph: paragraphs) {
                output.add(paragraph.getText());
            }
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        catch (EncryptedDocumentException encryptedDocumentException) {
            encryptedDocumentException.printStackTrace();
        }
        catch (OLE2NotOfficeXmlFileException ole2NotOfficeXmlFileException) {
            return (new ArrayList<String>());
        }
        catch (Exception exception) {
            return (new ArrayList<String>());
        }
        
        return output;
    }
    
    public String GetTextFromMSWordOld(String fileName) {
        String text = "";
        
        try {
            FileInputStream fis = new FileInputStream(new File(fileName));
            HWPFDocument doc = new HWPFDocument(fis);
            WordExtractor extractor = new WordExtractor(doc);
            text = extractor.getText();
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        catch (Exception exception) {
            return "";
        }
        
        return text;
    }
    
    public String GetTextFromMSExcel(String fileName) {
        String text = "";
        
        try {
            InputStream ExcelFileToRead = new FileInputStream(fileName);
            XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);

            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                XSSFSheet sheet = wb.getSheetAt(i);
                XSSFRow row; 
                XSSFCell cell;

                Iterator rows = sheet.rowIterator();

                while (rows.hasNext()) {
                    row = (XSSFRow) rows.next();
                    Iterator cells = row.cellIterator();

                    while (cells.hasNext()) {
                        cell = (XSSFCell) cells.next();

                        if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                            text += (cell.getStringCellValue() + " ");
                        }
                        else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                            text += (cell.getNumericCellValue() + " ");
                        }
                        else if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
                            text += (cell.getBooleanCellValue() + " ");
                        }
                        else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
                            text += " ";
                        }
                    }

                    text += "\n";
                }
            }
        }
        catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
        catch (Exception exception) {
            return "";
        }
        
        return text;
    }
    
    public String GetTextFromMSExcelOld(String fileName) {
        String text = "";
        
        try {
            InputStream ExcelFileToRead = new FileInputStream(fileName);
            HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);

            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                HSSFSheet sheet = wb.getSheetAt(i);
                HSSFRow row;
                HSSFCell cell;

                Iterator rows = sheet.rowIterator();

                while (rows.hasNext()) {
                    row = (HSSFRow) rows.next();
                    Iterator cells = row.cellIterator();

                    while (cells.hasNext()) {
                        cell = (HSSFCell) cells.next();
                        
                        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            text += cell.getStringCellValue() + " ";
                        }
                        else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            text += cell.getNumericCellValue() + " ";
                        }
                        else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
                            text += cell.getBooleanCellValue() + " ";
                        }
                        else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
                            text += " ";
                        }
                    }

                    text += "\n";
                }
            }
        }
        catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
        catch (Exception exception) {
            return "";
        }
        
        return text;
    }
    
    public String GetTextFromMSPowerPoint(String filePath) {
        String text = "";
        
        try {
            XMLSlideShow pptx = new XMLSlideShow(new FileInputStream(filePath));
        
            List<XSLFSlide> slides = pptx.getSlides();

            for (XSLFSlide slide: slides) {
                for (XSLFTextShape textBox: slide.getPlaceholders()) {
                    text += textBox.getText() + "\n";
                }
                
                text += "\n";
            }
        }
        catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
        catch (Exception exception) {
            return "";
        }
        
        return text;
    }
}
