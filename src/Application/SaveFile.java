package Application;
import java.io.*;

/**
 * Interface representing all necessary tasks for saving a file.
 */
public interface SaveFile {

    /**
     * Reads all serialized data from a .ser file in the local directory.
     *
     * @param filePath: The location of the file to be read from.
     */
    void readFromFile(String filePath) throws ClassNotFoundException, IOException;

    /**
     * Serializes all imageFileManager data and writes it to a file.
     *
     * @param filePath: The location of the file to be read from.
     */
    void saveToFile(String filePath) throws IOException;
}
