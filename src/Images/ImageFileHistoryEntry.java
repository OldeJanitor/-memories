package Images;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * A class representing a new ImageFile update.
 */

public class ImageFileHistoryEntry implements Serializable {

    /* The timestamp for when this update was made. */
    private Timestamp timestamp;
    /* This ImageFileHistoryEntry's name as it appears to the user, with tags. */
    private String displayName;
    /* The previous entry's name. */
    private String oldName;

    /**
     * Constructs a new ImageFileHistoryEntry with its current displayName and list of tags.
     *
     * @param oldName: The previous name of the corresponding ImageFile.
     * @param newName: The ImageFile's display name at the time of modification.
     *
     * This code was adapted from:
     * @link https://stackoverflow.com/questions/8345023/need-to-get-current-timestamp-in-java
     */
    ImageFileHistoryEntry(String oldName, String newName) {
        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
        this.displayName = newName;
        this.oldName = oldName;
    }

    String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String toString() {
        return this.timestamp.toString() + ": " + this.displayName;
    }

    /**
     * A String method for viewing history entries in the Master Log.
     *
     * @return A string including the timestamp, old name, and new name.
     */
    public String masterString() {
        return this.timestamp.toString() + " : " + this.oldName + " ---> " + this.displayName;
    }
}
