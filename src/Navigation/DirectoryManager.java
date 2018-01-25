package Navigation;

import Application.AppRunner;
import Images.ImageFileManager;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.ArrayList;

/**
 * Manages all tasks related to directories.
 */

class DirectoryManager {

    /* Runs this application. */
    private AppRunner appRunner;

    /**
     * Constructs a new DirectoryManager.
     *
     * @param appRunner The appRunner sequence from which this is being run.
     */
    DirectoryManager(AppRunner appRunner) {
        this.appRunner = appRunner;
    }

    /**
     * Prompts the user to choose a directory.
     *
     * @return The directory chosen by the user.
     */
    File chooseDirectory() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        return dirChooser.showDialog(appRunner.getStage());
    }

    /**
     * Finds the File associated with the list of directory files. Accomplishes this by matching the File name
     * to the index of the directory file names, and then returning it.
     *
     * @param selectedFileName The name of the file being searched for.
     * @param dirFileNames     The list of corresponding directory file names.
     * @param dirFiles         The list of corresponding directory files.
     * @return The directory file associated with this name, from this list.
     */
    File findAssociatedDirFile(String selectedFileName, ArrayList<String> dirFileNames, ArrayList<File> dirFiles) {
        int chosenFileIndex = dirFileNames.indexOf(selectedFileName);
        return dirFiles.get(chosenFileIndex);
    }

    /**
     * Returns a list of image files in the current directory. Checks for files with pre-existing tags,
     * and passes them over to the ImageFileManager. This method gets used anytime the UIManager gets
     * a user request to view all image files in a given directory.
     *
     * @param currentDir The directory that the user has chosen to look through.
     * @return A list of all image files within the directory.
     *
     * Code adapted from this post:
     * @link https://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder
     */
    ArrayList<File> getDirectoryFiles(File currentDir) {
        File[] listOfFiles = currentDir.listFiles();
        ArrayList<File> allDirFiles = new ArrayList<>();

        if (listOfFiles != null) {
            for (File someFile : listOfFiles) {
                if (someFile.isFile()) {
                    String test = someFile.getName();
                    if (test.toLowerCase().endsWith(".jpg") || test.toLowerCase().endsWith((".png"))
                            || test.toLowerCase().endsWith(".bmp") || test.toLowerCase().endsWith(".gif")
                            || test.toLowerCase().endsWith(".tiff") || test.toLowerCase().endsWith(".jpeg")) {

                        /* Checks if the image has any tags.*/
                        if (test.contains("@")) {
                            ImageFileManager ourManager = appRunner.getImageFileManager();
                            ourManager.addDirectoryFile(someFile);
                        }
                        allDirFiles.add(someFile);
                    }

                  /* If it's a directory instead, we recurse and call the function on it. */
                } else if (someFile.isDirectory()) {
                    allDirFiles.addAll(getDirectoryFiles(someFile));
                }
            }
        }

        return allDirFiles;
    }

    /**
     * Prompts user to select a destination folder. Searches through destination folder
     * for any files with the same name. If a copy name is found, no changes are made.
     * Otherwise, the file is moved to new folder and recorded in history.
     *
     * Code adapted from this post:
     * @link https://www.mkyong.com/java/how-to-move-file-to-another-directory-in-java/
     */
    void moveSomeFile() {
        try {
            String newLocationName = appRunner.getMoveTargetDir().getPath() +
                    File.separator + appRunner.getSelectedFile().getName();
            File newFileLocation = new File(newLocationName);

            /* If the file doesn't already exist... */
            if (!newFileLocation.exists()) {
                ImageFileManager imageManager = appRunner.getImageFileManager();

                /* Update the ImageFile's location, check if it exists in our database. */
//                File updatedImageFile = imageManager.updateLocation(appRunner.getSelectedFile(), newFileLocation);
                imageManager.updateLocation(appRunner.getSelectedFile(), newFileLocation);
                File updatedImageFile = imageManager.getCurrentFile();
                if (updatedImageFile != null) {
                    appRunner.setSelectedFile(updatedImageFile);
                } else { // If this File hasn't been treated by our program.
                    appRunner.getSelectedFile().renameTo(newFileLocation);
                    appRunner.setSelectedFile(updatedImageFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
