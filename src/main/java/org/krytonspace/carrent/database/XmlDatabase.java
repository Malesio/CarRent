package org.krytonspace.carrent.database;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.krytonspace.carrent.database.exceptions.LoadingFailedException;
import org.krytonspace.carrent.database.exceptions.WritingFailedException;
import org.krytonspace.carrent.models.DatabaseModel;

import java.util.List;

/**
 * Class implementing database reading/writing with XML.
 */
public class XmlDatabase extends FileDatabase {

    private final XmlMapper xmlMapper;

    public XmlDatabase(String jsonFileName) {
        super(jsonFileName);
        xmlMapper = new XmlMapper();

        // Small fix: parse empty XML list tags as empty lists, not 'null'.
        xmlMapper.configOverride(List.class)
                .setSetterInfo(JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY));
    }

    @Override
    protected DatabaseModel parse(String raw) throws LoadingFailedException {
        DatabaseModel model;

        try {
            model = xmlMapper.readValue(raw, DatabaseModel.class);
        } catch (JsonProcessingException e) {
            throw new LoadingFailedException("Could not parse model as JSON: " + e.getMessage());
        }

        return model;
    }

    @Override
    protected String dump(DatabaseModel model) throws WritingFailedException {
        String raw;

        try {
            raw = xmlMapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new WritingFailedException("Could not dump model as JSON: " + e.getMessage());
        }

        return raw;
    }
}