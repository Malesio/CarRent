package org.krytonspace.carrent.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.krytonspace.carrent.utils.ModelField;

/**
 * Base class for a model.
 */
public abstract class Model {
    @ModelField(name = "ID")
    private String id;

    /**
     * Internal model ID. Not stored on saves, restored by ModelFactory on load.
     */
    @JsonIgnore
    protected final int internalId;

    /**
     * Constructor.
     */
    public Model() {
        // Create a unique internal ID for this model.
        this.internalId = ModelFactory.generateIdForModel(getClass());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getInternalId() {
        return internalId;
    }
}
