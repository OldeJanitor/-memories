package Tags;

import java.io.Serializable;

/**
 * A class that represents a single Tag.
 */

public class Tag implements Serializable {

    /* The name belonging to this Tag. */
    private String tagName;

    /**
     * Constructs a new Tag.
     *
     * @param tagName: This tag's name.
     */
    public Tag(String tagName) {
        this.tagName = tagName;
    }

    /**
     * Returns a string representation of this Tag.
     *
     * @return The string representation of a Tag.
     */
    @Override
    public String toString() {
        return "@" + this.tagName;
    }

    /**
     * Returns true iff both Tags share the same tagName.
     *
     * @param other: The object to compare this Tag to.
     * @return A boolean value indicating whether their tagName is the same.
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof Tag && other.toString().equals(this.toString());
    }
}
