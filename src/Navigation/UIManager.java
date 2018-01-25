package Navigation;

import Application.AppRunner;
import Images.ImageFileHistoryEntry;
import Images.ImageFileHistoryManager;
import Images.ImageFileManager;
import Tags.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

/**
 * A class that deals with all tasks related to the User Interface.
 */

public class UIManager {

    /* Runs the application. */
    private AppRunner appRunner;
    /* Manages all directory-related operations. */
    private DirectoryManager directoryManager;
    /* Manages any events triggered by buttons. */
    private EventsManager eventsManager;
    /* Managers all listeners. */
    private ListenersManager listenersManager;

    /* All buttons used in the application. */
    private Button selectDirectoryButton, backToFolderButton, activeTagsButton, closeButton, homeButton,
            viewImageButton, addTagButton, removeTagButton, moveFileButton, viewImageHistoryButton,
            revertToHistoryEntryButton, removeTagFromAllButton, addSelectedTagButton, filterImagesButton,
            removeFilterButton, createNewTagButton, backToImageButton, masterLogButton, addThisTagButton,
            filterDirImagesButton;

    /* The field where the user inputs the tag text. */
    private TextField newTagTextField = new TextField();
    private TextField createTagTextField = new TextField();

    /* All display views. */
    private ListView<Tag> displayTags;
    private ListView<String> displayFiles;
    private ListView<String> displayHistory;
    private ListView<Tag> displayImageTags;

    private boolean dirToBeFiltered = false;
    private boolean filterOnlyDirFiles = false;

    /**
     * Constructs a new UIManager.
     *
     * @param initiatorAppRunner Runs the application.
     */
    public UIManager(AppRunner initiatorAppRunner) {
        appRunner = initiatorAppRunner;
        directoryManager = new DirectoryManager(initiatorAppRunner);
        eventsManager = new EventsManager(appRunner, directoryManager, this);
        listenersManager = new ListenersManager(appRunner, this);
        setUpButtons();
        setUpHomeDisplay();
        appRunner.getStage().show();
    }

    /**
     * Constructs all buttons.
     */
    private void setUpButtons() {
        /* For returning to the home screen. */
        homeButton = new Button("Home");
        homeButton.setOnAction(eventsManager.RETURN_HOME_EVENT_HANDLER);

        /* For selecting a directory. */
        selectDirectoryButton = new Button("Select Directory");
        selectDirectoryButton.setOnAction(eventsManager.SELECT_DIRECTORY_EVENT_HANDLER);

        /* For going back to the most recently selected folder. */
        backToFolderButton = new Button("Back to Folder");
        backToFolderButton.setOnAction(eventsManager.BACK_TO_FOLDER_EVENT_HANDLER);

        /* For the viewing of a selected image. */
        viewImageButton = new Button("Edit Image");
        viewImageButton.setOnAction(eventsManager.VIEW_IMAGE_EVENT_HANDLER);

        /* For the viewing of the image, after examining its history. */
        backToImageButton = new Button("Back to Image");
        backToImageButton.setOnAction(eventsManager.VIEW_IMAGE_EVENT_HANDLER);

        /* For the creation of a new tag, independent of any images. */
        createNewTagButton = new Button("Create Tag");
        createNewTagButton.setOnAction(eventsManager.CREATE_NEW_TAG_EVENT_HANDLER);

        /* For the creation of a new tag, applied to a single image or a set of images. */
        addTagButton = new Button("Create a Tag for Images");
        addTagButton.setOnAction(eventsManager.ADD_TAG_EVENT_HANDLER);

        /* For the creation of a new tag, written by the user and applied to a single image. */
        addThisTagButton = new Button("Add Tag to Image");
        addThisTagButton.setOnAction(eventsManager.ADD_TAG_EVENT_HANDLER);

        /* For the adding of an already-existing tag from the list of all tags, to an image. */
        addSelectedTagButton = new Button("Add Tag(s) to this Image");
        addSelectedTagButton.setOnAction(eventsManager.ADD_SELECTED_TAG_EVENT_HANDLER);

        /* For the removal of a tag that belongs to an image. */
        removeTagButton = new Button("Remove Tag(s) from Image");
        removeTagButton.setOnAction(eventsManager.REMOVE_TAG_EVENT_HANDLER);

        /* For the removal of a tag from all images possessing it. */
        removeTagFromAllButton = new Button("Remove Tag from All Images");
        removeTagFromAllButton.setOnAction(eventsManager.REMOVE_ALL_TAG_EVENT_HANDLER);
        removeTagFromAllButton.setDisable(true);

        /* For the moving of a File to another directory. */
        moveFileButton = new Button("Move File");
        moveFileButton.setOnAction(eventsManager.MOVE_FILE_EVENT_HANDLER);

        /* For the viewing of an Image's history of changes. */
        viewImageHistoryButton = new Button("View History");
        viewImageHistoryButton.setOnAction(eventsManager.VIEW_HISTORY_EVENT_HANDLER);

        /* For the reverting of an Image to some previous state. */
        revertToHistoryEntryButton = new Button();
        revertToHistoryEntryButton.setText("Revert State");
        revertToHistoryEntryButton.setOnAction(eventsManager.REVERT_HISTORY_EVENT_HANDLER);

        /* For the viewing of the Master Log. */
        masterLogButton = new Button("Master Log");
        masterLogButton.setOnAction(eventsManager.MASTER_LOG_EVENT_HANDLER);

        /* For the display of all active tags. */
        activeTagsButton = new Button("Edit Active Tags");
        activeTagsButton.setOnAction(eventsManager.DISPLAY_ALL_TAGS_EVENT_HANDLER);

        /* For the searching of Images with some tag(s). */
        filterImagesButton = new Button("Search For Images");
        filterImagesButton.setOnAction(eventsManager.SEARCH_IMAGES_EVENT_HANDLER);
        filterImagesButton.setDisable(true);

        /* For filtering images in a directory */
        filterDirImagesButton = new Button("Filter Images");
        filterDirImagesButton.setOnAction(eventsManager.FILTER_IMAGES_EVENT_HANDLER);
        filterDirImagesButton.setDisable(true);

        /* For the ending of searching Images with some tag(s). */
        removeFilterButton = new Button("Remove Filtering");
        removeFilterButton.setOnAction(eventsManager.REMOVE_FILTERING_EVENT_HANDLER);
        removeFilterButton.setDisable(true);

        /* For the closing of the application. */
        closeButton = new Button("Close Application");
        closeButton.setOnAction(eventsManager.CLOSE_EVENT_HANDLER);

        /* Start with Tag Buttons and View Image Button Disabled */
        disableTagUI();
        disableViewImageUI();
        disableImageRemoveTagUI();
    }

    /**
     * Returns a <code> TextField </code> object that can be used to create new tags.
     * This is retrieved from a <code> TextField </code>, of which the user inputs
     * the <code> Tag </code> into.
     *
     * @return the text for the new tag inputted by the user.
     */
    TextField getNewTagTextField() {
        return newTagTextField;
    }

    TextField getCreateTagTextField() {
        return createTagTextField;
    }

    /**
     * Returns a list of tags to be displayed. This method is called by EventsManager to return
     * multiple tags.
     *
     * @return All tags that have been selected by the user.
     */
    ListView<Tag> getDisplayTags() {
        return displayTags;
    }

    /**
     * Returns a list of Files to be displayed. This method is called by EventsManager to
     * get multiple Files selected by the user.
     *
     * @return All Files selected by the user.
     */
    ListView<String> getDisplayFiles() {
        return displayFiles;
    }

    ListView<Tag> getDisplayImageTags() {
        return displayImageTags;
    }

    /**
     * Enables the "View Image" button. Occurs when a single image has been chosen.
     */
    void enableViewImageUI() {
        viewImageButton.setDisable(false);
    }

    /**
     * Disables the "View Image" button. Occurs when multiple images have been chosen.
     */
    void disableViewImageUI() {
        viewImageButton.setDisable(true);
    }

    /**
     * Enables the "Remove Tag" button. Occurs when an image has been selected.
     */
    void enableImageRemoveTagUI() {
        removeTagButton.setDisable(false);
    }

    /**
     * Disables the "Remove Tag" button.
     */
    void disableImageRemoveTagUI() {
        removeTagButton.setDisable(true);
    }

    /**
     * Enables the adding of a tag to multiple images. Occurs when multiple images are selected.
     */
    void enableTagUI() {
        addTagButton.setDisable(false);
        newTagTextField.setDisable(false);
        viewImageButton.setDisable(true);
    }

    /**
     * Disables the "Add Tag" buttons. Occurs when a user is selecting a single image.
     */
    void disableTagUI() {
        addTagButton.setDisable(true);
        newTagTextField.setDisable(true);
    }

    /**
     * Enables all Tag buttons in Active Tags display.
     */
    void enableAllTagsUI() {
        removeTagFromAllButton.setDisable(false);
        filterImagesButton.setDisable(false);
        filterDirImagesButton.setDisable(false);
    }

    /**
     * Disables all Tag buttons in Active Tags display.
     */
    void disableAllTagsUI() {
        removeTagFromAllButton.setDisable(true);
        filterImagesButton.setDisable(true);
        filterDirImagesButton.setDisable(true);
    }

    void disableFilterDirUI() {
        filterDirImagesButton.setDisable(true);
    }

    /**
     * Deactivates the filter(s) on placed on the Directory.
     */
    void filterDir() {
        dirToBeFiltered = true;
        removeFilterButton.setDisable(false);
    }

    void filterOnlyDirFiles() {
        filterDir();
        filterOnlyDirFiles = true;
    }

    /**
     * Activates the filters on the Directory.
     */
    void unFilterDir() {
        dirToBeFiltered = false;
        filterOnlyDirFiles = false;
        removeFilterButton.setDisable(true);
    }

    /**
     * Displays the home scene. This method is accessed by opening the application.
     */
    void setUpHomeDisplay() {
        VBox homeLayout = new VBox();
        //set Home Title
        Label homeTitle = new Label("@memories");
        homeTitle.setFont(new Font(32));
        homeTitle.setTranslateY(-160);
        Label brehLabel = new Label("100's Only Studios");
        brehLabel.setTranslateY(-150);
        homeLayout.getChildren().addAll(homeTitle, brehLabel, masterLogButton, selectDirectoryButton, backToFolderButton,
                activeTagsButton, closeButton);
        homeLayout.setAlignment(Pos.CENTER);
        homeLayout.setSpacing(20);
        setUpScene(homeLayout);
    }

    /**
     * Displays the files available in a directory specified by the user. It is accessed
     * when the user opens up the home menu and chooses a directory.
     */
    void setUpDirectoryDisplay() {
        if (appRunner.getDir() == null) {
            return;
        }
        Label label;
        displayFiles = new ListView<>();
        disableTagUI();
        label = setUpDirectoryLabelHelper();
        setUpDirectoryObservableListHelper();
        setUpTagsListView();
        setUpDirectorySceneHelper(label);
        appRunner.saveToFiles();
    }

    /**
     * Sets up the directory up the directory or tag filter ImageFiles and label.
     *
     * @return The label containing the current directory location.
     */
    private Label setUpDirectoryLabelHelper() {
        ArrayList<File> allDirFiles;
        ImageFileManager imageFileManager = appRunner.getImageFileManager();
        String resultLabelString = "";
        Label label;

        if (dirToBeFiltered) {
            allDirFiles = imageFileManager.gatherFilesWithTags(appRunner.getSelectedTags(),
                    appRunner.getDir(), filterOnlyDirFiles);
            if (filterOnlyDirFiles) {
                resultLabelString += appRunner.getDir().getPath() +
                        " --- Filter: " + Arrays.toString(appRunner.getSelectedTags());
            } else {
                resultLabelString += "Filtering All Files For: " + Arrays.toString(appRunner.getSelectedTags());
            }
        } else {
            allDirFiles = directoryManager.getDirectoryFiles(appRunner.getDir());
            resultLabelString += appRunner.getDir().getPath();
        }

        appRunner.setDirectoryFiles(allDirFiles);
        label = new Label(resultLabelString);
        return label;
    }

    /**
     * Sets up the list of image Files in the Directory, or the image Files that have the appropriate tag(s).
     */
    private void setUpDirectoryObservableListHelper() {
        ObservableList<String> items;
        ArrayList<String> tempDirFilenames = new ArrayList<>();
        String pathName = appRunner.getDir().getPath();

        /* Sets all file names. */
        for (File dirFile : appRunner.getDirectoryFiles()) {
            if (dirFile.getParent().equals(pathName)) {
                tempDirFilenames.add(dirFile.getName());
            } else {
                String subPath = dirFile.getPath().substring(pathName.length());
                tempDirFilenames.add(subPath);
            }
        }
        appRunner.setDirFileNames(tempDirFilenames);
        items = FXCollections.observableArrayList(appRunner.getDirFileNames());
        displayFiles.setItems(items);

        /* Adds the model to allow us to retrieve the selected files. */
        displayFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        displayFiles.setOnMouseClicked(eventsManager.SELECT_MULTIPLE_FILES_EVENT_HANDLER);
    }

    /**
     * Sets up the Directory Scene.
     */
    private void setUpDirectorySceneHelper(Label label) {

        BorderPane directoryDisplay = new BorderPane();
        VBox topBox = new VBox();
        VBox leftBox = new VBox();
        VBox rightBox = new VBox();
        HBox topBoxButtons = new HBox();
        HBox topBoxLabel = new HBox();

        topBoxButtons.getChildren().addAll(homeButton, selectDirectoryButton);
        topBox.getChildren().addAll(topBoxButtons);
        leftBox.getChildren().addAll(label, displayFiles, viewImageButton, addTagButton, newTagTextField);
        rightBox.getChildren().addAll(new Label("All Active Tags"), displayTags, filterDirImagesButton, removeFilterButton, activeTagsButton);

        topBoxButtons.setSpacing(20);
        leftBox.setSpacing(10);
        rightBox.setSpacing(10);

        directoryDisplay.setTop(topBox);
        directoryDisplay.setCenter(leftBox);
        directoryDisplay.setRight(rightBox);

        setUpScene(directoryDisplay);
    }


    /**
     * Displays a single image. This allows the user to perform all tagging operations, and is accessed by
     * choosing a file from the directory display.
     * <p>
     * This code was adapted from:
     *
     * @link https://stackoverflow.com/questions/7830951/how-can-i-load-computer-directory-images-in-javafx
     * @link https://docs.oracle.com/javase/8/javafx/api/javafx/scene/image/ImageView.html
     */
    void setUpImageDisplay() {
        Label imageName;
        ArrayList<Tag> tagsList;
        displayImageTags = new ListView<>();
        ObservableList<Tag> items;
        BorderPane imageBorderPane = new BorderPane();
        HBox topHBox = new HBox();
        VBox centerVBox = new VBox();
        VBox leftVBox = new VBox();
        VBox rightVBox = new VBox();
        HBox addTagBox = new HBox();
        VBox listLayout = new VBox();
        Label tagLabel = new Label("Image Tags");
        Label allTagsLabel = new Label("All Tags");
        enableTagUI();

        /* Gets all information pertaining to the selected image. */
        Label imagePathName = new Label(appRunner.getSelectedFile().getPath());
        imageName = new Label(appRunner.getSelectedFile().getName());
        tagsList = appRunner.getImageFileManager().getImageTags(appRunner.getSelectedFile());

        /* Gets the list of all tags to be displayed. */
        items = FXCollections.observableArrayList(tagsList);
        displayImageTags.setItems(items);
        displayImageTags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        displayImageTags.setOnMouseClicked(eventsManager.SELECT_MULTIPLE_IMAGE_TAGS_EVENT_HANDLER);

        /* Creates the text field for the user to add a tag. */
        newTagTextField.setPromptText("Add a tag!");
        addTagBox.getChildren().addAll(addThisTagButton, newTagTextField);
        addTagBox.setSpacing(20);
        setUpTagsListView();

        /* Sets up the arrangement of all UI components. */
        listLayout.getChildren().addAll(displayImageTags, addThisTagButton, addTagBox, removeTagButton);
        listLayout.setSpacing(10);
        topHBox.getChildren().addAll(backToFolderButton, moveFileButton, viewImageHistoryButton);
        topHBox.setSpacing(20);
        centerVBox.getChildren().addAll(imageName, imagePathName, setUpImage());
        leftVBox.getChildren().addAll(tagLabel, listLayout);
        rightVBox.getChildren().addAll(allTagsLabel, displayTags, addSelectedTagButton, activeTagsButton);
        rightVBox.setSpacing(10);

        /* All Scene information. */
        setUpImageSceneHelper(imageBorderPane, topHBox, centerVBox, leftVBox, rightVBox);
    }

    /**
     * Sets up and returns the ImageView to be displayed in the Image Display scene.
     *
     * @return the ImageView object with all appropriate parameters attached.
     */
    private ImageView setUpImage() {
        ImageView imageView;
        Image image = new Image(appRunner.getSelectedFile().toURI().toString());
        imageView = new ImageView(image);
        imageView.setImage(image);
        imageView.setSmooth(true);
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(appRunner.getStage().widthProperty().divide(2));
        imageView.fitHeightProperty().bind(appRunner.getStage().heightProperty().divide(2));
        return imageView;
    }

    /**
     * Sets up the BorderPane for the viewing of this image.
     *
     * @param image  The BorderPane that everything is being displayed on.
     * @param top    The box containing all buttons.
     * @param center The box containing all image information and viewing.
     * @param left   The box containing the tags pertaining to this image.
     * @param right  The box containing the tags that exist globally.
     */
    private void setUpImageSceneHelper(BorderPane image, HBox top, VBox center, VBox left, VBox right) {
        image.setTop(top);
        image.setCenter(center);
        image.setLeft(left);
        image.setRight(right);
        setUpScene(image);
    }

    /**
     * Displays all history instances belonging to an image.
     */
    void setUpHistoryDisplay() {
        VBox historyLayout = new VBox();
        ListView<ImageFileHistoryEntry> displayHistory = new ListView<>();
        ObservableList<ImageFileHistoryEntry> items;
        ArrayList<ImageFileHistoryEntry> allHistory;

        /* Gets all of the history objects. */
        allHistory = appRunner.getImageFileManager().getImageHistory(appRunner.getSelectedFile());
        items = FXCollections.observableArrayList(allHistory);
        displayHistory.setItems(items);

        /* All Scene information. */
        historyLayout.getChildren().addAll(homeButton, backToImageButton, backToFolderButton, displayHistory,
                revertToHistoryEntryButton);
        displayHistory.getSelectionModel().selectedItemProperty().addListener(
                listenersManager.HISTORY_ENTRY_CHANGE_LISTENER);
        historyLayout.setSpacing(20);
        setUpScene(historyLayout);
    }

    /**
     * Displays the log of all operations performed by the user.
     */
    void setUpLogDisplay() {
        displayHistory = new ListView<>();
        VBox logLayout = new VBox();
        ObservableList<String> items;
        ArrayList<String> tempHistoryNames = new ArrayList<>();
        ImageFileHistoryManager log = appRunner.getMasterLog();

        /* Sets all file names. */
        for (ImageFileHistoryEntry entry : log.getAllHistory()) {
            tempHistoryNames.add(entry.masterString());
        }
        items = FXCollections.observableArrayList(tempHistoryNames);
        displayHistory.setItems(items);

        /* All Scene information. */
        logLayout.getChildren().addAll(homeButton, displayHistory);
        setUpScene(logLayout);
    }

    /**
     * Displays all existing tags. Accessed via the "Active Tags" button.
     */
    void setUpTagsDisplay() {
        VBox tagsLayout = new VBox();
        HBox createTagBox = new HBox();

        disableAllTagsUI();
        setUpTagsListView();

        /* Allows the user to create a tag independently. */
        createTagTextField.setPromptText("Create a tag!");
        createTagBox.getChildren().addAll(createNewTagButton, createTagTextField);
        createTagBox.setSpacing(20);

        /* All Scene information.*/
        tagsLayout.getChildren().addAll(homeButton, backToFolderButton, displayTags, createTagBox,
                removeTagFromAllButton, filterImagesButton);
        tagsLayout.setSpacing(20);
        setUpScene(tagsLayout);
    }

    /**
     * Sets up the list of tags as seen in Active Tags.
     */
    private void setUpTagsListView() {
        displayTags = new ListView<>();
        ObservableList<Tag> items;
        appRunner.setSelectedTags(new Tag[0]);

        /* Gets all tags. */
        items = FXCollections.observableArrayList(appRunner.getTagManager().getAllExistingTags());
        displayTags.setItems(items);
        displayTags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        displayTags.setOnMouseClicked(eventsManager.SELECT_MULTIPLE_TAGS_EVENT_HANDLER);
    }

    private void setUpScene(Parent layout) {
        Scene newScene = new Scene(layout, 1080, 720);
        appRunner.getStage().setScene(newScene);
    }
}
