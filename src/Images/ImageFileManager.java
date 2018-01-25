package Images;

import Application.SaveFile;
import Application.SaveOperation;
import Tags.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.io.File;
import java.io.Serializable;


/**
 * A class that deals with any tasks involving an ImageFile.
 */

public class ImageFileManager implements Observer, Serializable, SaveFile {

    /* A list of any files that have been treated by our program. */
    private ArrayList<ImageFile> imageFiles;
    /* The TagManager keeping record of all tags. */
    private TagManager tagManager;
    /* Used to save and store all data pertaining to this ImageFileManager. */
    private SaveOperation<ImageFile> ifmSave;
    /* The Manager keeping track of all tagging. */
    private ImageFileHistoryManager masterLog;
    /* The file that the user has currently selected. */
    private File currentFile;

    /**
     * Constructs an ImageFileManager.
     */
    public ImageFileManager(TagManager tagManager, ImageFileHistoryManager masterLog) {
        this.imageFiles = new ArrayList<>();
        this.tagManager = tagManager;
        this.ifmSave = new SaveOperation<>(this.imageFiles);
        this.masterLog = masterLog;
        this.currentFile = null;
    }

    public File getCurrentFile() {
        return this.currentFile;
    }

    /**
     * @return An ArrayList of all ImageFiles.
     */
    public ArrayList<ImageFile> getImageFiles(){
        return this.imageFiles;
    }

    /**
     * Gets all tags belonging to an ImageFile.
     *
     * @param selectedFile: The file being queried.
     * @return All tags belonging to this file.
     */
    public ArrayList<Tag> getImageTags(File selectedFile) {
        ImageFile testImage = this.findImage(selectedFile);
        if (testImage != null) {
            return testImage.getTags();
        } return new ArrayList<>();
    }

    /**
     * Returns a list of all previous changes made to this File.
     *
     * @param selectedFile: The file of which is being queried.
     * @return All previous changes made.
     */
    public ArrayList<ImageFileHistoryEntry> getImageHistory(File selectedFile) {
        ImageFile test = this.findImage(selectedFile);
        if (test != null) {
            return test.getNameHistory();
        } return new ArrayList<>();
    }

    /**
     * Updates an ImageFile with a new tag. Checks whether it exists already–if not,
     * creates a new one and adds it.
     *
     * @param selectedFile: The file that is being updated.
     * @param someTag: Tag to be added to the file.
     */
    public void updateFileAdd(File selectedFile, Tag someTag) {

        /* Create a temporary ImageFile to check for its existence. */
        ImageFile testImage = this.findImage(selectedFile);

        /* If it exists, we update and set currentFile to this reference. */
        if (testImage != null) {
            testImage.addTag(someTag);
            this.currentFile = testImage.getCurrentDir();
        }

        /* If not, we create a new one, add it to imageFiles, and set it. */
        else {
            ImageFile newImage = new ImageFile(selectedFile, this.tagManager, this.masterLog);
            newImage.addTag(someTag);
            this.imageFiles.add(newImage);
            this.currentFile = newImage.getCurrentDir();
        }
    }

    /**
     * Checks whether an ImageFile already exists; updates its tags accordingly.
     *
     * @param selectedFile: The file that is being updated.
     * @param someTag: Tag of which is to be added to the file.
     */
    public void updateFileRemove(File selectedFile, Tag someTag) {
        ImageFile testImage = this.findImage(selectedFile);
        if (testImage != null) {
            testImage.removeTag(someTag);
            this.currentFile =  testImage.getCurrentDir();
        } else {
            this.currentFile = null;
        }
    }

    /**
     * Updates the location of an ImageFile.
     *
     * @param selectedFile: The ImageFile whose location is being updated.
     * @param newDir: The new location of the ImageFile.
     */
    public void updateLocation(File selectedFile, File newDir) {
        ImageFile testImage = this.findImage(selectedFile);
        if (testImage != null) {
            testImage.setFileDirectory(newDir);
            this.currentFile = testImage.getCurrentDir();
        } else {
            this.currentFile = null;
        }
    }

    /**
     * Adds and converts Files into ImageFiles when a File has pre-existing
     * tags that have not been added already by this application.
     *
     * @param selectedFile The file being processed.
     */
    public void addDirectoryFile(File selectedFile) {

        /* Arrives here iff the File had an "@" in it. */
        ImageFile testImage = this.findImage(selectedFile);
        if (testImage == null) {
            ImageFile newImage = new ImageFile(selectedFile, this.tagManager, this.masterLog);
            this.imageFiles.add(newImage);
        }
    }

    /**
     * Reverts the state of an ImageFile to some previous state.
     *
     * @param selectedFile: The File of which is being reverted.
     * @param oldState: The information as to how it is being reverted.
     */
    public void revertState(File selectedFile, ImageFileHistoryEntry oldState) {
        ImageFile test = this.findImage(selectedFile);
        if (test != null) {
            test.revertState(oldState);
            this.currentFile = test.getCurrentDir();
        } else {
            this.currentFile = selectedFile;
        }
    }

    /**
     * Returns all ImageFiles with the set of tags specified.
     *
     * @param tags Tags specified by the user to be searched for.
     * @return An ArrayList containing all files with relevant tags.
     */
    public ArrayList<File> gatherFilesWithTags(Tag[] tags, File dir, boolean filterOnlyDirFiles) {
        ArrayList<File> gatheredFiles = new ArrayList<>();
        for (ImageFile imageFile : imageFiles) {
            if (imageFile.hasAllTags(tags)) {
                if (!filterOnlyDirFiles) {
                    gatheredFiles.add(imageFile.getCurrentDir());
                } else if (isInSubDirectory(dir, imageFile.getCurrentDir())) {
                    gatheredFiles.add(imageFile.getCurrentDir());
                }
            }
        }
        return gatheredFiles;
    }

    /**
     * Helper that checks whether a file is in a subdirectory.
     *
     * @param dirToSearch The directory we're checking.
     * @param checkFile The file we're checking for.
     * @return Whether this is within the directory.
     *
     * @link https://stackoverflow.com/questions/18227634/check-if-file-is-in-subdirectory
     */
    private boolean isInSubDirectory(File dirToSearch, File checkFile) {
        if (checkFile == null)
            return false;
        if (checkFile.equals(dirToSearch))
            return true;
        return isInSubDirectory(dirToSearch, checkFile.getParentFile());
    }

    /**
     * Helper that returns an ImageFile with a memory reference to the correct ImageFile.
     *
     * @param selectedFile: The ImageFile containing the relevant content.
     * @return An ImageFile with the correct reference to memory address.
     */
    public ImageFile findImage(File selectedFile) {
        for (ImageFile thisImage : this.imageFiles) {
            if (selectedFile.equals(thisImage.getCurrentDir())) {
                return thisImage;
            }
        }
        return null;
    }

    /**
     * Updates the observer with all pertinent information.
     */
    private void hardUpdate() {
        for (ImageFile image : this.imageFiles) {
            image.hardUpdate(this.tagManager, this.masterLog);
        }
    }

    /**
     * Updates all ImageFiles to remove all tags. Called from the TagManager.
     *
     * @param o: The TagManager being updated.
     * @param arg: The Tag to be deleted.
     */
    @Override
    public void update(java.util.Observable o, Object arg) {
        Tag tag = (Tag) arg;
        for (ImageFile imageFile : this.imageFiles) {
            imageFile.removeTag(tag);
        }
    }

    /**
     * Reads all serialized data from a .ser file in the local directory.
     *
     * @param filePath: The location of the file to be read from.
     */
    @Override
    public void readFromFile(String filePath)  {
        this.ifmSave.readFromFile(filePath);
        this.imageFiles = this.ifmSave.getData();
        this.hardUpdate();
    }

    /**
     * Serializes all imageFileManager data and writes it to a file.
     *
     * @param filePath: The location of the file to be read from.
     */
    @Override
    public void saveToFile(String filePath) {
        this.ifmSave.saveToFile(filePath);
    }
}
