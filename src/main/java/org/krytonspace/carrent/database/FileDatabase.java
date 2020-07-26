package org.krytonspace.carrent.database;

import org.krytonspace.carrent.database.exceptions.LoadingFailedException;
import org.krytonspace.carrent.database.exceptions.WritingFailedException;
import org.krytonspace.carrent.models.DatabaseModel;

import java.io.*;

/**
 * Base class implementing a handler based on a file.
 */
public abstract class FileDatabase implements DatabaseHandler {

    /**
     * The database file.
     */
    protected File file;

    /**
     * Constructor.
     * @param fileName The file to read from/write to
     */
    public FileDatabase(String fileName) {
        this.file = new File(fileName);
    }

    @Override
    public DatabaseModel load() throws LoadingFailedException {
        if (!file.exists()) {
            throw new LoadingFailedException("File '" + file.getName() + "' does not exist");
        }

        return parse(readFully());
    }

    @Override
    public void save(DatabaseModel model) throws WritingFailedException {
        writeFully(dump(model));
    }

    /**
     * Read a file completely in memory.
     * @return The file contents
     * @throws LoadingFailedException if the file could not be read
     */
    private String readFully() throws LoadingFailedException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new LoadingFailedException("Could not read '" + file.getName() + "' file: " + e.getMessage());
        }

        return sb.toString();
    }

    /**
     * Write a file completely from memory.
     * @param raw The file contents
     * @throws WritingFailedException if the file could not be written
     */
    private void writeFully(String raw) throws WritingFailedException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            bw.write(raw);
            bw.flush();
        } catch (IOException e) {
            throw new WritingFailedException("Could not write '" + file.getName() + "' file: " + e.getMessage());
        }
    }

    /**
     * Parse the content into a database model.
     * @param raw The file content to transform
     * @return The new database
     * @throws LoadingFailedException if the content is not a valid database dump
     */
    protected abstract DatabaseModel parse(String raw) throws LoadingFailedException;

    /**
     * Dump the database model to a string representation, that can be saved to a file.
     * @param model The database to dump
     * @return The string representation of the database
     * @throws WritingFailedException if the database could not be dumped
     */
    protected abstract String dump(DatabaseModel model) throws WritingFailedException;
}
