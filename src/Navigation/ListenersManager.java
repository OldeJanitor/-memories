package Navigation;

import Application.AppRunner;
import Images.ImageFileHistoryEntry;
import Tags.Tag;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Class that manages all actions performed by the user.
 */

class ListenersManager {

    /* Runs the application. */
    private AppRunner appRunner;
    /* Responsible for all UI-related tasks. */
    private UIManager uiManager;

    /**
     * Constructs a new ListenersManager.
     *
     * @param appRunner: Runs the application.
     */
    ListenersManager(AppRunner appRunner, UIManager uiManager) {
        this.appRunner = appRunner;
        this.uiManager = uiManager;
    }

    /**
     * Updates the HistoryEntry selected by the user.
     */
    final ChangeListener<ImageFileHistoryEntry> HISTORY_ENTRY_CHANGE_LISTENER = new ChangeListener<ImageFileHistoryEntry>() {
        @Override
        public void changed(ObservableValue<? extends ImageFileHistoryEntry> observable,
                            ImageFileHistoryEntry oldValue, ImageFileHistoryEntry newValue) {
            appRunner.setSelectedHistoryEntry(newValue);
        }
    };
}
