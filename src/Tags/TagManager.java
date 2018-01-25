package Tags;

import Application.SaveFile;
import Application.SaveOperation;
import java.io.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * A class that handles all tasks related to Tags.
 */

public class TagManager extends Observable implements Observer, Serializable {

    /* An ArrayList of all available tags. */
    private ArrayList<Tag> allExistingTags;

    /**
     * Constructs a new TagManager.
     */
    public TagManager() {
        this.allExistingTags = new ArrayList<>();
    }

    public ArrayList<Tag> getAllExistingTags() {
        return allExistingTags;
    }

    /**
     * Updates the TagManager when any tag is added to an ImageFile.
     *
     * @param o: The ImageFile whose tag(s) are being added.
     * @param arg: The specific Tag to be added.
     */
    @Override
    public void update(Observable o, Object arg) {
        Tag tag = (Tag) arg;
        this.addTag(tag);
    }

    /**
     * Adds a Tag to allExistingTags.
     *
     * @param newTag: The new Tag to be added.
     */
    public void addTag(Tag newTag) {
        if (!allExistingTags.contains(newTag)) {
            this.allExistingTags.add(newTag);
        }
    }

    /**
     * Removes a Tag from allExistingTags and from all corresponding ImageFiles.
     *
     * @param oldTag: The Tag to be removed.
     */
    public void deleteTag(Tag oldTag) {
        this.allExistingTags.remove(oldTag);
        this.setChanged();
        this.notifyObservers(oldTag);
    }

    /**
     * If this Tag already exists, this will return the correct memory reference to it.
     * Otherwise, creates and returns a new Tag.
     *
     * @param newTagName string being checked
     * @return the new/found tag
     */
    public Tag createTag(String newTagName) {
        Tag newTag = new Tag(newTagName);
        for (Tag oldTag : this.allExistingTags) {
            if (oldTag.equals(newTag)) {
                return oldTag;
            }
        } return newTag;
    }
}
