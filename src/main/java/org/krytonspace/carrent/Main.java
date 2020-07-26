package org.krytonspace.carrent;

import org.krytonspace.carrent.controllers.DatabaseController;
import org.krytonspace.carrent.database.exceptions.LoaderNotFoundException;
import org.krytonspace.carrent.database.exceptions.LoadingFailedException;
import org.krytonspace.carrent.gui.Window;

import java.io.File;

/**
 * Application entry class.
 */
public class Main {

    /**
     * The application entry point.
     * @param args The command line args supplied to this program
     */
    public static void main(String[] args) {
        DatabaseController controller = new DatabaseController();
        Window window = new Window(controller);

        if (args.length > 0) {
            String databaseFile = args[0];
            try {
                // Load database on startup if provided.
                controller.loadModelFromFile(new File(databaseFile));
            } catch (LoaderNotFoundException | LoadingFailedException e) {
                Window.notifyException(e);
            }
        }

        window.show();
    }
}
