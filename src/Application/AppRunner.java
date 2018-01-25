package Application;

import Images.*;
import Tags.*;
import Navigation.UIManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class AppRunner extends Application {

    private Stage stage;

    /* All Objects selected by the user through the application UI. */
    private File dir, moveTargetDir;
    private static File selectedFile;
    private ImageFileHistoryEntry selectedHistoryEntry;
    private Tag selectedTag;
    private Tag[] selectedTags;
    private Tag[] selectedImageTags;
    private File[] selectedFiles;

    /* Management of all data. */
    private ImageFileHistoryManager masterLog = new ImageFileHistoryManager();
    private TagManager tagManager = new TagManager();
    private ImageFileManager imageFileManager;

    /* Pertaining to the files available in a given directory. */
    private ArrayList<File> directoryFiles;
    private ArrayList<String> dirFileNames;

    /* File name for saving. */
    private String imageSaveFileName = System.getProperty("user.dir") + File.separator + "serializedImageData.ser";
    private String logSaveFileName = System.getProperty("user.dir") + File.separator + "serializedLogData.ser";

    /**
     * Instantiates all parameters and information related to this application.
     *
     * @param primaryStage The home screen for this application.
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("@memories");
        masterLog.readFromFile(logSaveFileName);
        imageFileManager = new ImageFileManager(tagManager, masterLog);
        tagManager.addObserver(imageFileManager);
        imageFileManager.readFromFile(imageSaveFileName);
        this.saveToFiles();
        new UIManager(this);
    }

    /**
     * Runs the application.
     */
    static void main() {
        launch();
    }

    /**
     * @return the Stage for display.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @return the File that has currently been selected by the user.
     */
    public File getSelectedFile() {
        return selectedFile;
    }

    /**
     * @return the directory that is currently being navigated by the user.
     */
    public File getDir() {
        return dir;
    }

    /**
     * @return the directory that the user has chosen as a destination for some File to be moved to.
     */
    public File getMoveTargetDir() {
        return moveTargetDir;
    }

    /**
     * @return the ArrayList of directory files belonging to the currently chosen directory.
     */
    public ArrayList<File> getDirectoryFiles() {
        return directoryFiles;
    }

    /**
     * @return the ArrayList of directory file names belonging to the currently chosen directory.
     */
    public ArrayList<String> getDirFileNames() {
        return dirFileNames;
    }

    /**
     * @return the Tag currently chosen by the user.
     */
    public Tag getSelectedTag() {
        return selectedTag;
    }

    /**
     * @return the Tags currently chosen by the user.
     */
    public Tag[] getSelectedTags() {
        return selectedTags;
    }

    public Tag[] getSelectedImageTags() {
        return selectedImageTags;
    }

    /**
     * @return the Files currently chosen by the user.
     */
    public File[] getSelectedFiles() {
        return selectedFiles;
    }

    /**
     * @return the ImageFileManager responsible for all ImageFile data.
     */
    public ImageFileManager getImageFileManager() {
        return imageFileManager;
    }

    /**
     * @return the TagManager responsible for all Tag data.
     */
    public TagManager getTagManager() {
        return tagManager;
    }

    /**
     * @return the ImageFileHistoryEntry chosen by the user.
     */
    public ImageFileHistoryEntry getSelectedHistoryEntry() {
        return selectedHistoryEntry;
    }

    /**
     * @return This application's ImageFileHistoryManager.
     */
    public ImageFileHistoryManager getMasterLog() {
        return masterLog;
    }

    /**
     * Reassigns the current directory.
     *
     * @param newTarget the new Directory of which the user is working with.
     */
    public void setDir(File newTarget) {
        dir = newTarget;
    }

    /**
     * Reassigns the currently chosen File.
     *
     * @param updatedFile the newly chosen File that the user has selected.
     */
    public void setSelectedFile(File updatedFile) {
        selectedFile = updatedFile;
    }

    /**
     * Reassigns the directoryFiles to another set of directory files.
     *
     * @param allDirFiles all new directory files selected by the user.
     */
    public void setDirectoryFiles(ArrayList<File> allDirFiles) {
        directoryFiles = allDirFiles;
    }

    /**
     * Reassigns the dirFileNames to another set of directory file names.
     *
     * @param allDirFileNames all new directory file names selected by the user.
     */
    public void setDirFileNames(ArrayList<String> allDirFileNames) {
        dirFileNames = allDirFileNames;
    }

    /**
     * Reassigns the selected tag to another Tag.
     *
     * @param newTag the new tag chosen by the user.
     */
    public void setSelectedTag(Tag newTag) {
        selectedTag = newTag;
    }

    /**
     * Reassigns the selected tags to another set of tags.
     *
     * @param newTags the new tags that the user has selected.
     */
    public void setSelectedTags(Tag[] newTags) {
        selectedTags = newTags;
    }

    public void setSelectedImageTags(Tag[] newTags) {
        selectedImageTags = newTags;
    }

    /**
     * Reassigns the selectedFiles to another set of Files.
     *
     * @param selectedFiles the new Files that the user has selected.
     */
    public void setSelectedFiles(File[] selectedFiles) {
        this.selectedFiles = selectedFiles;
    }

    /**
     * Reassigns the moveTargetDir to a new target.
     *
     * @param newLocation the new location that the user has chosen.
     */
    public void setMoveTargetDir(File newLocation) {
        moveTargetDir = newLocation;
    }

    /**
     * Reassigns the selectedHistoryEntry to a new entry.
     *
     * @param oldEntry the entry selected by the user.
     */
    public void setSelectedHistoryEntry(ImageFileHistoryEntry oldEntry) {
        selectedHistoryEntry = oldEntry;
    }

    /**
     * Saves the current state of the application.
     */
    public void saveToFiles() {
        imageFileManager.saveToFile(imageSaveFileName);
        masterLog.saveToFile(logSaveFileName);
    }

}
