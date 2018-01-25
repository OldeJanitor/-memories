package Images;

import Tags.Tag;
import Tags.*;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

/**
 * A class representing a single ImageFile.
 */
class ImageFile extends Observable implements Serializable {

    /* This ImageFile's original name, without any tags or a file extension. */
    private String originalName;
    /* This ImageFile's name, as seen it is seen by the user. Includes all tags, and file extension. */
    private String displayName;
    /* This ImageFile's file extension. */
    private String fileExt;
    /* The current directory path to the location of this ImageFile. */
    private File currentDir;
    /* An ArrayList of all Tags currently attached to this ImageFile. */
    private ArrayList<Tag> tags;
    /* An ArrayList of all alterations made to this ImageFile's names or tags. */
    private ArrayList<ImageFileHistoryEntry> nameHistory;
    /* The HistoryManager managing all history entries. */
    private ImageFileHistoryManager historyManager;

    /**
     * Constructs a new ImageFile with its displayName, its currentDir, and a TagManager.
     */
    ImageFile(File selectedFile, TagManager observerTagManager, ImageFileHistoryManager historyManager) {
        this.addObserver(observerTagManager);
        this.currentDir = selectedFile;
        this.originalName = this.buildOriginalString(selectedFile.getName());
        this.tagConstructor(this.buildTagList(selectedFile.getName()));
        this.fileExt = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."),
                selectedFile.getName().length());
        this.displayName = selectedFile.getName();
        this.historyManager = historyManager;
        this.nameHistory = new ArrayList<>();
        this.addNameToHistory(this.originalName + this.fileExt, this.displayName);
    }

    ArrayList<Tag> getTags() {
        return this.tags;
    }

    ArrayList<ImageFileHistoryEntry> getNameHistory() {
        return this.nameHistory;
    }

    File getCurrentDir() {
        return this.currentDir;
    }

    /**
     * Sets this ImageFile to a new Directory.
     *
     * @param newDir: The new location/name for this ImageFile.
     */
    void setFileDirectory(File newDir) {
        boolean isSuccess = this.currentDir.renameTo(newDir);
        if (isSuccess) {
            this.currentDir = newDir;
        }
    }

    /**
     * Adds a tag to this ImageFile. Updates its displayName and nameHistory accordingly
     * and notifies the TagManager.
     *
     * @param newTag: The new Tag added to this ImageFile.
     */
    void addTag(Tag newTag) {
        if (!this.tags.contains(newTag)) {
            this.tags.add(newTag);
            this.updateImageFile();
            this.setChanged();
            this.notifyObservers(newTag);
        }
    }

    /**
     * Removes a tag from this ImageFile. Updates its displayName, nameHistory, and File directory accordingly.
     *
     * @param oldTag: The tag to be removed from this ImageFile.
     */
    void removeTag(Tag oldTag) {
        if (this.tags.contains(oldTag)) {
            this.tags.remove(oldTag);
            this.updateImageFile();
        }
    }

    /**
     * Returns true iff this ImageFile contains all of the tags that are being filtered for.
     *
     * @param tags Tag(s) that the user is searching for.
     * @return Whether or not this ImageFile has these tags.
     */
    boolean hasAllTags(Tag[] tags) {
        for (Tag tag : tags) {
            if (!this.tags.contains(tag)) {
                return false;
            }
        } return true;
    }

    /**
     * Resets this ImageFile to re-update its observer.
     *
     * @param observerTagManager The observer being updated.
     */
    void hardUpdate(TagManager observerTagManager, ImageFileHistoryManager masterLog) {
        this.addObserver(observerTagManager);
        this.tagConstructor(this.tags);
        this.historyManager = masterLog;
    }

    /**
     * Reverts this ImageFile back to some pre-existing state.
     *
     * @param historyEntry: Some previous state within this ImageFile.nameHistory.
     */
    void revertState(ImageFileHistoryEntry historyEntry) {
        String parse = historyEntry.getDisplayName();
        this.originalName = this.buildOriginalString(parse);
        this.tagConstructor(this.buildTagList(parse));
        this.updateImageFile();
    }

    /**
     * Updates the nameHistory of this ImageFile as it pertains to any changes made.
     *
     * @param newDisplayName: The updated name of the ImageFile.
     */
    private void addNameToHistory(String oldName, String newDisplayName) {
        ImageFileHistoryEntry newEntry = new ImageFileHistoryEntry(oldName, newDisplayName);
        this.nameHistory.add(newEntry);
        this.historyManager.addEntry(newEntry);
    }

    /**
     * Helper that fulfills the operations of adding/removing a tag for this ImageFile.
     */
    private void updateImageFile() {
        String oldName = this.displayName;
        this.displayName = this.buildDisplayString(this.originalName, this.tags, this.fileExt);
        File checkParent = this.currentDir.getParentFile();
        if (checkParent.isDirectory()) {
            File testFile = new File(checkParent.getPath() + File.separator + this.displayName);
            this.setFileDirectory(testFile);
        }
        this.addNameToHistory(oldName, this.displayName);
    }

    /**
     * Helper that creates an updated displayName for this ImageFile.
     *
     * @return String representing this ImageFile's new name.
     */
    private String buildDisplayString(String givenName, ArrayList<Tag> tagList, String fileExt) {
        StringBuilder allTags = new StringBuilder();
        for (Tag tag : tagList) {
            allTags.append(" ");
            allTags.append(tag.toString());
        }
        return givenName + allTags.toString() + fileExt;
    }

    /**
     * Helper for constructing an ImageFile with pre-existing tags.
     * Operates on the following file format: "FILENAME @TAG1 @TAG2.jpg"
     * Presupposes a space before any tags.
     *
     * @param givenName: The whole name given to this ImageFile.
     * @return The original name of this ImageFile.
     */
    private String buildOriginalString(String givenName) {
        if (givenName.contains("@")) {
            return givenName.substring(0, givenName.indexOf("@") - 1);
        }
        return givenName.substring(0, givenName.lastIndexOf("."));
    }

    /**
     * Helper for constructing an ImageFile with pre-existing tags.
     * Creates an ArrayList<Tag> from the File's String.
     *
     * @param fullFileName: The entire FileName for this ImageFile.
     * @return All tags found in this name.
     */
    private ArrayList<Tag> buildTagList(String fullFileName) {
        if (fullFileName.contains("@")) {
            ArrayList<Tag> preTags = new ArrayList<Tag>();
            String onlyFileName = fullFileName.substring(fullFileName.indexOf("@"),
                    fullFileName.lastIndexOf("."));

            for (int i = 0; i < onlyFileName.length(); i++) {
                if (onlyFileName.charAt(i) == '@') {

                    int nextIndex = onlyFileName.indexOf("@", i + 1);
                    if (nextIndex != -1) {
                        String newTag = onlyFileName.substring(i + 1, nextIndex - 1);
                        preTags.add(new Tag(newTag));
                    } else {
                        String newTag = onlyFileName.substring(i + 1, onlyFileName.length());
                        preTags.add(new Tag(newTag));
                    }
                }
            }
            return preTags;
        } else {
            return new ArrayList<Tag>();
        }
    }

    /**
     * Helper for constructing an ImageFile with pre-existing tags.
     *
     * @param preExistingTags: ***
     */
    private void tagConstructor(ArrayList<Tag> preExistingTags) {
        this.tags = new ArrayList<Tag>();
        if (!preExistingTags.isEmpty()) {
            for (Tag tag : preExistingTags) {
                this.tags.add(tag);
                this.setChanged();
                this.notifyObservers(tag);
            }
        }
    }
}
