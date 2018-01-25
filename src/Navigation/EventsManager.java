package Navigation;

import Application.AppRunner;
import Tags.*;
import Images.ImageFileManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Handles all actions performed by the user.
 */

class EventsManager {

    /* Runs the application. */
    private AppRunner appRunner;
    /* Manages all directory-related tasks. */
    private DirectoryManager directoryManager;
    /* Manages all UI-related tasks. */
    private UIManager uiManager;

    /**
     * Constructs a new EventsManager.
     *
     * @param appRunner        The application runner this is being called from.
     * @param directoryManager The directory manager handling all directory tasks.
     * @param uiManager        The UI manager handling all UI tasks.
     */
    EventsManager(AppRunner appRunner, DirectoryManager directoryManager, UIManager uiManager) {
        this.appRunner = appRunner;
        this.directoryManager = directoryManager;
        this.uiManager = uiManager;
    }

    /**
     * Prompts the user to choose a directory. This method is called from selectDirectoryButton
     * and appears when at the Home screen.
     */
    final EventHandler<ActionEvent> SELECT_DIRECTORY_EVENT_HANDLER = event -> {
        appRunner.setDir(directoryManager.chooseDirectory());
        appRunner.setSelectedFile(null);
        uiManager.unFilterDir();
        uiManager.setUpDirectoryDisplay();
    };

    /**
     * Displays all active tags. This method is called from activeTagsButton and appears at
     * the Home screen.
     */
    final EventHandler<ActionEvent> DISPLAY_ALL_TAGS_EVENT_HANDLER = event -> {
        uiManager.setUpTagsDisplay();
    };

    /**
     * Displays the most recently chosen directory. If there is none, it will prompt the user to
     * choose a new directory. This method is called by backToFolderButton, and appears when at the
     * Home screen, when viewing an ImageFile, its History, or Active Tags.
     */
    final EventHandler<ActionEvent> BACK_TO_FOLDER_EVENT_HANDLER = event -> {
        if (appRunner.getDir() == null) { // If there is no chosen directory...
            appRunner.setDir(directoryManager.chooseDirectory());
        }
        appRunner.setSelectedFile(null);
        uiManager.setUpDirectoryDisplay();
    };

    /**
     * Returns the user to the Home display. This method is called by returnHome and appears in
     * every screen.
     */
    final EventHandler<ActionEvent> RETURN_HOME_EVENT_HANDLER = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            appRunner.setSelectedTag(null);
            uiManager.setUpHomeDisplay();
        }
    };

    /**
     * Displays an image selected by the user. This method is called by viewImageButton and appears
     * during the Directory display.
     */
    final EventHandler<ActionEvent> VIEW_IMAGE_EVENT_HANDLER = event -> {
        if (appRunner.getSelectedFile() != null) {
            uiManager.setUpImageDisplay();
        }
    };

    /**
     * Adds a tag to the selected ImageFile, and to the TagManager. This method is called by
     * addTagButton and appears when viewing an ImageFile.
     */
    final EventHandler<ActionEvent> ADD_TAG_EVENT_HANDLER = event -> {

        TextField textField = uiManager.getNewTagTextField();

        /* Checks that multiple files were chosen, and that the Tag is not blank. */
        if (appRunner.getSelectedFiles().length == 0 || textField.getText().equals("")) {
            return;
        }

        TagManager tagManager = appRunner.getTagManager();
        ImageFileManager imageFileManager = appRunner.getImageFileManager();
        Tag newTag = tagManager.createTag(textField.getText());

        if (appRunner.getSelectedFile() != null) { // Treats a single image.
//            File updatedFile = imageFileManager.updateFileAdd(appRunner.getSelectedFile(), newTag);
            imageFileManager.updateFileAdd(appRunner.getSelectedFile(), newTag);
            File updatedFile = imageFileManager.getCurrentFile();
            appRunner.setSelectedFile(updatedFile);
            uiManager.disableImageRemoveTagUI();
        } else { // Treats multiple images.
            for (File file : appRunner.getSelectedFiles()) {
                imageFileManager.updateFileAdd(file, newTag);
            }
        }
        textField.clear();
        appRunner.saveToFiles();

        if (appRunner.getSelectedFile() != null) {
            uiManager.setUpImageDisplay();
        } else {
            uiManager.setUpDirectoryDisplay();
        }
    };

    /**
     * Adds tag(s) currently selected by the user from the list of all tags to the
     * to the currently selected ImageFile.
     */
    final EventHandler<ActionEvent> ADD_SELECTED_TAG_EVENT_HANDLER = event -> {
        File selectedFile = appRunner.getSelectedFile();
        Tag[] selectedTags = appRunner.getSelectedTags();
        ImageFileManager imageFileManager = appRunner.getImageFileManager();

        if (selectedFile != null && selectedTags.length != 0) {
            for (Tag tag : selectedTags) {
                imageFileManager.updateFileAdd(selectedFile, tag);
                selectedFile = imageFileManager.getCurrentFile();
            }
            appRunner.setSelectedFile(selectedFile);
        }
        appRunner.saveToFiles();
        uiManager.setUpImageDisplay();
    };

    /**
     * Adds a tag to the list of all tags; is accessed from the Active Tags window.
     */
    final EventHandler<ActionEvent> CREATE_NEW_TAG_EVENT_HANDLER = event -> {
        TextField textField = uiManager.getCreateTagTextField();

        if (textField.getText().length() < 1) {
            return;
        }

        TagManager tagManager = appRunner.getTagManager();
        Tag newTag = tagManager.createTag(textField.getText());
        tagManager.addTag(newTag);
        textField.clear();
        appRunner.saveToFiles();
        uiManager.setUpTagsDisplay();
    };

    /**
     * Removes a tag from the selected ImageFile. Appears when viewing an ImageFile.
     */
    final EventHandler<ActionEvent> REMOVE_TAG_EVENT_HANDLER = event -> {
        ImageFileManager imageFileManager = appRunner.getImageFileManager();
        Tag[] tags = appRunner.getSelectedImageTags();
        File selectedFile = appRunner.getSelectedFile();

        if (appRunner.getSelectedFile() != null) {
            for (Tag tag : tags) {
//                selectedFile = imageFileManager.updateFileRemove(selectedFile, tag);
                imageFileManager.updateFileRemove(selectedFile, tag);
                selectedFile = imageFileManager.getCurrentFile();
            }
            appRunner.setSelectedFile(selectedFile);
            appRunner.saveToFiles();
        }
        uiManager.disableImageRemoveTagUI();
        uiManager.setUpImageDisplay();
    };

    /**
     * Moves the File to a new directory. Appears when viewing an ImageFile.
     */
    final EventHandler<ActionEvent> MOVE_FILE_EVENT_HANDLER = event -> {
        File newDir = directoryManager.chooseDirectory();
        if (newDir != null) {
            appRunner.setMoveTargetDir(newDir);
            directoryManager.moveSomeFile();
            appRunner.saveToFiles();
            uiManager.setUpDirectoryDisplay();
        }
    };

    /**
     * Shows the history of the selected File. Appears when viewing an ImageFile.
     */
    final EventHandler<ActionEvent> VIEW_HISTORY_EVENT_HANDLER = event -> {
        uiManager.setUpHistoryDisplay();
    };

    /**
     * Reverts an ImageFile's state to the selected entry. Apepars when viewing an
     * ImageFile's history.
     */
    final EventHandler<ActionEvent> REVERT_HISTORY_EVENT_HANDLER = event -> {
        if (appRunner.getSelectedHistoryEntry() != null) {
//            File tempFile = appRunner.getImageFileManager().revertState(appRunner.getSelectedFile(), appRunner.getSelectedHistoryEntry());
            appRunner.getImageFileManager().revertState(appRunner.getSelectedFile(), appRunner.getSelectedHistoryEntry());
            File tempFile = appRunner.getImageFileManager().getCurrentFile();
            appRunner.setSelectedFile(tempFile);
        }
        appRunner.saveToFiles();
        uiManager.setUpHistoryDisplay();
    };

    final EventHandler<ActionEvent> MASTER_LOG_EVENT_HANDLER = event -> {
        uiManager.setUpLogDisplay();
    };

    /**
     * Removes the selected tag from all ImageFiles. Called from the Active Tags screen,
     * and called by the RemoveTagFromAll button.
     */
    final EventHandler<ActionEvent> REMOVE_ALL_TAG_EVENT_HANDLER = event -> {
        Tag[] selectedTags = appRunner.getSelectedTags();
        for (Tag tag : selectedTags) {
            appRunner.getTagManager().deleteTag(tag);
            appRunner.saveToFiles();
            uiManager.setUpTagsDisplay();
        }
    };

    /**
     * Allows the user to select multiple tags and adjusts the display accordingly.
     */
    final EventHandler<MouseEvent> SELECT_MULTIPLE_TAGS_EVENT_HANDLER = event -> {
        ListView<Tag> displayTags = uiManager.getDisplayTags();
        Tag[] selectedTags = displayTags.getSelectionModel().getSelectedItems().toArray(new Tag[0]);
        appRunner.setSelectedTags(selectedTags);

        if (selectedTags.length != 0) {
            uiManager.enableAllTagsUI();
        } else {
            uiManager.disableAllTagsUI();
        }
    };

    /**
     * Allows the user to select and operate upon multiple files.
     */
    final EventHandler<MouseEvent> SELECT_MULTIPLE_FILES_EVENT_HANDLER = event -> {
        ListView<String> displayFiles = uiManager.getDisplayFiles();
        String[] selectedFileNames = displayFiles.getSelectionModel().getSelectedItems().toArray(new String[0]);
        String selectedFileName = displayFiles.getSelectionModel().getSelectedItem();

        if (selectedFileNames.length > 0) {
            ArrayList<File> tempList = new ArrayList<>();
            for (String fileName : selectedFileNames) {
                File selectedDirFile = directoryManager.findAssociatedDirFile(fileName,
                        appRunner.getDirFileNames(), appRunner.getDirectoryFiles());
                tempList.add(selectedDirFile);
            }
            File[] selectedDirFiles = tempList.toArray(new File[0]);
            appRunner.setSelectedFiles(selectedDirFiles);

            if (selectedFileNames.length == 1) {
                File selectedDirFile = directoryManager.findAssociatedDirFile(selectedFileName,
                        appRunner.getDirFileNames(), appRunner.getDirectoryFiles());
                appRunner.setSelectedFile(selectedDirFile);
                uiManager.disableTagUI();
                uiManager.enableViewImageUI();
            } else if (selectedDirFiles.length != 0) {
                appRunner.setSelectedFile(null);
                uiManager.enableTagUI();
                uiManager.disableViewImageUI();
            } else {
                uiManager.disableTagUI();
                uiManager.disableViewImageUI();
            }
        }
    };

    /**
     * Filters the directory for images containing tag(s) selected by the user.
     */
    final EventHandler<ActionEvent> SEARCH_IMAGES_EVENT_HANDLER = event -> {
        uiManager.filterDir();
        filterHelper();
    };

    final EventHandler<ActionEvent> FILTER_IMAGES_EVENT_HANDLER = event -> {
        uiManager.filterOnlyDirFiles();
        uiManager.disableFilterDirUI();
        filterHelper();
    };

    /**
     * Un-filters the directory, to display all images again.
     */
    final EventHandler<ActionEvent> REMOVE_FILTERING_EVENT_HANDLER = event -> {
        uiManager.unFilterDir();
        uiManager.disableFilterDirUI();
        uiManager.setUpDirectoryDisplay();
    };

    /**
     * Closes the application.
     */
    final EventHandler<ActionEvent> CLOSE_EVENT_HANDLER = event -> {
        appRunner.getStage().close();
    };

    final EventHandler<MouseEvent> SELECT_MULTIPLE_IMAGE_TAGS_EVENT_HANDLER = event -> {
        ListView<Tag> displayImageTags = uiManager.getDisplayImageTags();
        Tag[] selectedTags = displayImageTags.getSelectionModel().getSelectedItems().toArray(new Tag[0]);
        appRunner.setSelectedImageTags(selectedTags);

        if (selectedTags.length != 0) {
            uiManager.enableImageRemoveTagUI();
        } else {
            uiManager.disableAllTagsUI();
        }
    };

    private void filterHelper() {
        if (appRunner.getDir() == null) {
            appRunner.setDir(directoryManager.chooseDirectory());
        }
        uiManager.setUpDirectoryDisplay();
    }
}
