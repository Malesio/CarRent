package org.krytonspace.carrent.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.krytonspace.carrent.database.exceptions.LoadingFailedException;
import org.krytonspace.carrent.database.exceptions.WritingFailedException;
import org.krytonspace.carrent.models.DatabaseModel;

/**
 * Class implementing database reading/writing with JSON.
 */
public class JsonDatabase extends FileDatabase {

    private final ObjectMapper jsonParser;

    public JsonDatabase(String jsonFileName) {
        super(jsonFileName);
        jsonParser = new ObjectMapper();
    }

    @Override
    protected DatabaseModel parse(String raw) throws LoadingFailedException {
        DatabaseModel model;

        try {
            model = jsonParser.readValue(raw, DatabaseModel.class);
        } catch (JsonProcessingException e) {
            throw new LoadingFailedException("Could not parse model as JSON: " + e.getMessage());
        }

        return model;
    }

    @Override
    protected String dump(DatabaseModel model) throws WritingFailedException {
        String raw;

        try {
            raw = jsonParser.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new WritingFailedException("Could not dump model as JSON: " + e.getMessage());
        }

        return raw;
    }
}
