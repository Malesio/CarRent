package org.krytonspace.carrent.database;

import org.krytonspace.carrent.database.exceptions.LoaderNotFoundException;

import java.util.Arrays;
import java.util.List;

/**
 * Simple factory class, useful to create the right database handler.
 */
public final class DatabaseHandlers {

    /**
     * Can't create instances of this class.
     */
    private DatabaseHandlers() {

    }

    /**
     * Get a list of supported file extensions.
     * @return Supported extensions
     */
    public static List<String> extensionsSupported() {
        return Arrays.asList("json", "xml");
    }

    /**
     * Get a suitable handler for the specified file.
     * @param file A file path
     * @return A database handler
     * @throws LoaderNotFoundException if no suitable loader was found for this file
     */
    public static DatabaseHandler getHandlerForFile(String file) throws LoaderNotFoundException {
        String ext = file.substring(file.lastIndexOf('.') + 1);
        if (ext.equalsIgnoreCase("json")) {
            return new JsonDatabase(file);
        } else if (ext.equalsIgnoreCase("xml")) {
            return new XmlDatabase(file);
        } else {
            throw new LoaderNotFoundException("Could not find a compatible loader: unknown file format: " + ext);
        }
    }
}
