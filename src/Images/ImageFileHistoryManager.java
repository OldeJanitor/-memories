package Images;

import Tags.Tag;
import Application.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Manages a log of all tagging performed by the user.
 */

public class ImageFileHistoryManager implements Serializable, SaveFile {

    /* The list of all history entries. */
    private ArrayList<ImageFileHistoryEntry> allHistory;
    /* Enables saving of all data. */
    private SaveOperation<ImageFileHistoryEntry> ifhmSave;

    public ImageFileHistoryManager() {
        this.allHistory = new ArrayList<>();
        this.ifhmSave = new SaveOperation<>(this.allHistory);
    }

    void addEntry(ImageFileHistoryEntry newEntry) {
        this.allHistory.add(newEntry);
    }

    public ArrayList<ImageFileHistoryEntry> getAllHistory() {
        return this.allHistory;
    }

    @Override
    public String toString() {
        StringBuilder newString = new StringBuilder("");
        for (ImageFileHistoryEntry entry : this.allHistory) {
            newString.append(entry.toString() + System.lineSeparator());
        }
        return newString.toString();
    }

    @Override
    public void readFromFile(String filePath) {
        this.ifhmSave.readFromFile(filePath);
        this.allHistory = this.ifhmSave.getData();
    }

    @Override
    public void saveToFile(String filePath) {
        this.ifhmSave.saveToFile(filePath);
    }

}



