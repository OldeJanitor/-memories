package Application;

import Images.ImageFileManager;

import java.io.*;
import java.util.ArrayList;
import Tags.*;
import java.io.Serializable;

/**
 * A class responsible for all Save File-related operations.
 *
 * @param <E>: The type of data being stored.
 */
public class SaveOperation<E> implements SaveFile, Serializable {

    /* Contains all data to be stored. */
    private ArrayList<E> data;

    public SaveOperation(ArrayList<E> data) {
        this.data = data;
    }

    public ArrayList<E> getData() {
        return this.data;
    }

    /**
     * Reads all serialized data from a .ser file in the local directory.
     *
     * @param filePath: The location of the file to be read from.
     */
    @Override
    public void readFromFile(String filePath) {
        File saveFile = new File(filePath);

        if (saveFile.exists()) {
            try {
                InputStream file = new FileInputStream(filePath);
                InputStream buffer = new BufferedInputStream(file);
                ObjectInput input = new ObjectInputStream(buffer);
                this.data = (ArrayList<E>) input.readObject();
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                saveFile.createNewFile();
                this.data = new ArrayList<E>();
            } catch (Exception i) {
                i.printStackTrace();
            }
        }
    }

    /**
     * Serializes all imageFileManager data and writes it to a file.
     *
     * @param filePath: The location of the file to be read from.
     */
    @Override
    public void saveToFile(String filePath) {
        try {
            OutputStream file = new FileOutputStream(filePath);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(this.data);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
