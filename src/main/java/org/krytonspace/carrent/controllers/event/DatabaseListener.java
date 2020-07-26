package org.krytonspace.carrent.controllers.event;

/**
 * Callback interface for actions that has to do with the database.
 */
public interface DatabaseListener {
    /**
     * This method is called at every modification in the database.
     */
    void onDatabaseChanged();

    /**
     * This method is called before a new database is loaded.
     */
    void onDatabaseLoading();

    /**
     * This method is called after a new database has been loaded.
     */
    void onDatabaseLoaded();

    /**
     * This method is called after the working database has been saved.
     */
    void onDatabaseSaved();
}
