package org.krytonspace.carrent.controllers;

import org.krytonspace.carrent.controllers.event.DatabaseListener;
import org.krytonspace.carrent.controllers.event.ModelEvent;
import org.krytonspace.carrent.controllers.event.ModelListener;
import org.krytonspace.carrent.controllers.model.*;
import org.krytonspace.carrent.database.*;
import org.krytonspace.carrent.database.exceptions.LoaderNotFoundException;
import org.krytonspace.carrent.database.exceptions.LoadingFailedException;
import org.krytonspace.carrent.database.exceptions.WritingFailedException;
import org.krytonspace.carrent.models.DatabaseModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Main holder of application state. Each change to the database must be done
 * by the methods of this class or one of the specific controllers.
 */
public class DatabaseController implements ModelListener {

    /**
     * A list of listeners that are to be notified upon database change.
     */
    private final List<DatabaseListener> listeners = new LinkedList<>();

    /**
     * The underlying database.
     */
    private DatabaseModel model;
    private final ContractModelController contractController;
    private final VehicleModelController vehicleController;
    private final ClientModelController clientController;

    /**
     * This flag indicates unsaved changes.
     */
    private boolean dirty;
    /**
     * This holds the last file the controller has loaded.
     */
    private File lastLoadedFile;

    /**
     * Constructor.
     * Upon construction, the database controller holds an empty database.
     */
    public DatabaseController() {
        this.model = new DatabaseModel();
        this.clientController = new ClientModelController(model);
        this.contractController = new ContractModelController(model);
        this.vehicleController = new VehicleModelController(model);
        // There is nothing to save.
        this.dirty = false;
        this.lastLoadedFile = null;

        // The main controller should be notified upon model addition/removal/edition.
        clientController.addModelListener(this);
        contractController.addModelListener(this);
        vehicleController.addModelListener(this);
    }

    /**
     * Utility function that gives the Window a ready-to-open file chooser.
     * @return A file chooser to open/save database files
     */
    public static JFileChooser prepareFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        for (String ext : DatabaseHandlers.extensionsSupported()) {
            chooser.addChoosableFileFilter(
                    new FileNameExtensionFilter(ext.toUpperCase() + " files", ext));
        }

        return chooser;
    }

    /**
     * Load a database from a file.
     * @param f The file to read data from
     * @throws LoaderNotFoundException if a suitable loader could not be found
     * @throws LoadingFailedException if the file could not be loaded
     */
    public void loadModelFromFile(File f) throws LoaderNotFoundException, LoadingFailedException {
        // Notify all listeners
        for (DatabaseListener ev : listeners) {
            ev.onDatabaseLoading();
        }

        // Get a handler for this file
        DatabaseHandler db = DatabaseHandlers.getHandlerForFile(f.getAbsolutePath());
        // Load the database using this handler
        model = db.load();

        // Attach the new model to each sub-controller
        contractController.setModel(model);
        vehicleController.setModel(model);
        clientController.setModel(model);

        // Notify all listeners
        for (DatabaseListener ev : listeners) {
            ev.onDatabaseLoaded();
        }

        // Cache the loaded file.
        lastLoadedFile = f;
    }

    /**
     * Save the working database to a file.
     * @param f The file to write data to
     * @throws LoaderNotFoundException if a suitable loader could not be found
     * @throws WritingFailedException if the file could not be written
     */
    public void saveModelToFile(File f) throws LoaderNotFoundException, WritingFailedException {
        // Get a handler for this file
        DatabaseHandler db = DatabaseHandlers.getHandlerForFile(f.getAbsolutePath());
        // Save the database using this handler
        db.save(model);

        // Notify all listeners
        for (DatabaseListener ev : listeners) {
            ev.onDatabaseSaved();
        }

        // Mark as clean
        dirty = false;
    }

    /**
     * Getter
     * @return true if some changes are unsaved, false otherwise
     */
    public boolean hasUnsavedChanges() {
        return dirty;
    }

    /**
     * Getter
     * @return The last loaded file
     */
    public File getLastLoadedFile() {
        return lastLoadedFile;
    }

    /**
     * Getter
     * @return The client controller
     */
    public ClientController getClientController() {
        return clientController;
    }

    /**
     * Getter
     * @return The vehicle controller
     */
    public VehicleController getVehicleController() {
        return vehicleController;
    }

    /**
     * Getter
     * @return The contract controller
     */
    public ContractController getContractController() {
        return contractController;
    }

    /**
     * Add a new listener for listening to database events.
     * @param e The listener to add
     */
    public void addDatabaseEventListener(DatabaseListener e) {
        listeners.add(e);
    }

    /**
     * Remove a listener for listening to database events.
     * @param e The listener to remove
     */
    public void removeDatabaseEventListener(DatabaseListener e) {
        listeners.remove(e);
    }

    @Override
    public void onModelAdded(ModelEvent e) {
        for (DatabaseListener ev : listeners) {
            ev.onDatabaseChanged();
        }
        // New changes detected !
        dirty = true;
    }

    @Override
    public void onModelRemoving(ModelEvent e) {
        for (DatabaseListener ev : listeners) {
            ev.onDatabaseChanged();
        }
        // New changes detected !
        dirty = true;
    }

    @Override
    public void onModelEdited(ModelEvent e) {
        for (DatabaseListener ev : listeners) {
            ev.onDatabaseChanged();
        }
        // New changes detected !
        dirty = true;
    }
}
