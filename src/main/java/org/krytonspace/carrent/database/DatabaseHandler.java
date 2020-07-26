package org.krytonspace.carrent.database;

import org.krytonspace.carrent.database.exceptions.LoadingFailedException;
import org.krytonspace.carrent.database.exceptions.WritingFailedException;
import org.krytonspace.carrent.models.DatabaseModel;

/**
 * Interface denoting a database handler.
 */
public interface DatabaseHandler {
    /**
     * Load a new database.
     * @return A new database
     * @throws LoadingFailedException if the loader could not create the model
     */
    DatabaseModel load() throws LoadingFailedException;

    /**
     * Save a database.
     * @param model The database to save
     * @throws WritingFailedException if the loader could not save the model
     */
    void save(DatabaseModel model) throws WritingFailedException;
}
